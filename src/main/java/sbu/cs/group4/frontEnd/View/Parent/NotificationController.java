package sbu.cs.group4.frontEnd.View.Parent;

import com.google.gson.JsonObject;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sbu.cs.group4.frontEnd.View.Parent.ControllerClass;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NotificationController extends ControllerClass implements Initializable
{
    @FXML
    protected ImageView NotificationSign;


    @FXML
    void homeButton(ActionEvent event) throws IOException
    {
        switchScene(event, "HomeTab");
    }

    @FXML
    void searchButton(ActionEvent event) throws IOException
    {
        switchScene(event, "SearchTab");
    }

    @FXML
    void addPostButton(ActionEvent event) throws IOException
    {
        switchScene(event, "AddPostTab");
    }

    @FXML
    void activityButton(ActionEvent event) throws IOException
    {
        switchScene(event, "ActivityTab");
    }

    @FXML
    void ProfileButton(ActionEvent event) throws IOException
    {
        switchScene(event, "ProfileTab(Posts)");
    }

    public void showNotificationSign()
    {
        //a notification is available
        if(!notifications.isEmpty())
        {
            JsonObject lastNotification = notifications.get(notifications.size() - 1);
            String lastNotificationType = lastNotification.get("notification").getAsString();

            switch (lastNotificationType)
            {
                case "message":
                    NotificationSign.setImage(new Image("Image/haveDirect.PNG"));

                case "follow":
                    NotificationSign.setImage(new Image("Image/haveFollower.PNG"));

                case "like":
                    NotificationSign.setImage(new Image("Image/haveLike.PNG"));

                case "comment":
                    NotificationSign.setImage(new Image("Image/commentNotification.PNG"));
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        showNotificationSign();
    }
}
