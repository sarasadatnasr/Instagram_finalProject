package sbu.cs.group4.backEnd.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import sbu.cs.group4.backEnd.database.SqlHandler;
import sbu.cs.group4.connectors.dataTransfer.DataTransferProcessor;
import sbu.cs.group4.connectors.elements.*;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class ClientHandler implements Runnable
{
    //the active server
    private Server server;

    private SqlHandler sqlHandler;
    private Gson gson;
    private DataTransferProcessor dtp;

    //incoming and outgoing json messages
    private JsonObject jsonInput;
    private JsonObject jsonOutput;

    //incoming and outgoing file messages
    private JsonObject fileInput;
    private JsonObject fileOutput;

    //media paths
    private final String PROFILE_PATH = "src\\main\\java\\sbu\\cs\\group4\\backEnd\\media\\profiles\\";
    private final String POST_PATH = "src\\main\\java\\sbu\\cs\\group4\\backEnd\\media\\posts\\";
    private final String STORY_PATH = "src\\main\\java\\sbu\\cs\\group4\\backEnd\\media\\stories\\";

    //the active user
    private User activeUser;

    //the user is online or not
    private boolean quit;

    //constructor

    public ClientHandler(Socket socket, Server server) throws IOException
    {
        this.server = server;

        sqlHandler = server.getSqlHandler();
        gson = new GsonBuilder().serializeNulls().create();
        dtp = new DataTransferProcessor(socket);

        jsonOutput = null;
        jsonInput = null;

        fileInput = null;
        fileOutput = null;

        activeUser = null;

        quit = false;
    }

    public String getUsername()
    {
        return activeUser.getUsername();
    }

    public DataTransferProcessor getDtp()
    {
        return dtp;
    }

    public void setFileInput(JsonObject fileInput)
    {
        this.fileInput = fileInput;
    }

    public void setJsonInput(JsonObject jsonInput)
    {
        this.jsonInput = jsonInput;
    }

    public void setQuit(boolean quit)
    {
        this.quit = quit;
    }

    @Override
    public void run()
    {
        try
        {
            while (!quit)
            {
                if (jsonInput != null)
                {
                    switch (jsonInput.get("request").getAsString().trim().toLowerCase())
                    {
                        case "getFile":
                            getFile();
                            break;

                        case "logIn":
                            logIn();
                            break;

                        case "signUp":
                            signUp();
                            break;


                        case "timeline":
                            timeline();
                            break;

                        case "getUser":
                            getUser();
                            break;

                        case "updateUser":
                            updateUser();
                            break;

                        case "post":
                            post();
                            break;

                        case "deletePost":
                            deletePost();
                            break;


                        case "story":
                            story();
                            break;

                        case "deleteStory":
                            deleteStory();
                            break;


                        case "sendMessage":
                            sendMessage();
                            break;

                        case "deleteMessage":
                            deleteMessage();
                            break;


                        case "comment":
                            comment();
                            break;

                        case "deleteComment":
                            deleteComment();
                            break;


                        case "like":
                            like();
                            break;

                        case "unlike":
                            unlike();
                            break;


                        case "follow":
                            follow();
                            break;

                        case "unfollow":
                            unfollow();
                            break;


                        case "deleteUser":
                            deleteUser();
                            break;

                        case "quit":
                            quit();
                            break;
                    }
                }
            }

        }

        catch (SQLException e)
        {
            SQLException();
            e.printStackTrace();
            dtp.sendJson(jsonOutput);
        }
        catch (IOException e)
        {
            IOException();
            e.printStackTrace();
            dtp.sendJson(jsonOutput);
        }
    }


    private void getFile() throws IOException
    {
        //initialize the fileOutput
        fileOutput = new JsonObject();

        //get the filePath

        String filePath = null;

        switch (jsonInput.get("fileType").getAsString())
        {
            case "profile":
                filePath = PROFILE_PATH + jsonInput.get("username").getAsString();
                break;

            case "post":
                filePath = POST_PATH + jsonInput.get("postID").getAsInt();
                break;

            case "story":
                filePath = STORY_PATH + jsonInput.get("storyID").getAsInt();
                break;
        }

        if (filePath != null)
        {
            //fill the fileOutput
            fileOutput.addProperty("result", true);
            fileOutput.addProperty("file", dtp.encodeFile(new File(filePath)));
        }

        else
        {
            //fill the fileOutput
            fileOutput.addProperty("result", false);
        }

        //send the fileOutput to the client
        dtp.sendJson(fileOutput);
    }


    private void logIn() throws SQLException, IOException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the user information
        String providedUsername = jsonInput.get("username").getAsString();
        String providedPassword = jsonInput.get("password").getAsString();

        //get the user from the database
        User user = sqlHandler.getUser(providedUsername, providedPassword);

        //user not found
        if (user == null)
        {
            //fill the jsonOutput
            jsonOutput.addProperty("result", false);
            jsonOutput.addProperty("error",
                    "wrong username or password.");
        }

        //user found
        else
        {
            //initialize the activeUser
            activeUser = user;

            //fill the jsonOutput
            addUserToOutput(activeUser);
        }

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);
    }

    private void signUp() throws SQLException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the user information
        String username = jsonInput.get("username").getAsString();
        String password = jsonInput.get("password").getAsString();
        String fullName = jsonInput.get("fullName").getAsString();
        String email = jsonInput.get("email").getAsString();

        //the username is taken
        if (sqlHandler.userExists(username))
        {
            //fill the jsonOutput
            jsonOutput.addProperty("result", false);
            jsonOutput.addProperty("error",
                    "the username " + username + " is already taken.");
        }

        //the username is available
        else
        {
            //initialize the activeUser
            activeUser = new User(username, password, fullName, email, new Date());

            //add the activeUser to the database
            sqlHandler.addUser(activeUser);

            //fill the jsonOutput
            addUserToOutput(activeUser);
        }

        //send the jsonOutput to the database
        dtp.sendJson(jsonOutput);
    }

    private void timeline() throws SQLException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //initialize the posts and stories of the timeLine
        ArrayList<Post> followingsPosts = new ArrayList<>();
        ArrayList<Story> followingsStories = new ArrayList<>();

        //fill the two lists
        for (String following : activeUser.getFollowings())
        {
            ArrayList<Post> postList;
            ArrayList<Story> storyList;

            postList = sqlHandler.getAllPosts(following);
            storyList = sqlHandler.getAllStories(following);

            if (!postList.isEmpty())
            {
                followingsPosts.addAll(postList);
            }

            if (!storyList.isEmpty())
            {
                followingsStories.addAll(storyList);
            }
        }

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);
        jsonOutput.addProperty("followingsPosts", gson.toJson(followingsPosts));
        jsonOutput.addProperty("followingsStories", gson.toJson(followingsStories));

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);
    }

    private void getUser() throws SQLException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the user information
        String username = jsonInput.get("username").getAsString();

        //get the user from the database
        User user = sqlHandler.getUser(username);

        //user not found
        if (user == null)
        {
            //fill the jsonOutput
            jsonOutput.addProperty("result", false);
            jsonOutput.addProperty("error", "user not found!");
        }

        //user found
        else
        {
            //fill the jsonOutput
            addUserToOutput(user);
        }

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);
    }

    private void updateUser() throws SQLException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the user's updated information
        String email = jsonInput.get("email").getAsString();
        String password = jsonInput.get("password").getAsString();
        String fullName = jsonInput.get("fullName").getAsString();
        String bio = jsonInput.get("bio").getAsString();

        //update the activeUser
        activeUser.setEmail(email);
        activeUser.setPassword(password);
        activeUser.setBio(bio);
        activeUser.setFullName(fullName);

        //update the activeUser in the database
        sqlHandler.updateUser(activeUser);

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);

        //send the jsonOutput to the database
        dtp.sendJson(jsonOutput);
    }


    private void post() throws SQLException, IOException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the post's information
        String poster = activeUser.getUsername();
        String caption = jsonInput.get("caption").getAsString();
        Date postDate = new Date();

        //make a new post instance
        Post post = new Post(poster, caption, postDate);

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);

        //send the result of the text operation
        dtp.sendJson(jsonOutput);

        //wait for the file of the post
        while (fileInput == null)
        {
        }

        //save the file of the post
        dtp.decodeFile(fileInput.get("file").getAsString(), POST_PATH + "temp");

        //add the post to the database
        int postID = sqlHandler.addPost(post);

        //rename the file of the post
        new File(POST_PATH + "temp").renameTo(new File(POST_PATH + postID));

        //complete the post instance
        post.setPostID(postID);

        //add the post to the activeUser
        activeUser.addPost(post);

        //set the fileInput to null
        fileInput = null;

        //fill the jsonOutput
        jsonOutput.addProperty("post", gson.toJson(post));

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);
    }

    public void deletePost() throws SQLException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the postId
        int postID = jsonInput.get("postID").getAsInt();

        //delete the post from the activeUser's posts
        activeUser.deletePost(postID);

        //delete the post from the database
        sqlHandler.deletePost(postID);

        //delete the file of the post
        new File(POST_PATH + postID).delete();

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);
    }


    private void like() throws SQLException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //initialize the jsonNotification
        JsonObject jsonNotification = new JsonObject();

        //get the like's information
        int postID = jsonInput.get("postID").getAsInt();
        String poster = jsonInput.get("poster").getAsString();
        String liker = activeUser.getUsername();

        //add the like to the database
        sqlHandler.addLike(postID, liker);

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);

        //fill the jsonNotification
        jsonNotification.addProperty("notification", "like");
        jsonNotification.addProperty("notificationText",
                liker + " has just liked one of your posts!");

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);

        //send a notification to the poster
        server.sendNotification(poster, jsonNotification);
    }

    public void unlike() throws SQLException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the unlike's information
        int postID = jsonInput.get("postID").getAsInt();
        String unliker = activeUser.getUsername();

        //delete the like from the database
        sqlHandler.unlike(postID, unliker);

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);
    }


    public void comment() throws SQLException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the comment's information
        int postID = jsonInput.get("postID").getAsInt();
        String commentText = jsonInput.get("commentText").getAsString();
        String commenter = activeUser.getUsername();

        //make a new comment instance
        Comment comment = new Comment(postID, commenter, commentText, new Date());

        //add the comment to the database
        int commentID = sqlHandler.addComment(comment);

        //complete the comment instance
        comment.setID(commentID);

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);
        jsonOutput.addProperty("comment", gson.toJson(comment));

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);
    }

    private void deleteComment() throws SQLException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the commentId
        int commentID = jsonInput.get("commentID").getAsInt();

        //delete the comment from the database
        sqlHandler.deleteComment(commentID);

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);
    }


    private void follow() throws SQLException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //initialize the notificationJson
        JsonObject notificationJson = new JsonObject();

        //get the follow's information
        String followed = jsonInput.get("followed").getAsString();
        String follower = activeUser.getUsername();

        //add the follow to the database
        sqlHandler.addFollow(follower, followed);

        //add the follow to the activeUser
        activeUser.follow(followed);

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);

        //fill the notificationJson
        notificationJson.addProperty("notification", "follow");
        notificationJson.addProperty("notificationText",
                follower + " has just started following you!");

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);

        //send a notification to the followed user
        server.sendNotification(followed, notificationJson);
    }

    public void unfollow() throws SQLException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the unfollowed username
        String unfollowed = jsonInput.get("unfollowed").getAsString();
        String unfollower = jsonInput.get("unfollower").getAsString();

        //delete the follow from the database
        sqlHandler.deleteFollow(unfollower, unfollowed);

        //delete the follow from the activeUser
        activeUser.unfollow(unfollowed);

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);
    }


    private void sendMessage() throws SQLException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //initialize the jsonNotification
        JsonObject jsonNotification = new JsonObject();

        //get the message's information
        String messageSender = activeUser.getUsername();
        String messageReceiver = jsonInput.get("messageReceiver").getAsString();
        String messageText = jsonInput.get("messageText").getAsString();
        Date messageDate = new Date();

        //make a new message instance
        Message message = new Message(messageSender, messageReceiver, messageText, messageDate);

        //add the message to the database
        int messageID = sqlHandler.addMessage(message);

        //complete the message instance
        message.setMessageID(messageID);

        //add the message to the activeUser
        activeUser.addMessage(message);

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);
        jsonOutput.addProperty("message", gson.toJson(message));

        //fill the jsonNotification
        jsonNotification.addProperty("notification", "message");
        jsonNotification.addProperty("notificationText",
                messageSender + " has just sent you a message!");

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);

        //send a notification to the messageReceiver
        server.sendNotification(messageReceiver, jsonNotification);
    }

    public void deleteMessage() throws SQLException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the messageId
        int messageID = jsonInput.get("messageID").getAsInt();

        //delete the message from the database
        sqlHandler.deleteMessage(messageID);

        //delete the message from the activeUser's messages
        activeUser.deleteMessage(messageID);

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);
    }


    public void story() throws SQLException, IOException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the story's information
        String username = activeUser.getUsername();
        Date storyDate = new Date();

        //make a new story instance
        Story story = new Story(username, storyDate);

        //add the story to the database
        int storyID = sqlHandler.addStory(story);

        //complete the story instance
        story.setStoryID(storyID);

        //add the story to the activeUser's stories list
        activeUser.addStory(story);

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);
        jsonOutput.addProperty("story", gson.toJson(story));

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);

        //wait for the file of the story
        while (fileInput == null)
        {
        }

        //save the file of the story
        dtp.decodeFile(fileInput.get("file").getAsString(), STORY_PATH + story.getStoryID());

        fileInput = null;
    }

    public void deleteStory() throws SQLException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the story id
        int storyID = jsonInput.get("storyID").getAsInt();

        //delete the story from the database
        sqlHandler.deleteStory(storyID);

        //delete the story from the activeUser
        activeUser.deleteStory(storyID);

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);
    }


    private void deleteUser() throws SQLException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the username of the deleted user
        String username = activeUser.getUsername();

        //delete the user from the database
        sqlHandler.deleteUser(username);

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);

        //send the jsonOutput to the client
        dtp.sendJson(jsonOutput);
    }

    private void quit()
    {
        dtp.close();
        server.removeClient(this);
        quit = true;
    }


    private void IOException()
    {
        jsonOutput = new JsonObject();
        jsonOutput.addProperty("result", false);
        jsonOutput.addProperty("error", "IOException");
    }

    private void SQLException()
    {
        jsonOutput = new JsonObject();
        jsonOutput.addProperty("result", false);
        jsonOutput.addProperty("error", "SQLException");
    }

    private void addUserToOutput(User user)
    {
        String userJson = gson.toJson(user);
        jsonOutput.addProperty("result", true);
        jsonOutput.addProperty("user", userJson);
    }
}