package sbu.cs.group4.frontEnd.View.Children;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import sbu.cs.group4.frontEnd.View.Parent.ControllerClass;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingController extends ControllerClass implements Initializable
{
    @FXML
    private Circle profileImage;
    @FXML
    private Text postsNumberText;
    @FXML
    private Text followersNumberText;
    @FXML
    private Text followingsNumberText;
    @FXML
    private Text UsernameText;

    @FXML
    void ArchiveButton(ActionEvent event)
    {

    }

    @FXML
    void CloseFriendButton(ActionEvent event)
    {

    }

    @FXML
    void Covid19Button(ActionEvent event)
    {

    }

    @FXML
    void DiscoverPeopleButton(ActionEvent event)
    {

    }

    @FXML
    void QRButton(ActionEvent event)
    {

    }

    @FXML
    void SavedButton(ActionEvent event)
    {

    }

    @FXML
    void SettingButton(ActionEvent event)
    {

    }

    @FXML
    void YourActivityButton(ActionEvent event)
    {

    }

    @FXML
    void ProfileButton(ActionEvent event) throws IOException
    {
        switchScene(event, "ProfileTab(Posts)");
    }

    @FXML
    void addPost(ActionEvent event) throws IOException
    {
        switchScene(event, "AddPostTab");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //initialize a default profile picture

        Image image = new Image(getClass().getResource("media/profiles/tempProfile.PNG").toExternalForm());
        profileImage.setFill(new ImagePattern(image));

        //fill stuff

        String username = activeUser.getUsername();
        String postNumber = String.valueOf(activeUser.getPosts().size());
        String followersNumber = String.valueOf(activeUser.getFollowers().size());
        String followingsNumber = String.valueOf(activeUser.getFollowings().size());

        UsernameText.setText(username);
        postsNumberText.setText(postNumber);
        followersNumberText.setText(followersNumber);
        followingsNumberText.setText(followingsNumber);
    }
}
