package sbu.cs.group4.frontEnd.View.Children;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import sbu.cs.group4.connectors.elements.Post;
import sbu.cs.group4.frontEnd.View.Parent.NotificationController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController extends NotificationController {
    @FXML
    private JFXListView listViewMyProfile;
    private List<Post> posts;
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

    @FXML
    void IGTVButton(ActionEvent event) throws IOException {
        switchScene(event, "ProfileTab(IGTV)");
    }

    @FXML
    void TaggedButton(ActionEvent event) throws IOException {
        switchScene(event, "ProfileTab(Tagged)");
    }

    @FXML
    void postButton(ActionEvent event) throws IOException {
        switchScene(event, "ProfileTab(Posts)");
    }

    @FXML
    void addPost(ActionEvent event) throws IOException {
        switchScene(event, "AddPostTab");
    }

    @FXML
    void editProfile(ActionEvent event) throws IOException {
        switchScene(event, "EditProfileTab");
    }

    @FXML
    void setting(ActionEvent event) throws IOException {
        switchScene(event, "SettingTab");
    }
}
/*
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        posts=new ArrayList<>(date());
        try {
            for (int i = 0; i < posts.size(); i++) {
                FXMLLoader fxmlLoader =new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxml/PostBox.fxml"));

                listViewMyProfile=fxmlLoader.load();

                PostBoxController postBoxController=fxmlLoader.getController();
                postBoxController.setPost(posts.get(i));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //initialize a default profile picture
        Image image = new Image(getClass().getResource("media/profiles/tempProfile.PNG").toExternalForm());
        profileImage.setFill(new ImagePattern(image));

        //fill stuff
        UsernameText.setText(activeUser.getUsername());
        Biography.setText(activeUser.getBio());
        followersNumber.setText(String.valueOf(activeUser.getFollowers().size()));
        followingsNumber.setText(String.valueOf(activeUser.getFollowings().size()));
        postsNumber.setText(String.valueOf(activeUser.getPosts().size()));
    }

  /* private List<Post>date(){
        List<Post>ls=new ArrayList<>();
        Post post = new Post();
        post.setPostImageSrc("/Image/activity1.png");
        post.setDate("DAYS");
        post.setNbComments("12");
        post.setNbLikes("12");
        ls.add(post);
        return ls;
    }


}*/
