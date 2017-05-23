package controllers;

import com.sun.org.apache.regexp.internal.RE;
import play.mvc.*;
import java.util.*;
import views.html.*;
import play.data.*;
import models.*;

/**
 * Created by feixu on 12/1/16.
 */
public class adminController extends Controller{
    private String userId;
    private String submitButton;

    public Result deleteUser(){
        DynamicForm in   = Form.form().bindFromRequest();
        userId = in.get("UserId");
        submitButton = in.get("Submit");
        String userIndex = in.get("users");
        String providerIndex = in.get("providers");
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
        }

        return ok("delete");
    }

    public Result generateContract(){
        return ok("generate");
    }


}
