package controllers;

import models.DBwrapTest;
import models.Dbtry;
import models.Profile;
import models.Project;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.*;
import java.util.*;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class UserController extends Controller {
    private ArrayList<String> providers; //provider names with properties
    private String userId;
    private String submitButton;
    private String projectName;
    private String description;
    private String expertise;
    private String start;
    private String end;
    private String price;
    public List<String> ongoing = new ArrayList<String>();
    public List<String> finished = new ArrayList<String>();

    //routing function for user middle page
    public Result routing() {
        DynamicForm in   = Form.form().bindFromRequest();
// 		String selection = in.get("selection");
        userId = in.get("UserId");
        submitButton = in.get("Submit");

        if(submitButton.equals("Return")){
            return ok(signIn.render(""));
        }

        if(submitButton.equals("UserInput")){
            return ok(userRequest.render(userId, ""));
        }
        if(submitButton.equals("ShowProviders")){
            return ok(showProviders.render(userId));
        }
        //////////////////////////////////////// User Selection 3: show user Info /////////////////////////////
        if(submitButton.equals("ShowUserInfo")){
            //Dbtry d = new Dbtry();
            try{
                System.out.println(1);
                Dbtry.ReturnUserInfo userInfo = DBwrapTest.dbtry.listUserInfo(userId);
                System.out.println(2);
                String UserId = userInfo.getUserID();
                System.out.println(UserId);
                List<String> requestedServiceKeyowords = userInfo.getRequestedServiceKeywords();
                System.out.println(requestedServiceKeyowords.size());
                System.out.println(3);
                List<String> ongoing = userInfo.getOngoingProj();
                List<String> finished = userInfo.getFinishedProj();
                System.out.println(4);
                return ok(showUserInfo.render(UserId, requestedServiceKeyowords, ongoing, finished));
            }
            catch(Exception e){
                e.printStackTrace();
                System.out.println(6);
            }
        }

        return ok("Invalid selection!");
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////User Selection 1: User Query Input////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //User selection 1. query for certain service
    public Result search(){
        DynamicForm in   = Form.form().bindFromRequest();
        projectName = in.get("ProjectName");
        description = in.get("Description");
        expertise = in.get("Expertise");
        start = in.get("Start");
        end = in.get("End");
        price = in.get("Price");
        userId = in.get("UserId");
        submitButton = in.get("Submit");

        if(submitButton.equals("Return")){
            return ok(userMiddlePage.render(userId));
        }

        if(projectName == null || projectName.length() == 0){
            return ok(userRequest.render(userId, "Project-Name cannot be null!"));
        }

        System.out.println(projectName);
        //Dbtry db = new Dbtry();
        Project p = new Project();
        p.setProjectName(projectName);
        p.setProjectDesc(description);
        p.setProjectReq(expertise);
        p.setProjectPrice(price);
        p.setProjectTimeStart(start);
        p.setProjectTimeEnd(end);
        ArrayList<Project> ans;
        System.out.println("begin checking");
        try {
            ans = DBwrapTest.dbtry.queryProject(userId, p);
            System.out.print(ans.size());
            ArrayList<Project> newProjectList = new ArrayList<Project>();
            ArrayList<Project> ongoingProjectList = new ArrayList<Project>();
            ArrayList<Project> finishedProjectList = new ArrayList<Project>();
            return classify(ans, newProjectList, ongoingProjectList, finishedProjectList);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ok("submit fail");
        }

    }

    public Result classify(ArrayList<Project> projectList, ArrayList<Project> newProjectList,
                           ArrayList<Project> ongoingProjectList, ArrayList<Project> finishedProjectList) throws Exception{
        for (Project j : projectList) {
            if(j.getProjectStatus().equals("Finished")) {
                finishedProjectList.add(j);
            }

            else if(j.getProjectStatus().equals("Ongoing")) {
                ongoingProjectList.add(j);
            }
            else {
                newProjectList.add(j);
            }
        }
        return ok(showProjects.render(newProjectList, ongoingProjectList, finishedProjectList));
    }

    public Result sortedSearch(){
        DynamicForm in   = Form.form().bindFromRequest();
        submitButton = in.get("Submit");

        if(submitButton.equals("Return")){
            return ok(userRequest.render(userId, ""));
        }

        if(projectName == null || projectName.length() == 0){
            return ok(userRequest.render(userId, "Project-Name cannot be null!"));
        }

        System.out.println(projectName);
        //Dbtry db = new Dbtry();
        Project p = new Project();
        p.setProjectName(projectName);
        p.setProjectDesc(description);
        p.setProjectReq(expertise);
        p.setProjectPrice(price);
        p.setProjectTimeStart(start);
        p.setProjectTimeEnd(end);
        ArrayList<Project> ans;
        try {
            ans = DBwrapTest.dbtry.queryProject(userId, p);
            ArrayList<Project> newProjectList = new ArrayList<Project>();
            ArrayList<Project> ongoingProjectList = new ArrayList<Project>();
            ArrayList<Project> finishedProjectList = new ArrayList<Project>();
            return classify2(ans, newProjectList, ongoingProjectList, finishedProjectList);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ok("Submit fail");
        }

    }

    public Result classify2(ArrayList<Project> projectList, ArrayList<Project> newProjectList,
                            ArrayList<Project> ongoingProjectList, ArrayList<Project> finishedProjectList) throws Exception{
        for (Project j : projectList) {
            if(j.getProjectStatus().equals("Finished")) {
                finishedProjectList.add(j);
            }

            else if(j.getProjectStatus().equals("Ongoing")) {
                ongoingProjectList.add(j);
            }
            else {
                newProjectList.add(j);
            }
        }
        return ok(showSortProjects.render(newProjectList, ongoingProjectList, finishedProjectList));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////User Selection 2: show all providers/////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //User Selection 2. show all providers : search function of search all providers in different ways
    public Result searchProviders(){
        DynamicForm in   = Form.form().bindFromRequest();
        userId = in.get("UserId");
        submitButton = in.get("Submit");
        String selection = in.get("selection");
        String expertise = in.get("Expertise");
        String keywords = in.get("Keywords");

        if(submitButton.equals("Return")){
            return ok(userMiddlePage.render(userId));
        }

        try {
            if(selection.equals("Popularity")){
                //Dbtry db = new Dbtry();
                providers = DBwrapTest.dbtry.showPopularity();
                if(providers.size() == 0){
                    return ok(showProviders.render(userId));
                }
                return ok(showProvidersNames.render(providers, userId));
            }
            if(selection.equals("Ranking")){
                //Dbtry db = new Dbtry();
                providers = DBwrapTest.dbtry.showRating();
                if(providers.size() == 0){
                    return ok(showProviders.render(userId));
                }
                return ok(showProvidersNames.render(providers, userId));
            }
            if(selection.equals("Expertise")){
                //Dbtry db = new Dbtry();
                providers = DBwrapTest.dbtry.showExpertise(expertise);
                if(providers.size() == 0){
                    return ok(showProviders.render(userId));
                }
                return ok(showProvidersNames.render(providers, userId));
            }
            if(selection.equals("KeyWords")){
                //Dbtry db = new Dbtry();
                providers = DBwrapTest.dbtry.showKeyWord(keywords);
                if(providers.size() == 0){
                    return ok(showProviders.render(userId));
                }
                return ok(showProvidersNames.render(providers, userId));
            }
            return ok("default");
        }
        catch (Exception e) {
            e.printStackTrace();
            return ok("fail");
        }
    }

    //select certain provider and go into his details or return
    public Result showProviderInfo(){
        DynamicForm in   = Form.form().bindFromRequest();
        String providerString = in.get("provider");
        submitButton = in.get("Submit");
        userId = in.get("UserId");

        if(submitButton.equals("Return")){
            return ok(showProviders.render(userId));
        }

        try {
            String[] providerInfo = providerString.split(",");
            String providerID = providerInfo[0];
            System.out.println("ProviderId: " + providerID + "in showProvidersNamesController");
            Profile p = DBwrapTest.dbtry.showProfile(providerID);

            // TODO: parse profile
            String profileCredential = p.getProfileCredential();
            String profileArea = p.getProfileArea();
            String profilePublication = p.getProfilePublication();
            String profileService = p.getProfileService();
            ongoing = p.getOngoingProjs();
            finished = p.getFinishedProjs();
            List<String> comment = DBwrapTest.dbtry.showComment(providerID);
            return ok(showProviderInfo.render(providerID, userId, profileCredential, profileArea, profilePublication, profileService, ongoing, finished, comment));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ok("fail");
        }
    }
    //////////////////////////////////////////Return processing///////////////////////////////////////////
    //from provider info page:
    //1. go to send email
    //2. return to providers names list page
    //3. project rating
    //4. make comments
    public Result returnToProviderNames(){
        //Dbtry db = new Dbtry();
        DynamicForm in   = Form.form().bindFromRequest();
        submitButton = in.get("Submit");
        //String selection = in.get("selection");
        List<String> ongoCur = new ArrayList<String>();
        List<String> finishCur = new ArrayList<String>();
        String comment = in.get("Comment");
        String providerId = in.get("providerId");
        String userId = in.get("UserId");
        String Area = in.get("Area");
        String Publication = in.get("Publication");
        String credential = in.get("credential");
        String profileService = in.get("profileService");

        if(submitButton.equals("Return")){
            return ok(showProvidersNames.render(providers, userId));
        }

        if(submitButton.equals("Send Email"))
        {
            return ok(email.render("", providerId, userId));
        }

        if(submitButton.equals("Comment")) {
            //System.out.println("providerID:      " + providerId + "       userID       " + userId + "          comment:     " + comment);
            try{
                DBwrapTest.dbtry.addComment(providerId, userId, comment);
                List<String> dbComment = DBwrapTest.dbtry.showComment(providerId);
                return ok(showProviderInfo.render(providerId, userId, credential, Area, Publication, profileService, ongoing, finished, dbComment));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        try {
            //Profile p = db.showProfile(providerId);
            if(submitButton.equals("Submit"))
            {
                for(String s : ongoing) {
                    String selection = in.get(s);
                    ongoCur.add(selection);
                    String[] selectionInfo = selection.split(":");
                    String projectRank = selectionInfo[0];
                    String projectName = selectionInfo[1];
                    System.out.println("projectName: " +projectName + "        projectRank: " + projectRank);
                    DBwrapTest.dbtry.changeProjectRating(projectName, projectRank);
                }
                for(String f : finished) {
                    String selection = in.get(f);
                    finishCur.add(selection);
                    String[] selectionInfo = selection.split(":");
                    String projectRank = selectionInfo[0];
                    String projectName = selectionInfo[1];
                    System.out.println("projectName: " +projectName + "        projectRank: " + projectRank);
                    DBwrapTest.dbtry.changeProjectRating(projectName, projectRank);
                }
                List<String> dbComment = DBwrapTest.dbtry.showComment(providerId);
                return ok(showProviderInfo.render(providerId, userId, credential, Area, Publication, profileService, ongoing, finished, dbComment));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return ok("rating fail");
        }
        return ok("Invalid Selection!");
    }
    //return to the user middle page
    public Result returnToMiddle(){
        return ok(userMiddlePage.render(userId));
    }
}