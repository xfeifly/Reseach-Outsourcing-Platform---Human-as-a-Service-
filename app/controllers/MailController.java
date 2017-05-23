package controllers;

import models.Dbtry;
import play.api.libs.mailer.MailerClient;
import play.libs.mailer.Email;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.*;
import views.html.*;
import play.data.*;
import models.*;
import java.util.*;

import javax.inject.Inject;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 * 
 * thisi is provider profile
 */
public class MailController extends Controller{
  private final MailerClient mailer;
  private String userId;
  private String providerId;
  private String submitButton;

  @Inject
  public MailController(MailerClient mailer) {
    this.mailer = mailer;
  }

  public String send(String userID, String providerID, String title, String Document) {
    //Dbtry db = new Dbtry();
    try {
          String address1 = DBwrapTest.dbtry.getMailAddress(userID);
          String newAddress1 = "<" + address1 + ">";
          String address2 = DBwrapTest.dbtry.getMailAddress(providerID);
          String newAddress2 = "<" + address2 + ">";
        Email email = new Email().setSubject(title).setFrom("SOCTEAM-5 <socpublishment@gmail.com>")
       .addTo(newAddress2)
       .setBodyText(Document);
        String id = mailer.send(email);
        return "Email has sent!";
    }
    catch (Exception e) {
        e.printStackTrace();
        return "Email sending fail!";
    }
  }
  
  public Result sendContract(String userID, String providerID) {
      //Dbtry db = new Dbtry();
      try {
          String address1 = DBwrapTest.dbtry.getMailAddress(userID);
          String newAddress1 = "<" + address1 + ">";
          String address2 = DBwrapTest.dbtry.getMailAddress(providerID);
          String newAddress2 = "<" + address2 + ">";
          String name1 = DBwrapTest.dbtry.getUserName(userID);
          String name2 = DBwrapTest.dbtry.getUserName(providerID);
          buildContract(name1, name2);
          Email email1 = new Email().setSubject("Service Agreement")
          .setFrom("Service Agreement <socpublishment@gmail.com>")
          .addTo(newAddress1)
          .setBodyText("Please check your new Agreement on Attachment list")
          .addAttachment("TheContract.txt", new File("TheContract.txt"));
          String id = mailer.send(email1);
          Email email2 = new Email().setSubject("Service Agreemnt")
                  .setFrom("Service Agreement <socpublishment@gmail.com>")
                  .addTo(newAddress2)
                  .setBodyText("Please check your new Agreement on Attachment list")
                  .addAttachment("TheContract.txt", new File("TheContract.txt"));
          String id2 = mailer.send(email2);
          return ok("Email has sent");
      } catch (Exception e) {
          e.printStackTrace();
          return ok("Bug exists");
      }
  }

  public Result sendEmailToProvider() {
      DynamicForm in   = Form.form().bindFromRequest();
      userId = in.get("UserId");
      providerId = in.get("ProviderId");
      submitButton = in.get("Submit");
      String providerId = in.get("ProviderId");
      String title = in.get("emailTitle");
      String content = in.get("emailContent");
      
      if(submitButton.equals("Return")){
          	try{
          	    System.out.println("Return: " + providerId);
                Profile p = DBwrapTest.dbtry.showProfile(providerId);
                String profileCredential = p.getProfileCredential();
    	        String profileArea = p.getProfileArea();
    	        String profilePublication = p.getProfilePublication();
    	        String profileService = p.getProfileService();
    	        List<String> ongoing = p.getOngoingProjs();
    	        List<String> finished = p.getFinishedProjs();
                List<String> dbcomment = DBwrapTest.dbtry.showComment(providerId);
        	    return ok(showProviderInfo.render(providerId, userId, profileCredential, profileArea, profilePublication, profileService, ongoing, finished, dbcomment));
          	}catch(Exception e){
          	    e.printStackTrace();
          	}
      }
      
      String ret = send(userId, providerId, title, content);
      return ok(email.render(ret, providerId, userId));
    //   return ok(userId + "Email " +  providerId + " title: " + title + " content: "  +  content + " sent!");

  }

  // build contract of service requester and provider
  public void buildContract(String username, String providerName) throws IOException {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    System.out.println(dateFormat.format(date));

    File file = new File("app/resource/contract.txt");
    BufferedReader brs = new BufferedReader(new FileReader(file));
    StringBuilder sb = new StringBuilder();
    String input = "";
    while ((input = brs.readLine()) != null) {
      sb.append(input).append("\n");
      String buyer = "[the Buyer]";
      String provider = "[the Service Provider]";
      String time = "[Month, day, year]";
      if(input.equals(buyer)) {
        int idx = sb.indexOf(buyer);
        sb.delete(idx, idx + buyer.length());
//        sb.append("Doctor Strange \n");
        sb.append(username).append("\n");
//        System.out.println("oh yeah1" + input);
      }
      if(input.equals(provider)) {
        int idx = sb.indexOf(provider);
        sb.delete(idx, idx + provider.length());
//        sb.append("Wonda Maximoff \n");
        sb.append(providerName).append("\n");
//        System.out.println("oh yeah 2" + input);
      }
      if(input.equals(time)) {
        int idx = sb.indexOf(time);
        sb.delete(idx, idx + time.length());
        sb.append(date.toString());
      }

    }
    brs.close();
    File tmpFile = new File("TheContract.txt");
    FileOutputStream outs = new FileOutputStream(tmpFile);
    outs.write(sb.toString().getBytes());
    outs.close();

  }
}