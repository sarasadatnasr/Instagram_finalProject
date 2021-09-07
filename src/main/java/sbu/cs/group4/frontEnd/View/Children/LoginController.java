package sbu.cs.group4.frontEnd.View.Children;

import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import sbu.cs.group4.connectors.elements.User;
import sbu.cs.group4.frontEnd.View.Parent.ControllerClass;

import java.io.IOException;

public class LoginController extends ControllerClass
{
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private Text textField;


    @FXML
    void loginButton(ActionEvent event) throws IOException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get user information
        String username = usernameField.getText();
        String password = passwordField.getText();

        //add the information to the jsonOutput
        jsonOutput.addProperty("request", "logIn");
        jsonOutput.addProperty("username", username);
        jsonOutput.addProperty("password", password);

        //send the jsonOutput to server
        dtp.sendJson(jsonOutput);

        //wait for a response
        while(jsonInput == null)
        {
        }

        //the operation was successful
        if (checkJsonResult())
        {
            //initialize the activeUser object
            activeUser = gson.fromJson(jsonInput.get("user").getAsString(), User.class);

            //go to profile tab panel
            switchScene(event, "ProfileTab(Posts)");
        }

        //the operation was not successful
        else
        {
            //show the error to the user
            textField.setText("wrong username or password.");
        }

        jsonInput = null;
    }

    @FXML
    void signupClick(ActionEvent event) throws IOException
    {
        switchScene(event, "Signup");
    }
}



