package models;

import play.db.DB;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Dbtry {
    Connection conn = null;

    public Dbtry() {
        Statement stmt = null;
        String url = null;
        try {
            conn = DB.getConnection();
        } catch (Exception e) {
            System.out.println("Error while connecting to database");
            e.printStackTrace();
        }
    }

    // function 1 - test Passed
    public int validateLogin(String uname, String upasswd, int utype) throws SQLException {
        String sql = null;
        PreparedStatement ps = null;
        sql = "SELECT * FROM User where uname='" + uname + "' AND upasswd='" + upasswd + "' AND utype=" + utype;
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int result = rs.getInt(1);
            ps.close();
            return result;
        }
        return -1;

    }

    // function 2 - test Passed
    public void insertNewUser(User u) throws SQLException {
        String sql = null;
        PreparedStatement ps = null;
        sql = "INSERT INTO User (uname, upasswd, uemail, utype, uques) VALUES (?, ?, ?, ?, ?)";
        ps = conn.prepareStatement(sql);
        ps.setString(1, u.getUsername());
        ps.setString(2, u.getUserpasswd());
        ps.setString(3, u.getUserMail());
        ps.setInt(4, u.getUserType());
        ps.setString(5, u.getUserQuestion());
        ps.executeUpdate();
        ps.close();
    }

    // function 3 - insertProfile - test Passed
    public void insertNewProfile(Profile p) throws SQLException {
        String sql = null;
        PreparedStatement ps = null;
        sql = "SELECT * FROM Profile WHERE pid=?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, p.getID());
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            sql = "INSERT INTO Profile (pid, pcred, parea, ppubl, pserv) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, p.getID());
            ps.setString(2, p.getProfileCredential());
            ps.setString(3, p.getProfileArea());
            ps.setString(4, p.getProfilePublication());
            ps.setString(5, p.getProfileService());
            ps.executeUpdate();
        }
        ps.close();
    }

    // function 3 - insert project - test Passed
    public void insertNewProject(Project p) throws SQLException {
        String sql = null;
        PreparedStatement ps = null;
        sql = "INSERT INTO Project (pname, pdesc, preq, pprice, pstart, pend, prating, powner, pstatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        ps = conn.prepareStatement(sql);
        ps.setString(1, p.getProjectName());
        ps.setString(2, p.getProjectDesc());
        ps.setString(3, p.getProjectReq());
        ps.setString(4, p.getProjectPrice());
        ps.setString(5, p.getProjectTimeStart());
        ps.setString(6, p.getProjectTimeEnd());
        ps.setString(7, "0");
        ps.setString(8, p.getOwnerID());
        ps.setString(9, p.getProjectStatusChange());
        ps.executeUpdate();
        ps.close();
    }


    // function 4 - query project by user
    public ArrayList<Project> queryProject(String uid, Project p) throws SQLException {
        String sql = null;
        PreparedStatement ps = null;
        // History information
        sql = "INSERT INTO History (uid, pname) VALUES (?, ?)";
        ps = conn.prepareStatement(sql);
        ps.setString(1, uid);
        ps.setString(2, p.getProjectName());
        ps.executeUpdate();
        sql = "SELECT * FROM Project WHERE pname LIKE '%" + p.getProjectName() + "%'";
        if (p.getProjectDesc().length() > 0)
            sql += " AND pdesc LIKE '%" + p.getProjectDesc() + "%'";
        if (p.getProjectReq().length() > 0)
            sql += " AND preq LIKE '%" + p.getProjectReq() + "%'";
        // Time frame NOT provided now
        if (p.getProjectTimeStart().length() > 0) {
            sql += " AND pstart>='" + p.getProjectTimeStart() + "'";
        }
        if (p.getProjectTimeEnd().length() > 0) {
            sql += " AND pend<='" + p.getProjectTimeEnd() + "'";
        }
        if (p.getProjectPrice().length() > 0)
            sql += " AND pprice LIKE '%" + p.getProjectPrice() + "%'";
        ps = null;
        ps = conn.prepareStatement(sql);
        System.out.println(sql);
        ResultSet rs = ps.executeQuery();
        ArrayList<Project>ans = new ArrayList<Project>();
        while (rs.next()) {
            Project p1 = new Project();
            p1.setProjectName(rs.getString(2));
            p1.setProjectDesc(rs.getString(3));
            p1.setProjectReq(rs.getString(4));
            p1.setProjectPrice(rs.getString(5));
            p1.setProjectTimeStart(rs.getString(6));
            p1.setProjectTimeEnd(rs.getString(7));
            p1.setProjectRating(rs.getString(8));
            p1.setOwnerID(rs.getString(9));
            p1.setProjectStatusChange(rs.getString(10));
            ans.add(p1);
        }
        System.out.println(ans.size());
        ps.close();
        return ans;
    }

    // function 5 - show all projects
    public ArrayList<Project> showProject() throws SQLException {
        String sql = null;
        ArrayList<Project> ans = new ArrayList<Project>();
        PreparedStatement ps = null;
        sql = "SELECT * FROM Project";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Project p = new Project();
            p.setProjectName(rs.getString(2));
            p.setProjectDesc(rs.getString(3));
            p.setProjectReq(rs.getString(4));
            p.setProjectPrice(rs.getString(5));
            p.setProjectTimeStart(rs.getString(6));
            p.setProjectTimeEnd(rs.getString(7));
            p.setProjectRating(rs.getString(8));
            p.setOwnerID(rs.getString(9));
            p.setProjectStatusChange(rs.getString(10));
            ans.add(p);
        }
        ps.close();
        return ans;
    }

    // function 6-1 - show all providers based on popularity
    public ArrayList<String> showPopularity() throws SQLException {
        String sql = null;
        ArrayList<String> ans = new ArrayList<String>();
        PreparedStatement ps = null;
        sql = "SELECT powner, COUNT(*) FROM Project GROUP BY powner ORDER BY COUNT(*) DESC;";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            sql = "SELECT uname FROM User WHERE uid=" + rs.getString(1);
            ps = conn.prepareStatement(sql);
            ResultSet rs2 = ps.executeQuery();
            while (rs2.next()) {
                String p = rs.getString(1) + "," + rs2.getString(1) + "," + rs.getString(2);
                ans.add(p);
            }
        }
        ps.close();
        return ans;
    }

    // function 6-2 - show all providers based on rating
    public ArrayList<String> showRating() throws SQLException {
        String sql = null;
        ArrayList<String> ans = new ArrayList<String>();
        PreparedStatement ps = null;
        sql = "SELECT powner, SUM(prating) FROM Project GROUP BY powner ORDER BY SUM(prating) DESC;";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            sql = "SELECT uname FROM User WHERE uid=" + rs.getString(1);
            ps = conn.prepareStatement(sql);
            ResultSet rs2 = ps.executeQuery();
            while (rs2.next()) {
                String p = rs.getString(1) + "," + rs2.getString(1) + "," + rs.getString(2);
                ans.add(p);
            }
        }
        ps.close();
        return ans;
    }

    // function 6-3 - show all providers based on expertise
    public ArrayList<String> showExpertise(String match1) throws SQLException {
        String sql = null;
        ArrayList<String> ans = new ArrayList<String>();
        PreparedStatement ps = null;
        sql = "SELECT powner FROM Project WHERE preq='" + match1 + "'";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            sql = "SELECT uname FROM User WHERE uid=" + rs.getString(1);
            ps = conn.prepareStatement(sql);
            ResultSet rs2 = ps.executeQuery();
            while (rs2.next()) {
                String p = rs.getString(1) + "," + rs2.getString(1);
                ans.add(p);
            }
        }
        ps.close();
        return ans;
    }

    // function 6-4 - show all providers based on keyword
    public ArrayList<String> showKeyWord(String match1) throws SQLException {
        String sql = null;
        ArrayList<String> ans = new ArrayList<String>();
        PreparedStatement ps = null;
        sql = "SELECT pid FROM Profile WHERE pserv='" + match1 + "'";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            sql = "SELECT uname FROM User WHERE uid=" + rs.getString(1);
            ps = conn.prepareStatement(sql);
            ResultSet rs2 = ps.executeQuery();
            while (rs2.next()) {
                String p = rs.getString(1) + "," + rs2.getString(1);
                ans.add(p);
            }
        }
        ps.close();
        return ans;
    }

    // function 7 - show provider profile
    public Profile showProfile(String uid) throws Exception {
        String sql = null;
        ArrayList<String> ans = new ArrayList<String>();
        PreparedStatement ps = null;
        sql = "SELECT * FROM Profile WHERE pid='" + uid + "'";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        Profile p = new Profile();

        while (rs.next()) {
            p.setID(uid);
            p.setProfileCredential(rs.getString(2));
            p.setProfileArea(rs.getString(3));
            p.setProfilePublication(rs.getString(4));
            p.setProfileService(rs.getString(5));
            
            System.out.println(uid);

            // @uid: provider/profile id
            // Search OnGoing Project
            // Search Finish Project
            sql = "SELECT pstart, pend, pname, pstatus FROM Project WHERE powner='" + uid + "'";
            ps = conn.prepareStatement(sql);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                String startTime = res.getString(1);
                String endTime = res.getString(2);
                String projName = res.getString(3);
                String projStat = res.getString(4);
                Project tempProj = new Project();
                tempProj.setProjectTimeStart(startTime);
                tempProj.setProjectTimeEnd(endTime);
                tempProj.setProjectStatusChange(projStat);
                String status = tempProj.getProjectStatus();
                System.out.println(projName);
                if(status.equals("finished")) {
                    p.setFinishedProjs(projName);
                } else if(status.equals("on-going")) {
                    p.setOngoingProjs(projName);
                }
            }
        }
        ps.close();
        return p;
    }

    // function 8
    //  Service user Info page: Provide a page to show the profile of a service user:
    //  User id;
    //  Requested service keywords;
    //  Finished projects;
    //  Ongoing projects
    public ReturnUserInfo listUserInfo(String userId) throws SQLException, ParseException {
        String sql = null;
        PreparedStatement ps = null;
        String useID = userId;
        sql = "SELECT pname FROM History WHERE uid='" + useID + "'";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        HashSet<String> requestedProjs = new HashSet<>();
        while(rs.next()) {
            requestedProjs.add(rs.getString(1));
        }

        ReturnUserInfo ru = new ReturnUserInfo();

        ru.setUserID(useID);
        ArrayList<String> reqProjList = new ArrayList <String>();
        reqProjList.addAll(requestedProjs);
        ru.setRequestedServiceKeywords(reqProjList);

        ArrayList<String> finishedProjs = new ArrayList<>();
        ArrayList<String> ongoingProjs = new ArrayList<>();
        ArrayList<String> newProjs = new ArrayList<>();

        for(String pid : requestedProjs) {
            sql = "SELECT pstart, pend FROM Project WHERE pname='" + pid + "'";
            ps = conn.prepareStatement(sql);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                String startTime = res.getString(1);
                String endTime = res.getString(2);
                String projName = pid;
                Project tempProj = new Project();
                tempProj.setProjectTimeStart(startTime);
                tempProj.setProjectTimeEnd(endTime);
                String status = tempProj.getProjectStatus();
                if(status.equals("Finished")) {
                    finishedProjs.add(projName);
                } else if(status.equals("Ongoing")) {
                    ongoingProjs.add(projName);
                } else if(status.equals("Ongoing")) {
                    newProjs.add(projName);
                }
            }
        }
        ru.setFinishedProj(finishedProjs);
        ru.setOngoingProj(ongoingProjs);
        ru.setNewProj(newProjs);
        ps.close();
        return ru;
    }
    // return class for function 8
    public class ReturnUserInfo {
        String userID;
        List<String> requestedServiceKeywords;
        List<String> finishedProj;
        List<String> ongoingProj;
        List<String> newProj;

        public String getUserID() {
            return userID;
        }
        public void setUserID(String userID) {
            this.userID = userID;
        }
        public List<String> getRequestedServiceKeywords() {
            return requestedServiceKeywords;
        }
        public void setRequestedServiceKeywords(List<String> requestedServiceKeywords) {
            this.requestedServiceKeywords = requestedServiceKeywords;
        }
        public List<String> getFinishedProj() {
            return finishedProj;
        }
        public void setFinishedProj(List<String> finishedProj) {
            this.finishedProj = finishedProj;
        }
        public List<String> getOngoingProj() {
            return ongoingProj;
        }
        public void setOngoingProj(List<String> ongoingProj) {
            this.ongoingProj = ongoingProj;
        }
        public List<String> getNewProj() {
            return newProj;
        }
        public void setNewProj(List<String> newProj) {
            this.newProj = newProj;
        }
    }
    
    // function 9-1 - return projects belongs to a specific person
    public ArrayList<Project> showProject(String userId) throws SQLException {
        String sql = null;
        ArrayList<Project> ans = new ArrayList<Project>();
        PreparedStatement ps = null;
        sql = "SELECT * FROM Project WHERE powner='" + userId +"'";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Project p = new Project();
            p.setID(rs.getString(1));
            p.setProjectName(rs.getString(2));
            p.setProjectDesc(rs.getString(3));
            p.setProjectReq(rs.getString(4));
            p.setProjectPrice(rs.getString(5));
            p.setProjectTimeStart(rs.getString(6));
            p.setProjectTimeEnd(rs.getString(7));
            p.setProjectRating(rs.getString(8));
            p.setOwnerID(rs.getString(9));
            p.setProjectStatusChange(rs.getString(10));
            ans.add(p);
        }
        ps.close();
        return ans;
    }
    
    // function 9-2 - return projects belongs to a specific person
    public void changeProjectStatus(String projId, String status) throws SQLException {
        String sql = null;
        ArrayList<Project> ans = new ArrayList<Project>();
        PreparedStatement ps = null;
        sql = "UPDATE Project SET pstatus='" + status +"' WHERE pid='" + projId + "'";
        ps = conn.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
        return;
    }
    
    // function 10-1 - return the email address of a certain user
    public String getMailAddress(String userId) throws SQLException {
        String sql = null;
        sql = "SELECT uemail FROM User WHERE uid='" + userId + "';";
        PreparedStatement ps = null;
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }
    
    // function 10-2 - return the email address of a certain user
    public String getUserName(String userId) throws SQLException {
        String sql = null;
        sql = "SELECT uname FROM User WHERE uid='" + userId + "';";
        PreparedStatement ps = null;
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }


    // function 11-1: change project rating
    public void changeProjectRating(String projectName, String newRating) throws SQLException {
        String sql = null;
        PreparedStatement ps = null;
        sql = "UPDATE Project SET prating= '" + newRating + "' WHERE pname= '" + projectName + "'";
        ps = conn.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
    }

    // function 11-2: add comment to the provider, update or insert into TABLE comment
    public void addComment(String provider, String user, String comment) throws SQLException{
        String sql = null;
        PreparedStatement ps = null;
        sql = "SELECT cmt FROM Comment WHERE pid= '" + provider + "' AND uid = '" + user + "'";
        ps = conn.prepareStatement(sql);
        ResultSet res = ps.executeQuery();

            sql = "INSERT INTO Comment (pid, cmt, uid) VALUES (?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, provider);
            ps.setString(2, comment);
            ps.setString(3, user);
            ps.executeUpdate();
            ps.close();

    }
    // function 11-3 : show comments of the provider
    public List<String> showComment(String provider) throws SQLException {
        List<String> cmts = new ArrayList<>();
        String sql = null;
        PreparedStatement ps = null;
        sql = "SELECT cmt FROM Comment WHERE pid= '" + provider + "'";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            cmts.add(rs.getString(1));
        }
        ps.close();
        return cmts;
    }
    

    // function 13-1: return all users
    public ArrayList<User> getAllUsers() throws SQLException {
        String sql = null;
        sql = "SELECT * FROM User;";
        PreparedStatement ps = null;
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ArrayList<User> alluser = new ArrayList<User>();
        while (rs.next()) {
            User u = new User();
            u.setID(rs.getString(1));
            u.setUsername(rs.getString(2));
            u.setUserpasswd(rs.getString(3));
            u.setUserMail(rs.getString(4));
            u.setUserType(rs.getInt(5));
            u.setUserQuestion(rs.getString(6));
            if (u.getUserType() != 3) {
                alluser.add(u);
            }
        }
        return alluser;
    }
    
    // function 13-2: delete
    public boolean deleteUser(String u) throws SQLException {
        String sql = null;
        sql = "DELETE FROM User WHERE uid='" + u + "';";
        PreparedStatement ps = null;
        ps = conn.prepareStatement(sql);
        ps.executeUpdate();
        return true;
    }
    
    // function 14-1: insert new bugs
    public void addBug(Bug b) throws SQLException{
        String sql = null;
        PreparedStatement ps = null;
        sql = "INSERT INTO Bug (bprovider, bname, bdesc, bstatus) VALUES (?, ?, ?, ?)";
        ps = conn.prepareStatement(sql);
        ps.setString(1, b.getBugProvider());
        ps.setString(2, b.getBugName());
        ps.setString(3, b.getBugDescription());
        ps.setString(4, b.getBugStatus());
        ps.executeUpdate();
        ps.close(); 
    }
    
    // function 14-2 - return all bugs
    public ArrayList<Bug> showBugs() throws SQLException {
        String sql = null;
        ArrayList<Bug> ans = new ArrayList<>();
        PreparedStatement ps = null;
        sql = "SELECT * FROM Bug";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Bug b = new Bug();
            b.setBugProvider(rs.getString(2));
            b.setBugName(rs.getString(3));
            b.setBugDescription(rs.getString(4));
            b.setBugStatus(rs.getString(5));
            ans.add(b);
        }
        ps.close();
        return ans;
    }
    
    // API 2 
    public String apiGetTopExperts(String area) throws SQLException {
        String sql = null;
        String ans = "";
        PreparedStatement ps = null;
        sql = "SELECT * FROM Profile WHERE parea='" + area + "'";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String pid = rs.getString(1);
            sql = "SELECT * FROM User WHERE uid='" + pid + "'";
            ps = conn.prepareStatement(sql);
            ResultSet rs2 = ps.executeQuery();
            while (rs2.next()) {
                ans += rs2.getString(2);
            }
        }
        ps.close();
        return ans;
    }
    
    // API 3 
    public String apiGetGoingProjs(String area) throws SQLException {
        String sql = null;
        String ans = "";
        PreparedStatement ps = null;
        sql = "SELECT * FROM Profile WHERE parea='" + area + "'";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String pid = rs.getString(1);
            sql = "SELECT * FROM Project WHERE powner='" + pid + "' AND pstatus='on-going'";
            ps = conn.prepareStatement(sql);
            ResultSet rs2 = ps.executeQuery();
            while (rs2.next()) {
                ans += rs2.getString(2) + ",";
            }
        }
        ps.close();
        return ans;
    }

    // API 4 
    public String apiGetFinishProjs(String area) throws SQLException {
        String sql = null;
        String ans = "";
        PreparedStatement ps = null;
        sql = "SELECT * FROM Profile WHERE parea='" + area + "'";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String pid = rs.getString(1);
            sql = "SELECT * FROM Project WHERE powner='" + pid + "' AND pstatus='finished'";
            ps = conn.prepareStatement(sql);
            ResultSet rs2 = ps.executeQuery();
            while (rs2.next()) {
                ans += rs2.getString(2) + ",";
            }
        }
        ps.close();
        return ans;
    }    
    
    // API 7
    public String apiGetGoingProjs() throws SQLException {
        String sql = null;
        String ans = "";
        PreparedStatement ps = null;
        sql = "SELECT * FROM Project WHERE pstatus='on-going'";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ans += rs.getString(2) + ",";
        }
        ps.close();
        return ans;
    }
    
    // API 8
    public String apiGetFinishProjs() throws SQLException {
        String sql = null;
        String ans = "";
        PreparedStatement ps = null;
        sql = "SELECT * FROM Project WHERE pstatus='finished'";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ans += rs.getString(2) + ",";
        }
        ps.close();
        return ans;
    }

    // API 1 : Given a topic, provide the top 20 keywords;
//    public String apiGetTopKeywords() throws SQLException {
//        String sql = null;
//        String ans = "";
//        PreparedStatement ps = null;
//
//    }
    //    API 5: Given a service provider, provide all projects finished by them with rating if possible;
    public String apiGetAllProjFinishedWithRating(String providerName)throws SQLException {
        String sql = null;
        String uid = "";
        StringBuilder res = new StringBuilder();
        PreparedStatement ps = null;
        // get provider id (user id)
        sql = "SELECT uid FROM User WHERE uname= '" + providerName + "'";
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            uid = rs.getString(1);
            // get names of project from THE provider
            sql = "SELECT pname, prating FROM Project WHERE powner= '" + uid + "'";
            ps = conn.prepareStatement(sql);
            ResultSet rs2 = ps.executeQuery();
            while (rs2.next()) {
                res.append("Project Name:");
                res.append(rs2.getString(1));
                res.append("\nWith Rating:");
                res.append(rs2.getString(2) + "\n");

            }
        }
        ps.close();
        return res.toString();
    }


    //    API 6:  Given a topic, provide a list of 20 papers on this topic with ranking;
}

