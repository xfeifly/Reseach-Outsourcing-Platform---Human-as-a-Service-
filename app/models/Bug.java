package models;

/**
 * Created by Gilbert1 on 11/30/16.
 */
public class Bug {
    int bid;
    String bugProvider;
    String bugName;
    String bugDescription;
    String bugStatus;

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getBugProvider() {
        return bugProvider;
    }

    public void setBugProvider(String bugProvider) {
        this.bugProvider = bugProvider;
    }

    public String getBugName() {
        return bugName;
    }

    public void setBugName(String bugName) {
        this.bugName = bugName;
    }

    public String getBugDescription() {
        return bugDescription;
    }

    public void setBugDescription(String bugDescription) {
        this.bugDescription = bugDescription;
    }

    public String getBugStatus() {
        return bugStatus;
    }

    public void setBugStatus(String bugStatus) {
        this.bugStatus = bugStatus;
    }
}
