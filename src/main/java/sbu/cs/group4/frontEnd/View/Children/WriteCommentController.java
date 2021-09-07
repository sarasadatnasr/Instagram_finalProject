package sbu.cs.group4.frontEnd.View.Children;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import sbu.cs.group4.frontEnd.View.Parent.ControllerClass;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class WriteCommentController extends ControllerClass implements Initializable
{
    @FXML
    private Text UsernameField;

    @FXML
    private JFXTextField CommentField;

    @FXML
    private Button SendButton;

    @FXML
    private ListView<String> CommentList;
    private List<String> comments;

    @FXML
    void SendButton(ActionEvent event)
    {
        String comment = CommentField.getText();
        comments = Arrays.asList(activeUser + ":" + comment);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        CommentList.setItems(FXCollections.observableList(comments));

    }

    public void backbutton(ActionEvent actionEvent) {
    }
}