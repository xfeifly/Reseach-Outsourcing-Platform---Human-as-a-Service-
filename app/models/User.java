package models;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 *
 * this class is user-provider mixed
 */
public class User {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    String userID;
    String userName;
    String userPasswd;
    int userType;
    /**
     * userType:
     * 1: user
     * 2: provider
     * 3: administrator
     * SteveRogers, ps: 123, is current administrator
     * 4: anonymous user
     */

    String userMail;
    String userQuestion;

    public void setID(String e) {
        this.userID = e;
    }

    public String getID() {
        return userID;
    }

    public void setUsername(String e) {
        this.userName = e;
    }

    public String getUsername() {
        return userName;
    }

    public void setUserpasswd(String e) {
        this.userPasswd = e;
    }

    public String getUserpasswd() {
        return userPasswd;
    }

    public void setUserType(int e) {
        this.userType = e;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserMail(String e) {
        this.userMail = e;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserQuestion(String e) {
        this.userQuestion = e;
    }

    public String getUserQuestion() {
        return userQuestion;
    }
}
