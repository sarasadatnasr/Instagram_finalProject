package sbu.cs.group4.connectors.elements;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Comment implements Comparable<Comment>, Serializable {
    private int commentID;
    private int postID;

    private String commenter;
    private String commentText;
    private Date commentDate;

    public Comment(int commentID, int postID, String commenter, String commentText, Date commentDate)
    {
        this.commentID = commentID;
        this.postID = postID;
        this.commenter = commenter;
        this.commentText = commentText;
        this.commentDate = commentDate;
    }

    public Comment(int postID, String commenter, String commentText, Date commentDate)
    {
        this.postID = postID;
        this.commenter = commenter;
        this.commentText = commentText;
        this.commentDate = commentDate;
    }

    public static Comment commentParser(ResultSet commentResult) throws SQLException
    {
        int commentID = commentResult.getInt("commentID");
        int postID = commentResult.getInt("postID");

        String commenter = commentResult.getString("commenter");
        String commentText = commentResult.getString("commentText");
        Date commentDate = new Date(commentResult.getLong("commentDate"));

        return new Comment(commentID, postID, commenter, commentText, commentDate);
    }

    public boolean isEquals(int commentID)
    {
        return this.commentID == commentID;
    }

    //setter

    public void setID(int id) {
        this.commentID = id;
    }

    public void setPostID(int postID)
    {
        this.postID = postID;
    }

    public void setCommenter(String commenter)
    {
        this.commenter = commenter;
    }

    public void setCommentText(String text)
    {
        this.commentText = text;
    }

    public void setCommentDate(Date commentDate)
    {
        this.commentDate = commentDate;
    }

    //getter

    public int getCommentID()
    {
        return commentID;
    }

    public int getPostID()
    {
        return postID;
    }

    public String getCommenter()
    {
        return commenter;
    }

    public String getCommentText()
    {
        return commentText;
    }

    public Date getCommentDate()
    {
        return commentDate;
    }

    @Override
    public int compareTo(Comment o)
    {
        return this.commentDate.compareTo(o.getCommentDate());
    }
}
