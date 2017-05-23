package models;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class Project {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    String projectID;
    String ownerID;
    String projectName;
    String projectDesc;
    String projectReq;
    String projectTimeStart;
    String projectTimeEnd;
    String projectPrice;
    String projectRating;
    String projectStatusChange;

    public void setOwnerID(String e) {
        this.ownerID = e;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setID(String e) {
        this.projectID = e;
    }

    public String getID() {
        return projectID;
    }

    public void setProjectName(String e) {
        this.projectName = e;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectDesc(String e) {
        this.projectDesc = e;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public void setProjectReq(String e) {
        this.projectReq = e;
    }

    public String getProjectReq() {
        return projectReq;
    }

    public void setProjectTimeStart(String e) {
        this.projectTimeStart = e;
    }

    public String getProjectTimeStart() {
        return projectTimeStart;
    }

    public void setProjectTimeEnd(String e) {
        this.projectTimeEnd = e;
    }

    public String getProjectTimeEnd() {
        return projectTimeEnd;
    }

    public void setProjectPrice(String e) {
        this.projectPrice = e;
    }

    public String getProjectPrice() {
        return projectPrice;
    }

    public String getProjectStatus() throws ParseException {
        String highStatus = getProjectStatusChange();
        if(highStatus != null && highStatus.length() != 0) {
            return highStatus;
        }
//        dataformat:  yyyy-MM-dd
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String curr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Date currTime = df.parse(curr);
        Date start =  df.parse(getProjectTimeStart());
        Date end = df.parse(getProjectTimeEnd());
        if(end.before(currTime)) {
            return "Finished";
        } else if(start.before(currTime)) {
            return "Ongoing";
        } else if(start.after(currTime)) {
            return "New";
        }
        return "New";
    }

    public void setProjectRating(String r) {
        this.projectRating = r;
    }

    public String getProjectRating() {
        return projectRating;
    }

    public String getProjectStatusChange() {
        return projectStatusChange;
    }

    public void setProjectStatusChange(String projectStatusChange) {
        this.projectStatusChange = projectStatusChange;
    }
}
