package sbu.cs.group4.frontEnd.View.Children;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import sbu.cs.group4.connectors.elements.Post;
import sbu.cs.group4.connectors.elements.Story;
import sbu.cs.group4.frontEnd.View.Parent.NotificationController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController extends NotificationController implements Initializable
{
    @FXML
    private Button AddStory;
    @FXML
    private JFXListView<Post>listViewMyHome;
    @FXML
    private Circle profileImage;

    //add story
    @FXML
    private JFXListView<Story> StoryViewList;
    ObservableList<Story>storyList = FXCollections.<Story>observableArrayList(
           // new Story()
            );

    @FXML
    void AddStory(ActionEvent event) throws IOException{

    }


    @FXML
    void DirectButton(ActionEvent event) throws IOException
    {
        switchScene(event, "ChooseDirectReceiver");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        StoryViewList.setItems(storyList);
        StoryViewList.setCellFactory(param -> new StoryCell());


        //show any available notification
        showNotificationSign();

        //initialize a default profile picture

        Image image = new Image(getClass().getResource("media/profiles/tempProfile.PNG").toExternalForm());
        profileImage.setFill(new ImagePattern(image));


        //initialize the jsonOutput
        jsonOutput = new JsonObject();

        //fill the jsonOutput
        jsonOutput.addProperty("result", true);

        //send the jsonOutput to server
        dtp.sendJson(jsonOutput);

        //wait for a response
        while (jsonInput == null)
        {
        }

        //the operation was successful
        if (checkJsonResult())
        {
            //initialize the followingsPosts with post objects
            ArrayList<Post> followingsPosts = gson.fromJson(jsonInput.get("followingsPosts"),
                    new TypeToken<List<Post>>() {}.getType());

            //get the files of the posts
            for (Post post : followingsPosts)
            {
                //initialize the jsonOutput
                jsonOutput = new JsonObject();

                //fill the jsonOutput
                jsonOutput.addProperty("request", "getFile");
                jsonOutput.addProperty("fileType", "post");
                jsonOutput.addProperty("postID", post.getPostID());

                //send the jsonOutput to the client
                dtp.sendJson(jsonOutput);

                //wait for a response
                while (fileInput == null)
                {
                }

                //save the file of the post
                try
                {
                    dtp.decodeFile(fileInput.get("file").getAsString(), POST_PATH + post.getPostID());
                }

                catch (IOException e)
                {
                    showError("saving the file of the post has failed",3000);
                }
            }

            //fill the listViewMyHome
            listViewMyHome.setItems(FXCollections.<Post>observableArrayList(followingsPosts));
            listViewMyHome.setCellFactory(param -> {
                try {
                    return new PostCell();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            });
        }

        //the operation was not successful
        else
        {
          showError("the operation was not successful",3000);
        }

        jsonInput = null;
    }
}
