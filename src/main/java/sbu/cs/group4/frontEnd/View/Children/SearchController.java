package sbu.cs.group4.frontEnd.View.Children;


import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import sbu.cs.group4.connectors.elements.User;
import sbu.cs.group4.frontEnd.View.Parent.ControllerClass;
import sbu.cs.group4.frontEnd.View.Parent.NotificationController;

import java.io.IOException;

public class SearchController extends NotificationController
{
    @FXML
    private JFXTextField SearchField;

    @FXML
    private Text resultText;

    @FXML
    void SearchButton(ActionEvent event) throws IOException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the information
        String username = SearchField.getText();

        //add the information to the output json
        jsonOutput.addProperty("request", "getUser");
        jsonOutput.addProperty("username", username);

        //send the output json
        dtp.sendJson(jsonOutput);

        //wait for a response
        while (jsonInput == null)
        {
        }

        //user found
        if (checkJsonResult())
        {
            //get the user object
            User user = gson.fromJson(jsonInput.get("user").getAsString(), User.class);

            //set the findProfileController's user
            FindProfileController.setUser(user);

            //go to the findProfileTab scene
            switchScene(event, "FindProfileTab");
        }

        //user not found
        else
        {
            resultText.setText("User not found!");
        }

        jsonInput = null;
    }


    public void AddPostButton(ActionEvent actionEvent) {
    }
}