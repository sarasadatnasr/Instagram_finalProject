package sbu.cs.group4.backEnd.database;

import sbu.cs.group4.connectors.elements.*;

import java.sql.*;
import java.util.ArrayList;

public class SqlHandler
{
    private final String url = "jdbc:mysql://127.0.0.1:3306/instagram";
    private final String DATABASE_USERNAME = "root";
    private final String DATABASE_PASSWORD = "root";
    private Connection conn;

    private final String DATABASE = "instagram";
    private final String USERS_TABLE = "usersTable";
    private final String POSTS_TABLE = "postsTable";
    private final String STORIES_TABLE = "storiesTable";
    private final String MESSAGES_TABLE = "messagesTable";
    private final String COMMENTS_TABLE = "commentsTable";
    private final String LIKES_TABLE = "likesTable";
    private final String FOLLOWS_TABLE = "followsTable";


    public void init() throws SQLException
    {
        conn = getConnection();
        initDatabase();
        initTables();
    }

    public Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(url, DATABASE_USERNAME, DATABASE_PASSWORD);
    }

    private void initDatabase() throws SQLException
    {
        String createCommand = "CREATE DATABASE IF NOT EXISTS " + DATABASE + ";";
        executeStatement(createCommand);

        String useCommand = "USE " + DATABASE + ";";
        executeStatement(useCommand);
    }

    private void initTables() throws SQLException
    {
        initUsersTable();
        initPostsTable();
        initCommentTable();
        initLikeTable();
        initFollowTable();
        initMessageTable();
        initStoryTable();
    }

    private void initUsersTable() throws SQLException
    {
        String command = "CREATE TABLE IF NOT EXISTS " + USERS_TABLE + "(\n" +

                "\tusername STRING UNIQUE NOT NULL,\n" +
                "\tpassword STRING NOT NULL,\n" +
                "\tfullName STRING NOT NULL,\n" +
                "\temail STRING NOT NULL,\n" +
                "\tjoinDate LONG NOT NULL, \n" +

                "\tbio STRING,\n" +
                "\tlocation STRING,\n" +

                "\n" +
                "\tPRIMARY KEY(username)\n" +
                ");";

        executeStatement(command);
    }

    private void initPostsTable() throws SQLException
    {
        String command = "CREATE TABLE IF NOT EXISTS " + POSTS_TABLE + "(\n" +

                "\tpostID INT AUTO_INCREMENT NOT NULL,\n" +

                "\tposter STRING NOT NULL,\n" +
                "\tcaption STRING NOT NULL,\n" +
                "\tpostDate LONG NOT NULL,\n" +

                "\n" +
                "\tPRIMARY KEY (postID),\n" +
                "\tFOREIGN KEY (poster) REFERENCES " + USERS_TABLE + " (username) ON DELETE CASCADE\n" +
                ");";

        executeStatement(command);
    }

    private void initCommentTable() throws SQLException
    {
        String command = "CREATE TABLE IF NOT EXISTS " + COMMENTS_TABLE + "(\n" +

                "\tcommentID INT AUTO_INCREMENT NOT NULL, \n" +
                "\tpostID INT NOT NULL,\n" +

                "\tcommenter STRING NOT NULL,\n" +
                "\tcommentText STRING NOT NULL,\n" +
                "\tcommentDate LONG NOT NULL,\n" +

                "\n" +
                "\tPRIMARY KEY (commentID),\n" +
                "\tFOREIGN KEY (postID) REFERENCES " + POSTS_TABLE + " (postID) ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY (commenter) REFERENCES " + USERS_TABLE + " (username) ON DELETE CASCADE\n" +
                ");";

        executeStatement(command);
    }

    private void initLikeTable() throws SQLException
    {
        String command = "CREATE TABLE IF NOT EXISTS " + LIKES_TABLE + "(\n" +

                "\tpostID INT NOT NULL,\n" +

                "\tliker STRING NOT NULL,\n" +

                "\n" +
                "\tFOREIGN KEY (postID) REFERENCES " + POSTS_TABLE + " (postID) ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY (liker) REFERENCES " + USERS_TABLE + " (username) ON DELETE CASCADE\n" +
                ");";

        executeStatement(command);
    }

    private void initFollowTable() throws SQLException
    {
        String command = "CREATE TABLE IF NOT EXISTS " + FOLLOWS_TABLE + "(\n" +

                "\tfollower STRING NOT NULL,\n" +
                "\tfollowed STRING NOT NULL,\n" +

                "\n" +
                "\tFOREIGN KEY (follower) REFERENCES " + USERS_TABLE + " (username) ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY (followed) REFERENCES " + USERS_TABLE + " (username) ON DELETE CASCADE\n" +
                ");";
        executeStatement(command);
    }

    private void initMessageTable() throws SQLException
    {
        String command = "CREATE TABLE IF NOT EXISTS " + MESSAGES_TABLE + "(\n" +

                "\tmessageID INT AUTO_INCREMENT NOT NULL,\n" +

                "\tmessageSender STRING NOT NULL,\n" +
                "\tmessageReceiver STRING NOT NULL,\n" +
                "\tmessageText STRING NOT NULL,\n" +
                "\tmessageDate LONG NOT NULL," +

                "\n" +
                "\tPRIMARY KEY (messageID),\n" +
                "\tFOREIGN KEY (messageSender) REFERENCES " + USERS_TABLE + " (username) ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY (messageReceiver) REFERENCES " + USERS_TABLE + " (username) ON DELETE CASCADE\n" +
                ");";

        executeStatement(command);
    }

    private void initStoryTable() throws SQLException
    {
        String command = "CREATE TABLE IF NOT EXISTS " + STORIES_TABLE + "(\n" +

                "\tstoryID INT AUTO_INCREMENT NOT NULL,\n" +

                "\tstoryPoster STRING NOT NULL,\n" +
                "\tstoryDate LONG NOT NULL,\n" +

                "\n" +
                "\tPRIMARY KEY (storyID),\n" +
                "\tFOREIGN KEY (storyPoster) REFERENCES " + USERS_TABLE + " (username) ON DELETE CASCADE\n" +
                ");";

        executeStatement(command);
    }

    private void executeStatement(String command) throws SQLException
    {
        Statement statement = conn.createStatement();
        statement.execute(command);
        statement.close();
    }

    public void close() throws SQLException
    {
        conn.close();
    }

    //user

    public boolean userExists(String username) throws SQLException
    {
        boolean result = false;

        String command = "SELECT username FROM " + USERS_TABLE + " WHERE username = ?;";

        PreparedStatement ps = conn.prepareStatement(command);
        ps.setString(1, username);

        ResultSet resultSet = ps.executeQuery();

        if (resultSet.next())
        {
            result = true;
        }

        ps.close();

        return result;
    }

    public User getUser(String username, String password) throws SQLException
    {
        User user = null;

        String userCommand = "SELECT * FROM " + USERS_TABLE + " WHERE username = ? AND password = ?;";

        PreparedStatement psUser = conn.prepareStatement(userCommand);
        psUser.setString(1, username);
        psUser.setString(2, password);

        ResultSet resultUser = psUser.executeQuery();

        if (resultUser.next())
        {
            user = User.parseUser(resultUser);

            user.setFollowings(getAllFollowings(username));
            user.setFollowers(getAllFollowers(username));
            user.setPosts(getAllPosts(username));
            user.setMessages(getAllMessages(username));
            user.setStories(getAllStories(username));
        }

        psUser.close();

        return user;
    }

    public User getUser(String username) throws SQLException
    {
        User user = null;

        String userCommand = "SELECT * FROM " + USERS_TABLE + " WHERE username = ?;";

        PreparedStatement psUser = conn.prepareStatement(userCommand);
        psUser.setString(1, username);

        ResultSet resultUser = psUser.executeQuery();

        if (resultUser.next())
        {
            user = User.parseUser(resultUser);

            user.setFollowings(getAllFollowings(username));
            user.setFollowers(getAllFollowers(username));
            user.setPosts(getAllPosts(username));
            user.setMessages(getAllMessages(username));
            user.setStories(getAllStories(username));
        }

        psUser.close();

        return user;
    }

    public void addUser(User user) throws SQLException
    {
        String command = "INSERT INTO " + USERS_TABLE +
                " (username, password, fullName, email, joinDate, bio, location) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";

        PreparedStatement ps = conn.prepareStatement(command);

        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getFullName());
        ps.setString(4, user.getEmail());
        ps.setLong(5, user.getJoinDate().getTime());
        ps.setString(6, user.getBio());
        ps.setString(7, user.getLocation());

        ps.executeUpdate();
        ps.close();
    }

    public void updateUser(User user) throws SQLException
    {
        String command = "UPDATE " + USERS_TABLE +
                " SET (password, fullName, email, bio) " +
                "VALUES (?, ?, ?, ?);";

        PreparedStatement ps = conn.prepareStatement(command);

        ps.setString(1, user.getPassword());
        ps.setString(2, user.getFullName());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getBio());

        ps.executeUpdate();
        ps.close();
    }

    public void deleteUser(String username) throws SQLException
    {
        String command = "DELETE FROM " + USERS_TABLE + " WHERE username = ?;";

        PreparedStatement ps = conn.prepareStatement(command);
        ps.setString(1, username);

        ps.executeUpdate();
        ps.close();
    }

    //post

    public int addPost(Post post) throws SQLException
    {
        int postID = -1;

        String postCommand = "INSERT INTO " + POSTS_TABLE +
                " (poster, caption, postDate) VALUES (?, ?, ?);";

        PreparedStatement ps = conn.prepareStatement(postCommand, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, post.getPoster());
        ps.setString(2, post.getCaption());
        ps.setLong(3, post.getPostDate().getTime());

        ps.executeUpdate();

        ResultSet postIDResult = ps.getGeneratedKeys();

        if (postIDResult.next())
        {
            postID = postIDResult.getInt(1);
        }

        ps.close();

        return postID;
    }

    public ArrayList<Post> getAllPosts(String poster) throws SQLException
    {
        ArrayList<Post> posts = new ArrayList<>();

        String allPostCommand = "SELECT * FROM " + POSTS_TABLE + " WHERE poster = ?;";

        PreparedStatement ps = conn.prepareStatement(allPostCommand);
        ps.setString(1, poster);

        ResultSet allPostResult = ps.executeQuery();

        while (allPostResult.next())
        {
            Post post = Post.postParser(allPostResult);

            post.setLikes(getAllLikes(post));
            post.setComments(getAllComments(post));

            posts.add(post);
        }

        ps.close();

        return posts;
    }

    public Post getPost(int postID) throws SQLException
    {
        String getPostCommand = "SELECT * FROM " + POSTS_TABLE + " WHERE postID = ?;";

        PreparedStatement ps = conn.prepareStatement(getPostCommand);
        ps.setInt(1, postID);

        ResultSet postResult = ps.executeQuery();

        Post post = null;

        if (postResult.next())
        {
            post = Post.postParser(postResult);

            post.setLikes(getAllLikes(post));
            post.setComments(getAllComments(post));
        }

        ps.close();

        return post;
    }

    public void deletePost(int postID) throws SQLException
    {
        String deletePostComment = "DELETE FROM " + POSTS_TABLE + " WHERE postID = ?;";

        PreparedStatement ps = conn.prepareStatement(deletePostComment);
        ps.setInt(1, postID);

        ps.executeUpdate();
        ps.close();
    }

    //like

    public void addLike(int postID, String liker) throws SQLException
    {
        String likeCommand = "INSERT INTO " + LIKES_TABLE +
                " (postID, liker) VALUES (?, ?);";

        PreparedStatement ps = conn.prepareStatement(likeCommand);
        ps.setInt(1, postID);
        ps.setString(2, liker);

        ps.executeUpdate();
        ps.close();
    }

    public void unlike(int postID, String unliker) throws SQLException
    {
        String unlikeCommand = "DELETE FROM " + LIKES_TABLE +
                " WHERE postID = ? AND liker = ?;";

        PreparedStatement ps = conn.prepareStatement(unlikeCommand);
        ps.setInt(1, postID);
        ps.setString(2, unliker);

        ps.executeUpdate();
        ps.close();
    }

    public ArrayList<String> getAllLikes(Post post) throws SQLException
    {
        ArrayList<String> likes = new ArrayList();

        String getLikeCommand = "SELECT liker FROM " + LIKES_TABLE + " WHERE postID = ?;";

        PreparedStatement ps = conn.prepareStatement(getLikeCommand);
        ps.setInt(1, post.getPostID());

        ResultSet getLikeResult = ps.executeQuery();

        while (getLikeResult.next())
        {
            likes.add(getLikeResult.getString("liker"));
        }

        ps.close();

        return likes;
    }

    //comment

    public int addComment(Comment comment) throws SQLException
    {
        int commentID = -1;

        String addCommentCommand = "INSERT INTO " + COMMENTS_TABLE +
                " (postID, commenter, commentText, commentDate)" +
                " VALUES (?, ?, ?, ?);";

        PreparedStatement ps = conn.prepareStatement(addCommentCommand, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, comment.getPostID());
        ps.setString(2, comment.getCommenter());
        ps.setString(3, comment.getCommentText());
        ps.setLong(4, comment.getCommentDate().getTime());

        ps.executeUpdate();

        ResultSet commentIDResult = ps.getGeneratedKeys();

        if (commentIDResult.next())
        {
            commentID = commentIDResult.getInt(1);
        }

        ps.close();

        return commentID;
    }

    public ArrayList<Comment> getAllComments(Post post) throws SQLException
    {
        ArrayList<Comment> comments = new ArrayList<>();

        String getCommentCommand = "SELECT * FROM " + COMMENTS_TABLE + " WHERE postID = ?;";

        PreparedStatement ps = conn.prepareStatement(getCommentCommand);
        ps.setInt(1, post.getPostID());

        ResultSet getCommentResult = ps.executeQuery();

        while (getCommentResult.next())
        {
            Comment comment = Comment.commentParser(getCommentResult);

            comments.add(comment);
        }

        ps.close();

        return comments;
    }

    public void deleteComment(int commentID) throws SQLException
    {
        String deleteCommentCommand = "DELETE FROM " + COMMENTS_TABLE + " WHERE commentID = ?;";

        PreparedStatement ps = conn.prepareStatement(deleteCommentCommand);
        ps.setInt(1, commentID);

        ps.executeUpdate();
        ps.close();
    }

    //follow

    public void addFollow(String follower, String followed) throws SQLException
    {
        String followCommand = "INSERT INTO " + FOLLOWS_TABLE +
                " (follower, followed)" +
                " VALUES (?, ?);";

        PreparedStatement ps = conn.prepareStatement(followCommand);
        ps.setString(1, follower);
        ps.setString(2, followed);

        ps.executeUpdate();
        ps.close();
    }

    public void deleteFollow(String follower, String followed) throws SQLException
    {
        String unFollowCommand = "DELETE FROM " + FOLLOWS_TABLE + " WHERE follower = ? AND followed = ?;";

        PreparedStatement ps = conn.prepareStatement(unFollowCommand);
        ps.setString(1, follower);
        ps.setString(2, followed);

        ps.executeUpdate();
        ps.close();
    }

    public ArrayList<String> getAllFollowings(String username) throws SQLException
    {
        ArrayList<String> followings = new ArrayList<>();

        String followingsCommand = "SELECT followed FROM " + FOLLOWS_TABLE + " WHERE follower = ?;";

        PreparedStatement psFollowings = conn.prepareStatement(followingsCommand);
        psFollowings.setString(1, username);

        ResultSet resultFollowings = psFollowings.executeQuery();

        while (resultFollowings.next())
        {
            followings.add(resultFollowings.getString("followed"));
        }

        psFollowings.close();

        return followings;
    }

    public ArrayList<String> getAllFollowers(String username) throws SQLException
    {
        ArrayList<String> followers = new ArrayList<>();

        String followersCommand = "SELECT follower FROM " + FOLLOWS_TABLE + " WHERE followed = ?;";

        PreparedStatement psFollowers = conn.prepareStatement(followersCommand);
        psFollowers.setString(1, username);

        ResultSet resultFollowers = psFollowers.executeQuery();

        while (resultFollowers.next())
        {
            followers.add(resultFollowers.getString("follower"));
        }

        psFollowers.close();

        return followers;
    }

    //message

    public int addMessage(Message message) throws SQLException
    {
        int messageID = -1;

        String messageCommand = "INSERT INTO " + MESSAGES_TABLE +
                " (messageSender, messageReceiver, messageText, MessageDate) VALUES (?, ?, ?, ?);";

        PreparedStatement ps = conn.prepareStatement(messageCommand, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, message.getMessageSender());
        ps.setString(2, message.getMessageReceiver());
        ps.setString(3, message.getMessageText());
        ps.setLong(4, message.getMessageDate().getTime());

        ps.executeUpdate();

        ResultSet messageIDResult = ps.getGeneratedKeys();

        if (messageIDResult.next())
        {
            messageID = messageIDResult.getInt(1);
        }

        ps.close();

        return messageID;
    }

    public ArrayList<Message> getAllMessages(String username) throws SQLException
    {
        ArrayList<Message> messages = new ArrayList<>();

        String getMessageCommand = "SELECT * FROM " + MESSAGES_TABLE + " WHERE " +
                "messageSender = ? OR messageReceiver = ?;";

        PreparedStatement ps = conn.prepareStatement(getMessageCommand);
        ps.setString(1, username);
        ps.setString(2, username);

        ResultSet getMessageResult = ps.executeQuery();

        while (getMessageResult.next())
        {
            Message message = Message.messageParser(getMessageResult);

            messages.add(message);
        }

        ps.close();

        return messages;
    }

    public void deleteMessage(int messageID) throws SQLException
    {
        String deleteMessageCommand = "DELETE FROM " + MESSAGES_TABLE + " WHERE messageID = ?;";

        PreparedStatement ps = conn.prepareStatement(deleteMessageCommand);
        ps.setInt(1, messageID);

        ps.executeUpdate();
        ps.close();
    }

    //story

    public int addStory(Story story) throws SQLException
    {
        int storyID = -1;

        String storyCommand = "INSERT INTO " + STORIES_TABLE +
                "(storyPoster, storyDate) VALUES (?, ?);";

        PreparedStatement ps = conn.prepareStatement(storyCommand, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, story.getStoryPoster());
        ps.setLong(2, story.getStoryDate().getTime());

        ps.executeUpdate();

        ResultSet storyIDResult = ps.getGeneratedKeys();

        if (storyIDResult.next())
        {
            storyID = storyIDResult.getInt(1);
        }

        ps.close();

        return storyID;
    }

    public ArrayList<Story> getAllStories(String storyPoster) throws SQLException
    {
        ArrayList<Story> stories = new ArrayList<>();

        String allStoryCommand = "SELECT * FROM " + STORIES_TABLE + " WHERE storyPoster = ?;";

        PreparedStatement ps = conn.prepareStatement(allStoryCommand);
        ps.setString(1, storyPoster);

        ResultSet allStoryResult = ps.executeQuery();

        while (allStoryResult.next())
        {
            Story story = Story.storyParser(allStoryResult);

            stories.add(story);
        }

        ps.close();

        return stories;
    }

    public void deleteStory(int storyID) throws SQLException
    {
        String deleteStoryCommand = "DELETE FROM " + STORIES_TABLE + " WHERE storyID = ?;";

        PreparedStatement ps = conn.prepareStatement(deleteStoryCommand);
        ps.setInt(1, storyID);

        ps.executeUpdate();
        ps.close();
    }
}