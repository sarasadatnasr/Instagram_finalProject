package sbu.cs.group4.frontEnd.View.Children;

import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import sbu.cs.group4.frontEnd.View.Parent.NotificationController;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ActivityController extends NotificationController
{
    @FXML
    private ListView<String> listViewPanel;

    //show activities
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //a notification is available
        if (!notifications.isEmpty())
        {
            //initialize the activities list
            ArrayList<String> activities = new ArrayList<>();

            //fill the arrayList
            for (JsonObject notification : notifications)
            {
                activities.add(notification.get("notificationText").getAsString());
            }

            //show the activities
            listViewPanel.setItems(FXCollections.observableList(activities));
        }
    }
}

