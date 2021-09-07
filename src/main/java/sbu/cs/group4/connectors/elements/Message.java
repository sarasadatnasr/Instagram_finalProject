package sbu.cs.group4.connectors.elements;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Message implements Comparable<Message>, Serializable
{
    private int messageID;

    private String messageSender;
    private String messageReceiver;
    private String messageText;
    private Date messageDate;

    public Message(int messageID, String messageSender, String messageReceiver, String messageText, Date messageDate)
    {
        this.messageID = messageID;
        this.messageSender = messageSender;
        this.messageReceiver = messageReceiver;
        this.messageText = messageText;
        this.messageDate = messageDate;
    }

    public Message(String messageSender, String messageReceiver, String messageText, Date messageDate)
    {
        this.messageSender = messageSender;
        this.messageReceiver = messageReceiver;
        this.messageText = messageText;
        this.messageDate = messageDate;
    }

    public static Message messageParser(ResultSet messageResult) throws SQLException
    {
        int messageID = messageResult.getInt("messageID");

        String messageSender = messageResult.getString("messageSender");
        String messageReceiver = messageResult.getString("messageReceiver");
        String messageText = messageResult.getString("messageText");
        Date messageDate = new Date(messageResult.getLong("messageDate"));

        return new Message(messageID, messageSender, messageReceiver, messageText, messageDate);
    }

    public boolean isEquals(int messageID)
    {
        return this.messageID == messageID;
    }

    //setter

    public void setMessageID(int messageID)
    {
        this.messageID = messageID;
    }

    public void setMessageSender(String messageSender)
    {
        this.messageSender = messageSender;
    }

    public void setMessageReceiver(String messageReceiver)
    {
        this.messageReceiver = messageReceiver;
    }

    public void setMessageText(String messageText)
    {
        this.messageText = messageText;
    }

    public void setMessageDate(Date messageDate)
    {
        this.messageDate = messageDate;
    }

    //getter

    public int getMessageID()
    {
        return messageID;
    }

    public String getMessageSender()
    {
        return messageSender;
    }

    public String getMessageReceiver()
    {
        return messageReceiver;
    }

    public String getMessageText()
    {
        return messageText;
    }

    public Date getMessageDate()
    {
        return messageDate;
    }

    @Override
    public int compareTo(Message o)
    {
        return this.messageDate.compareTo(o.getMessageDate());
    }
}
