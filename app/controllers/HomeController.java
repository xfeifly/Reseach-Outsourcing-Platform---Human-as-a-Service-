package controllers;

import play.api.libs.mailer.MailerClient;
import play.libs.mailer.Email;
import play.mvc.*;

import views.html.*;
import play.data.*;
import models.*;

import javax.inject.Inject;
import java.io.*;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.lang.reflect.Array;
import java.util.ArrayList;



/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    private String userId;
    private final MailerClient mailer;

    @Inject
    public HomeController(MailerClient mailer){
        this.mailer = mailer;
    }

    public Result index() {
        return ok(home.render());
    }

    public Result renderSignIn(){
        return  ok(signIn.render(""));
    }

    public Result renderRegister(){
        return ok(register.render());
    }

    public Result renderIntroductionForUser(){
        return ok(userIntro.render());
    }

    public Result renderIntroductionForPro(){
        return ok(providerIntro.render());
    }
    //sign in function
    public Result signin(){
        // String content = Page.getContentOf(info);
        DynamicForm in   = Form.form().bindFromRequest();
        String username    = in.get("username");
        String password  = in.get("password");
        String userType = in.get("selection");
        String signInSubmit = in.get("signInSubmit");
        String RegSubmit = in.get("RegSubmit");
        String action = (signInSubmit == null || signInSubmit.length() == 0) ? RegSubmit : signInSubmit;


        if(action.equals("Sign In")){
             //Dbtry db = new Dbtry();
             int types = 0;
             if(userType.equals("User")) {
                 types = 1;
             }else if(userType.equals("Provider")){
                 types = 2;
             }else if(userType.equals("Admin")){
                 types = 3;
             }
            try {
                 int id = DBwrapTest.dbtry.validateLogin(username, password, types);
                 userId = String.valueOf(id);
                 System.out.println("username: " + username + "userId: " + userId + "types: " + types);
                if (id < 0) {
                     return ok(signIn.render("Password or user id is wrong!"));
                 }
                 else {
                     if (types == 1) {
                         return ok(userMiddlePage.render(String.valueOf(id)));
                     }else if(types == 2) {
                         return ok(providerMiddlePage.render(String.valueOf(id)));
                     }
                     ArrayList<ArrayList<String>> allNames = getUsersNamesSeperateFromBackEnd();
                     ArrayList<String> users = allNames.get(0);
                     ArrayList<String> providers = allNames.get(1);
                     return ok(AdminPage.render(userId, users, providers));
                 }

            }
            catch (Exception e) {
                e.printStackTrace();
                return ok("login fail");
            }
        }else {
            return ok(register.render());
        }
    }
    //Admin maintenance function(delete, send contract and ...)
    public Result maintenance(){
        DynamicForm in   = Form.form().bindFromRequest();
        String userId = in.get("UserId");
        String submitButton = in.get("Submit");
        String userIndex = in.get("users");
        String providerIndex = in.get("providers");
        //Dbtry db = new Dbtry();

        int uIndex = -1;
        int pIndex = -1;
        if(userIndex != null && userIndex.length() != 0){
            uIndex = Integer.valueOf(userIndex);
        }
        if(providerIndex != null && providerIndex.length() != 0){
            pIndex = Integer.valueOf(providerIndex);
        }
        if(submitButton.equals("Return")){
            return ok(signIn.render(""));
        }else if(submitButton.equals("DeletUser")){
            try{
                if(uIndex >= 0){//delete a user
                    DBwrapTest.dbtry.deleteUser(getUsersSeperateFromBackEnd().get(0).get(uIndex).getID());
                }
                ArrayList<ArrayList<String>> all = getUsersNamesSeperateFromBackEnd();
                ArrayList<String> users = all.get(0);
                ArrayList<String> providers = all.get(1);
                return ok(AdminPage.render(userId, users, providers));
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(submitButton.equals("DeletProvider")){
            try{
                if(pIndex >= 0){ //delete a provider
                    DBwrapTest.dbtry.deleteUser(getUsersSeperateFromBackEnd().get(1).get(pIndex).getID());
                }
                ArrayList<ArrayList<String>> all = getUsersNamesSeperateFromBackEnd();
                ArrayList<String> users = all.get(0);
                ArrayList<String> providers = all.get(1);
                return ok(AdminPage.render(userId, users, providers));
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            ArrayList<ArrayList<String>> all = getUsersNamesSeperateFromBackEnd();
            ArrayList<String> users = all.get(0);
            ArrayList<String> providers = all.get(1);
            if(uIndex >= 0 && pIndex >= 0){
                ArrayList<ArrayList<User>> allUsers = getUsersSeperateFromBackEnd();
                String sendToUserId = allUsers.get(0).get(uIndex).getID();
                String sendToProviderId = allUsers.get(1).get(pIndex).getID();
                sendContract(sendToUserId, sendToProviderId);
                return ok(AdminPage.render(userId, users, providers));

            }else {
                return ok(AdminPage.render(userId, users, providers));
            }
        }
        return ok("Invalid Input");
    }


    //get ALL users' name from back-end
    private ArrayList<ArrayList<String>> getUsersNamesSeperateFromBackEnd(){
        ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
        try{
            ArrayList<User> allUsers = DBwrapTest.dbtry.getAllUsers();
            ArrayList<String> users = new ArrayList<String>();
            ArrayList<String> providers = new ArrayList<String>();
            for(User u : allUsers){
                if(u.getUserType() == 1){
                    users.add(u.getUsername());
                    System.out.println("user: " + u.getUsername());
                }else if(u.getUserType() == 2){
                    providers.add(u.getUsername());
                    System.out.println("provider: " + u.getUsername());
                }
            }
            ret.add(users);
            ret.add(providers);
            return ret;
        }catch(Exception e){
            e.printStackTrace();
        }
        return ret;
    }

    //get all the User Object from back-end
    private ArrayList<ArrayList<User>> getUsersSeperateFromBackEnd(){
        ArrayList<ArrayList<User>> ret = new ArrayList<ArrayList<User>>();
        try{
            ArrayList<User> allUsers = DBwrapTest.dbtry.getAllUsers();
            ArrayList<User> users = new ArrayList<User>();
            ArrayList<User> providers = new ArrayList<User>();
            for(User u : allUsers){
                if(u.getUserType() == 1){
                    users.add(u);
                    System.out.println("user: " + u.getUsername());
                }else if(u.getUserType() == 2){
                    providers.add(u);
                    System.out.println("provider: " + u.getUsername());
                }
            }
            ret.add(users);
            ret.add(providers);
            return ret;
        }catch(Exception e){
            e.printStackTrace();
        }
        return ret;
    }
    //register
    public Result register() {
        DynamicForm in   = Form.form().bindFromRequest();
        String submitButton = in.get("Submit");
        String password    = in.get("password");
        String userid    = in.get("userID");
        String repassword = in.get("repassword");
        String email = in.get("email");
        String userType = in.get("selection");
        String securityQuestion = in.get("security_question");
        User u = new User();
        u.setUsername(userid);
        u.setUserpasswd(password);
        System.out.println("User type::" + userType);

        if(submitButton.equals("Return")){
            return ok(signIn.render(""));
        }

        if (userType.equals("User")) {
            u.setUserType(1);
        }
        else
            u.setUserType(2);
        System.out.println("User type::" + u.getUserType());
        u.setUserMail(email);
        u.setUserQuestion("2333");
        System.out.println(password);
        System.out.println(userid);
        System.out.println(repassword);
        System.out.println(email);
        if (!password.equals(repassword))
            return ok("password mismatch");
        try {
            DBwrapTest.dbtry.insertNewUser(u);
            return ok(home.render());
        }
        catch (Exception e) {
            return ok("Fatal Error");
        }
       // System.out.println(securityQuestion);
        // return ok(password);
    }

    //send contract
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
    //build contract
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
