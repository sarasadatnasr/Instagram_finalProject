package sbu.cs.group4.frontEnd.View.Children;

import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import sbu.cs.group4.frontEnd.View.Parent.ControllerClass;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditProfileController extends ControllerClass implements Initializable
{
    @FXML
    private Circle ProfileCircle;
    @FXML
    private TextField NameField;
    @FXML
    private TextField PasswordField;
    @FXML
    private TextField EmailField;
    @FXML
    private TextField BioField;


    @FXML
    void CancelButton(ActionEvent event) throws IOException
    {
        switchScene(event, "ProfileTab(Posts)");
    }

    @FXML
    void DoneButton(ActionEvent event)
    {
        //initialize jsonOutput
        jsonOutput = new JsonObject();

        //get the updated text information
        String fullName = NameField.getText();
        String password = PasswordField.getText();
        String email = EmailField.getText();
        String bio = BioField.getText();

        //fill the jsonOutput
        jsonOutput.addProperty("request", "updateUser");
        jsonOutput.addProperty("fullName", fullName);
        jsonOutput.addProperty("password", password);
        jsonOutput.addProperty("email", email);
        jsonOutput.addProperty("bio", bio);

        //send the jsonOutput
        dtp.sendJson(jsonOutput);

        //wait for a response
        while(jsonInput == null)
        {
        }

        //the operation was successful
        if(checkJsonResult())
        {
            //update the activeUser object
            activeUser.setBio(bio);
            activeUser.setFullName(fullName);
            activeUser.setPassword(password);
            activeUser.setLocation(email);
        }

        //the operation was not successful
        else
        {
            //!!!!!!!!!!!!!!!!!!!
            //show an error
        }

        jsonInput = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //initialize a default profile picture

        Image image = new Image(getClass().getResource("media/profiles/tempProfile.PNG").toExternalForm());
        ProfileCircle.setFill(new ImagePattern(image));

        //set the current values to the fields
        NameField.setText(activeUser.getFullName());
        PasswordField.setText(activeUser.getPassword());
        BioField.setText(activeUser.getBio());
        EmailField.setText(activeUser.getEmail());
    }
}
