package sbu.cs.group4.frontEnd.View.Children;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import sbu.cs.group4.connectors.elements.Post;
import sbu.cs.group4.connectors.elements.Story;
import sbu.cs.group4.frontEnd.View.Parent.ControllerClass;
import java.io.IOException;

public class StoryBoxController extends ControllerClass {

    @FXML
    private AnchorPane AnchorBox;

    @FXML
    private Text UsernameText;

    @FXML
    private Circle StoryCircle;

    @FXML
    void StoryCircle(ActionEvent event) throws IOException {
        switchScene(event, "SeeStoryTab");
    }
//set them please
    public void setStory(Story story) {
        //StoryCircle.setFill(getClass().getResource(senderProfile));
       // UsernameText.setText(sender);
    }
}
