package sbu.cs.group4.frontEnd.View.Children;

import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import sbu.cs.group4.connectors.elements.Post;
import sbu.cs.group4.connectors.elements.User;
import sbu.cs.group4.frontEnd.View.Parent.NotificationController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewUserController extends NotificationController
{
    private static User user;

    @FXML
    private JFXListView<Post> listViewUserProfile;
    @FXML
    private Circle profileImage;
    @FXML
    private Text postsNumber;
    @FXML
    private Text followersNumber;
    @FXML
    private Text followingsNumber;
    @FXML
    private Text UsernameText;
    @FXML
    private Text Biography;

    public static void setUser(User newUser)
    {
        user = newUser;
    }

    @FXML
    void IGTVButton(ActionEvent event)
    {

    }

    @FXML
    void TaggedPostButton(ActionEvent event)
    {

    }

    @FXML
    void backButton(ActionEvent event) throws IOException
    {
        switchScene(event, "SearchTab");
    }

    @FXML
    void messageButton(ActionEvent event) throws IOException
    {
        WriteDirectController.setMessageReceiver(user);
        switchScene(event, "DirectTab");
    }

    @FXML
    void UnfollowButton(ActionEvent event) throws IOException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the unfollow's information
        String unfollowed = user.getUsername();
        String unfollower = activeUser.getUsername();

        //fill the output json
        jsonOutput.addProperty("request", "unfollow");
        jsonOutput.addProperty("unfollower", unfollower);
        jsonOutput.addProperty("unfollowed", unfollowed);

        //send the jsonOutput to the server
        dtp.sendJson(jsonOutput);

        //wait for a response
        while (jsonInput == null)
        {
        }

        //the operation was successful
        if (checkJsonResult())
        {
            //delete the unfollowed user from the activeUser's followed users
            activeUser.unfollow(user.getUsername());

            //go to the follow tab
            switchScene(event, "followUserTab");
        }

        //the operation was not successful
        else
        {
            //!!!!!!!!!!!!!!!!!!!!!!!!!!
            //show an error
            //!!!!!!!!!!!!!!!!!!!!!!!!!!
        }

        jsonInput = null;
    }

    @FXML
    void followButton(ActionEvent event) throws IOException
    {
        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //get the follow's information
        String followed = user.getUsername();
        String follower = activeUser.getUsername();

        //fill the output json
        jsonOutput.addProperty("request", "follow");
        jsonOutput.addProperty("follower", follower);
        jsonOutput.addProperty("followed", followed);

        //send the jsonOutput to the server
        dtp.sendJson(jsonOutput);

        //wait for a response
        while (jsonInput == null)
        {
        }

        //the operation was successful
        if (checkJsonResult())
        {
            //delete the unfollowed user from the activeUser's followed users
            activeUser.follow(user.getUsername());

            //go to the follow tab
            switchScene(event, "unfollowUserTab");
        }

        //the operation was not successful
        else
        {
       showError("the operation was not successful",3000);
        }

        jsonInput = null;
    }

    private List<Post> posts;
    @FXML
    private GridPane PostGrid;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //show the notifications
        showNotificationSign();
        //////////////////////////////////////////////////////

        posts = new ArrayList<>(data());
        try {
            for (int i=0;i<posts.size();i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxml/PostBox.fxml"));
                VBox postBox = fxmlLoader.load();

                MyPostBoxController postController = fxmlLoader.getController();
                postController.setData(posts.get(i));

                PostGrid.add(postBox, i, 1);
                 PostGrid.setMargin(postBox, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
///////////////////////////////////////////////////////////

        //initialize a default profile picture
        Image image = new Image(getClass().getResource("media/profiles/tempProfile.PNG").toExternalForm());
        profileImage.setFill(new ImagePattern(image));

        //fill stuff
        UsernameText.setText(user.getUsername());
        Biography.setText(user.getBio());
        followersNumber.setText(String.valueOf(user.getFollowers().size()));
        followingsNumber.setText(String.valueOf(user.getFollowings().size()));
        postsNumber.setText(String.valueOf(user.getPosts().size()));
    }


//set them
    private List<Post> data() {
        List<Post> ls = new ArrayList<>();
        Post post = new Post();
        post.setPostImageSrc("/Image/activity1.png");
        post.setPostDate();
        post.setLikes();
        post.setCaption();
        ls.add(post);
        return ls;
    }
}
