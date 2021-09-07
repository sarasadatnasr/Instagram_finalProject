package sbu.cs.group4.connectors.elements;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class User implements Comparable<User>, Serializable
{
    //obligatory fields
    private String username;
    private String password;
    private String fullName;
    private String email;
    private Date joinDate;

    //optional fields
    private String bio;
    private String location;

    //list of entities
    private ArrayList<String> followings;
    private ArrayList<String> followers;
    private ArrayList<Post> posts;
    private ArrayList<Message> messages;
    private ArrayList<Story> stories;

    //constructors

    public User(String username, String password, String fullName, String email, Date joinDate)
    {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.joinDate = joinDate;

        bio = "";
        location = "";

        followings = new ArrayList<>();
        followers = new ArrayList<>();
        posts = new ArrayList<>();
        messages = new ArrayList<>();
        stories = new ArrayList<>();
    }

    public User(String username, String password, String fullName, String email, Date joinDate, String bio,
                String location)
    {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.joinDate = joinDate;

        this.bio = bio;
        this.location = location;

        followings = new ArrayList<>();
        followers = new ArrayList<>();
        posts = new ArrayList<>();
        messages = new ArrayList<>();
        stories = new ArrayList<>();
    }

    //return a user object instantiated from a result set
    public static User parseUser(ResultSet userResult) throws SQLException
    {
        String username = userResult.getString("username");
        String password = userResult.getString("password");
        String fullName = userResult.getString("fullName");
        String email = userResult.getString("email");
        Date joinDate = new Date(userResult.getLong("joinDate"));

        String bio = userResult.getString("bio");
        String location = userResult.getString("location");

        return new User(username, password, fullName, email, joinDate, bio, location);
    }

    public void follow(String followedUsername)
    {
        followings.add(followedUsername);
    }

    public void unfollow(String unfollowUsername)
    {
        followings.remove(unfollowUsername);
    }

    public void addPost(Post post)
    {
        posts.add(post);
    }

    public void deletePost(int postID)
    {
        for (Post post : posts)
        {
            if (post.isEquals(postID))
            {
                posts.remove(post);
                return;
            }
        }
    }

    public void addMessage(Message message)
    {
        messages.add(message);
    }

    public void deleteMessage(int messageID)
    {
        for (Message message : messages)
        {
            if (message.isEquals(messageID))
            {
                messages.remove(message);
                return;
            }
        }
    }

    public void addStory(Story story)
    {
        stories.add(story);
    }

    public void deleteStory(int storyID)
    {
        for (Story story : stories)
        {
            if (story.isEquals(storyID))
            {
                stories.remove(story);
                return;
            }
        }
    }

    //setter

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setJoinDate(Date joinDate)
    {
        this.joinDate = joinDate;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setFollowings(ArrayList<String> followings)
    {
        this.followings = followings;
    }

    public void setFollowers(ArrayList<String> followers)
    {
        this.followers = followers;
    }

    public void setPosts(ArrayList<Post> posts)
    {
        this.posts = posts;
    }

    public void setMessages(ArrayList<Message> messages)
    {
        this.messages = messages;
    }

    public void setStories(ArrayList<Story> stories)
    {
        this.stories = stories;
    }

    //getters

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public Date getJoinDate()
    {
        return joinDate;
    }

    public String getBio()
    {
        return bio;
    }

    public String getFullName()
    {
        return fullName;
    }

    public String getLocation()
    {
        return location;
    }

    public String getEmail()
    {
        return email;
    }

    public ArrayList<String> getFollowings()
    {
        return followings;
    }

    public ArrayList<String> getFollowers()
    {
        return followers;
    }

    public ArrayList<Post> getPosts()
    {
        return posts;
    }

    public ArrayList<Message> getMessages()
    {
        return messages;
    }

    public ArrayList<Story> getStories()
    {
        return stories;
    }

    public ArrayList<String> getMessagesWithUser(String username)
    {
        Collections.sort(messages);

        ArrayList<String> messagesWithUser = new ArrayList<>();

        for(Message message : messages)
        {
            if(message.getMessageSender().equals(username)
            || message.getMessageSender().equals(username))
            {
                messagesWithUser.add(message.getMessageSender() +
                        " says:\n" + message.getMessageText());
            }
        }

        return messagesWithUser;
    }

    @Override
    public int compareTo(User o)
    {
        return this.joinDate.compareTo(o.getJoinDate());
    }
}
