package controllers;

import play.mvc.*;
import java.util.*;
import views.html.*;
import play.data.*;
import models.*;

public class ProviderController extends Controller{
    String userId;
    String submitButton;
    String selection;
    ArrayList<String> projectNames;
    ArrayList<Project> projects;
    
    
    //for routing in the "providermiddlePage"
    public Result routing(){
		DynamicForm in   = Form.form().bindFromRequest();
		userId = in.get("UserId");
		submitButton = in.get("Submit");
		selection = in.get("selection");
		if(submitButton.equals("Return")){
		    return ok(signIn.render(""));
		}
		
		System.out.println(selection);
		
	    if(selection.equals("PublishService")){
	        return ok(servicePublication.render(userId));
	    }
       // Dbtry db = new Dbtry();
        System.out.println(userId);
        try{
	        projects = DBwrapTest.dbtry.showProject(userId);
	        projectNames = new ArrayList<String>();
	        for(Project p : projects){
	            projectNames.add("Project Name: " + p.getProjectName() + "; Project ID: " + p.getID());
	            System.out.println(p.getProjectName());
	        }
	        return ok(editProjects.render(projectNames, userId));
	        
        }catch(Exception e){
            e.printStackTrace();
        }
        return ok("invalid");
    }
    
    // for submiting service and project detais in "servicePublication" page
    public Result provide() {
        DynamicForm in   = Form.form().bindFromRequest();
        String credential = in.get("Credential");
        String research  = in.get("Research");
        String publication = in.get("Publication");
        String projectName = in.get("ProjectName");
        String projectDes = in.get("ProjectDes");
        String projectExp = in.get("ProjectExpertise");
        String startTime = in.get("StartTime");
        String endTime = in.get("EndTime");
        String projectPrice = in.get("ProjectPrice");
        String service = in.get("Service");
        String status = in.get("StatusSelection");
        userId = in.get("UserId");
        submitButton = in.get("Submit");

        if(submitButton.equals("Return")){
            return ok(providerMiddlePage.render(userId));
        }
        
        //Dbtry db = new Dbtry();

        Profile p = new Profile();
        p.setID(userId);
        p.setProfileCredential(credential);
        p.setProfileArea(research);
        p.setProfilePublication(publication);
        p.setProfileService(service);


        Project pj = new Project();
        pj.setOwnerID(userId);
        pj.setProjectStatusChange(status);
        pj.setProjectName(projectName);
        pj.setProjectDesc(projectDes);
        pj.setProjectReq(projectExp);
        pj.setProjectTimeStart(startTime);
        pj.setProjectTimeEnd(endTime);
        pj.setProjectPrice(projectPrice);
        pj.setProjectRating("0");

        try {
            DBwrapTest.dbtry.insertNewProfile(p);
            DBwrapTest.dbtry.insertNewProject(pj);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ok("Submit Fail!");
        }
        return ok(servicePublication.render(userId));
    }
    
    
    //for changing project status in the "editProjects" page
    public Result editProjectStatus(){
		DynamicForm in   = Form.form().bindFromRequest();
		userId = in.get("UserId");
		submitButton = in.get("Submit");
		String projectIndex = in.get("projectsIndex");
		String selection = in.get("StatusSelection");
		
		if(submitButton.equals("Return")){
		    return ok(providerMiddlePage.render(userId));
		}

        System.out.println(projectIndex);
		int index = Integer.valueOf(projectIndex);
		
		System.out.println(projectNames.get(index));
		String proID = projects.get(index).getID();
		System.out.println(proID);
		//Dbtry db = new Dbtry();
		try{
            DBwrapTest.dbtry.changeProjectStatus(proID, selection);
		    
		}catch(Exception e){
		    e.printStackTrace();
		}
		return ok(editProjects.render(projectNames, userId));

    }
    
}