package sbu.cs.group4.connectors.elements;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Post implements Comparable<Post>, Serializable {
    private int postID;

    private String poster;
    private String caption;
    private Date postDate;

    private ArrayList<String> likes;
    private ArrayList<Comment> comments;


    public Post(int postID, String poster, String caption, Date postDate)
    {
        this.postID = postID;
        this.poster = poster;
        this.caption = caption;
        this.postDate = postDate;

        likes = new ArrayList<>();
        comments = new ArrayList<>();
    }

    public Post(String poster, String caption, Date postDate)
    {
        this.poster = poster;
        this.caption = caption;
        this.postDate = postDate;

        likes = new ArrayList<>();
        comments = new ArrayList<>();
    }


    public static Post postParser(ResultSet postResult) throws SQLException
    {
        int postID = postResult.getInt("postID");

        String poster = postResult.getString("poster");
        String caption = postResult.getString("caption");
        Date postDate = new Date(postResult.getLong("postDate"));

        return new Post(postID, poster, caption, postDate);
    }

    public boolean isEquals(int postID)
    {
        return this.postID == postID;
    }

    public void unLike(String unLiker)
    {
        likes.remove(unLiker);
    }

    //setter

    public void setPostID(int postID)
    {
        this.postID = postID;
    }

    public void setPoster(String poster)
    {
        this.poster = poster;
    }

    public void setCaption(String caption)
    {
        this.caption = caption;
    }

    public void setLikes(ArrayList<String> likes)
    {
        this.likes = likes;
    }

    public void setComments(ArrayList<Comment> comments)
    {
        this.comments = comments;
    }

    public void setPostDate(Date postDate)
    {
        this.postDate = postDate;
    }

    public boolean isThatLiked(String username){
        if(likes.contains(username)){
            return true;
        }
        else{
            return false;
        }
    }

    //getter

    public int getPostID()
    {
        return postID;
    }

    public String getPoster()
    {
        return poster;
    }

    public String getCaption()
    {
        return caption;
    }

    public ArrayList<String> getLikes()
    {
        return likes;
    }

    public ArrayList<Comment> getComments()
    {
        return comments;
    }

    public Date getPostDate()
    {
        return postDate;
    }

    @Override
    public int compareTo(Post o) {
        return this.postDate.compareTo(o.getPostDate());
    }
}
