package sbu.cs.group4.frontEnd.View.Children;

import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import sbu.cs.group4.connectors.elements.Message;
import sbu.cs.group4.connectors.elements.User;
import sbu.cs.group4.frontEnd.View.Parent.ControllerClass;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class WriteDirectController extends ControllerClass implements Initializable
{
    private static User messageReceiver;
    List<String> messageList = activeUser.getMessagesWithUser(messageReceiver.getUsername());

    @FXML
    private ListView<String> MessageList;
    @FXML
    private JFXTextField typeMessageField;
    @FXML
    private Circle profileImage;
    @FXML
    private Text UsernameField;

    public static void setMessageReceiver(User newMessageReceiver)
    {
        messageReceiver = newMessageReceiver;
    }

    @FXML
    void BackButton(ActionEvent event) throws IOException
    {
       switchScene(event, "FindDirecReceiver");
    }

    @FXML
    void SendButton(ActionEvent event) throws IOException
    {
        //initialize the output json
        jsonOutput = new JsonObject();

        //get the message information
        String messageText = typeMessageField.getText();

        //add the information to the output json
        jsonOutput.addProperty("request", "sendMessage");
        jsonOutput.addProperty("messageText", messageText);
        jsonOutput.addProperty("messageReceiver", messageReceiver.getUsername());

        //send the output json to the server
        dtp.sendJson(jsonOutput);

        //wait for a response
        while(jsonInput == null)
        {
        }

        //the operation was successful
        if(checkJsonResult())
        {
            //get the new message instance
            Message message = gson.fromJson(jsonInput.get("message").getAsString(), Message.class);

            //add the message to the activeUser's messages
            activeUser.addMessage(message);

            //add the message to the messageList
            messageList.add(activeUser + " says:\n" + messageText);

            //refresh
            switchScene(event, "DirectTab");
        }

        //the operation was not successful
        else
        {
            showError("the operation was not successful",3000);
        }

        jsonInput = null;
    }

    //set profile image and username
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //initialize the profile picture

        Image image = new Image(getClass().getResource("media/profiles/tempProfile.PNG").toExternalForm());
        profileImage.setFill(new ImagePattern(image));

        //set the usernameField

        UsernameField.setText(messageReceiver.getUsername());

        //show the messages
        MessageList.setItems(FXCollections.observableList(messageList));
    }
}
