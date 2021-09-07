package sbu.cs.group4.connectors.elements;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Story implements Comparable<Story>, Serializable {
    private int storyID;

    private String storyPoster;
    private Date storyDate;

    public Story(int storyID, String storyPoster, Date storyDate)
    {
        this.storyID = storyID;
        this.storyPoster = storyPoster;
        this.storyDate = storyDate;
    }

    public Story(String storyPoster, Date storyDate)
    {
        this.storyPoster = storyPoster;
        this.storyDate = storyDate;
    }


    public static Story storyParser(ResultSet storyResult) throws SQLException {
        int storyID = storyResult.getInt("storyID");

        String storyPoster = storyResult.getString("storyPoster");
        Date storyDate = new Date(storyResult.getLong("storyDate"));

        return new Story(storyID, storyPoster, storyDate);
    }

    public boolean isEquals(int storyID)
    {
        return this.storyID == storyID;
    }

    //setter

    public void setStoryID(int storyID) {
        this.storyID = storyID;
    }

    public void setStoryPoster(String storyPoster) {
        this.storyPoster = storyPoster;
    }

    public void setStoryDate(Date storyDate) {
        this.storyDate = storyDate;
    }

    //getter

    public int getStoryID() {
        return storyID;
    }

    public String getStoryPoster() {
        return storyPoster;
    }

    public Date getStoryDate() {
        return storyDate;
    }

    @Override
    public int compareTo(Story o) {
        return this.storyDate.compareTo(o.getStoryDate());
    }
}
