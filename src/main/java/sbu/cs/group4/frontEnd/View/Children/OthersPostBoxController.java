package sbu.cs.group4.frontEnd.View.Children;
import com.jfoenix.controls.JFXSnackbar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import sbu.cs.group4.connectors.elements.Post;
import sbu.cs.group4.frontEnd.View.Parent.ControllerClass;

import java.io.IOException;

public class OthersPostBoxController extends ControllerClass
{
    private Post post;

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private ImageView imageView;
    @FXML
    private Button likeButton;
    @FXML
    private Text LikeNumber;
    @FXML
    private Text commentNumber;

    public OthersPostBoxController() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PostBox.fxml"));
        loader.setController(this);
        AnchorPane = loader.load();
    }

    public void setPost(Post newPost)
    {
        post = newPost;
        LikeNumber.setText(String.valueOf(post.getLikes().size()));
        commentNumber.setText(String.valueOf(newPost.getComments().size()));
        imageView.setImage(new Image(POST_PATH + post.getPostID()));
    }

    public Node getView()
    {
        return AnchorPane;
    }

    @FXML
    void commentButton(ActionEvent event) throws IOException
    {
        switchScene(event, "WriteComment");
    }

    @FXML
    void likeButtonClicked(ActionEvent event) {
        Image image =  new Image(getClass().getResourceAsStream("Image/activity1.png"));
        if(!post.isThatLiked(activeUser.getUsername()))
        {
        post.getLikes().add(activeUser.getUsername());
            image = new Image(getClass().getResourceAsStream("Image/like.png"));
        }
        else {
            post.getLikes().remove(activeUser.getUsername());
           image = new Image(getClass().getResourceAsStream("Image/activity1.png"));
        }
        Image finalImage = image;
        likeButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e)
            {
                Button button = (Button) e.getSource();
                button.setGraphic(new ImageView(finalImage));
            }
        });
    }


    @FXML
    void likeButtonDragged(ActionEvent event) throws IOException {
        //make the list unique
        JFXSnackbar snack = new JFXSnackbar();
        snack.show(post.getLikes().toString(), 4000);
        snack.prefHeight(320.0);
        snack.prefWidth(320.0);
    }


    @FXML
    void SaveButton(ActionEvent event)
    {

    }

    @FXML
    void shareButton(ActionEvent event)
    {

    }

    public void DeleteButton(ActionEvent actionEvent) {
    }
}

