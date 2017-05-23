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
public class ApiController extends Controller{
  private String userId;
  private String providerId;
  private String submitButton;

  public Result api2(String topic) {
    //Dbtry db = new Dbtry();
    try {
        System.out.println(topic);
        String ans = DBwrapTest.dbtry.apiGetTopExperts(topic);
        return ok(ans);
    }
    catch (Exception e) {
        e.printStackTrace();
        return ok("API2 Request Fail");
    }
  }
  
 public Result api3(String topic) {
    try {
        System.out.println(topic);
        String ans = DBwrapTest.dbtry.apiGetGoingProjs(topic);
        return ok(ans);
    }
    catch (Exception e) {
        e.printStackTrace();
        return ok("API3 Request Fail");
    }
  }
  
 public Result api4(String topic) {
    //Dbtry db = new Dbtry();
    try {
        System.out.println(topic);
        String ans = DBwrapTest.dbtry.apiGetFinishProjs(topic);
        return ok(ans);
    }
    catch (Exception e) {
        e.printStackTrace();
        return ok("API4 Request Fail");
    }
  }
    public Result api5(String topic) {
        try {
            String ans = DBwrapTest.dbtry.apiGetAllProjFinishedWithRating(topic);
            return ok(ans);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ok("API5 Request Fail");
        }
    }
   public Result api7() {
    try {
        String ans = DBwrapTest.dbtry.apiGetGoingProjs();
        return ok(ans);
    }
    catch (Exception e) {
        e.printStackTrace();
        return ok("API7 Request Fail");
    }
  }
  
   public Result api8() {
    try {
        String ans = DBwrapTest.dbtry.apiGetFinishProjs();
        return ok(ans);
    }
    catch (Exception e) {
        e.printStackTrace();
        return ok("API8 Request Fail");
    }
  }
  
}