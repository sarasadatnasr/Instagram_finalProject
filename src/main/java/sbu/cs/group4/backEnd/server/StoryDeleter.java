package sbu.cs.group4.backEnd.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import sbu.cs.group4.backEnd.database.SqlHandler;
import sbu.cs.group4.connectors.File.FileHandler;
import sbu.cs.group4.connectors.elements.Story;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StoryDeleter implements Runnable
{
    private Server server;
    private SqlHandler sqlHandler;
    private Connection conn;
    private Gson gson;
    private FileHandler fileHandler;

    public StoryDeleter(Server server) throws SQLException {
        this.server = server;
        sqlHandler = server.getSqlHandler();
        conn = sqlHandler.getConnection();
        gson = new GsonBuilder().serializeNulls().create();
        fileHandler = new FileHandler();
    }

    @Override
    public void run() {
        System.out.println("Story deleter is ready.");

        try {
            while (true) {
                long time = System.currentTimeMillis();
                if (time % 3600000 == 0) //3600000 = The number of milliseconds of an hour
                {
                    ArrayList<Story> expiredStories = expiredStoryFinder(time);
                    deleteExpiredStory(expiredStories);
                    Thread.sleep(3500000);
                }
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        } catch (InterruptedException e) { }
    }

    public ArrayList<Story> expiredStoryFinder(long time) throws SQLException {
        ArrayList<Story> expiredStories = new ArrayList<>();

        Long milliSecondsDay = 86400000L;
        String exfCommand = "SELECT * FROM storiesTable WHERE ( ? - storyDate) >= ?;";
        PreparedStatement ps = conn.prepareStatement(exfCommand);
        ps.setLong(1, time);
        ps.setLong(2, milliSecondsDay);
        ResultSet esfResult = ps.executeQuery();

        while (esfResult.next())
        {
            Story tempStory = new Story(Story.storyParser(esfResult));
            expiredStories.add(tempStory);
        }

        return expiredStories;
    }

    private void deleteExpiredStory(ArrayList<Story> expiredStories)
    {
        for (Story expiredStory : expiredStories)
        {
            try {
                sqlHandler.deleteStory(expiredStory.getStoryID());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
