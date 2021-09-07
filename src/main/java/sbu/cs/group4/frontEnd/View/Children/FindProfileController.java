package sbu.cs.group4.frontEnd.View.Children;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import sbu.cs.group4.connectors.elements.User;
import sbu.cs.group4.frontEnd.View.Parent.NotificationController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FindProfileController extends NotificationController
{
    private static User user;

    @FXML
    private Circle ProfileCircle;
    @FXML
    private Text UsernameField;


    public static void setUser(User newUser)
    {
        user = newUser;
    }

    @FXML
    void SearchField(ActionEvent event) throws IOException
    {
        switchScene(event, "SearchTab");
    }

    @FXML
    void SeeProfileButton(ActionEvent event) throws IOException
    {
        ViewUserController.setUser(user);
        if(activeUser.getFollowings().contains(user))
        {
            switchScene(event, "UnfollowUserTab");
        }

        else
        {
            switchScene(event, "followUserTab");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //get the username

        String username = user.getUsername();
        UsernameField.setText(username);

        //initialize a default profile picture

        Image image = new Image(getClass().getResource("media/profiles/tempProfile.PNG").toExternalForm());
        ProfileCircle.setFill(new ImagePattern(image));
    }
}
