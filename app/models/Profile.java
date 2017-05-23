package models;

import java.util.ArrayList;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 * 
 * this is is provider profile
 */
public class Profile {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    String profileID; // also ownerId in project
    String profileCredential;
    String profileArea;
    String profilePublication;
    String profileService;
    // String profileProject;
    List<String> finishedProjs = new ArrayList<>();
    List<String> OngoingProjs = new ArrayList<>();

    public void setID(String e) {
        this.profileID = e;
    }

    public String getID() {
        return profileID;
    }

    public void setProfileCredential(String e) {
        this.profileCredential = e;
    }

    public String getProfileCredential() {
        return profileCredential;
    }

    public void setProfileArea(String e) {
        this.profileArea = e;
    }

    public String getProfileArea() {
        return profileArea;
    }

    public void setProfilePublication(String e) {
        this.profilePublication = e;
    }

    public String getProfilePublication() {
        return profilePublication;
    }

    public void setProfileService(String e) {
        this.profileService = e;
    }

    public String getProfileService() {
        return profileService;
    }
    
    public void setFinishedProjs(String e) {
        this.finishedProjs.add(e);
    }

    public void setOngoingProjs(String e) {
        this.OngoingProjs.add(e);
    }

    public List<String> getFinishedProjs() {
        return finishedProjs;
    }

    public List<String> getOngoingProjs() {
        return OngoingProjs;
    }

}