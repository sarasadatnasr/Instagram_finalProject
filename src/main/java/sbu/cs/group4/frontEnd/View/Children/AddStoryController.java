package sbu.cs.group4.frontEnd.View.Children;

import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import sbu.cs.group4.connectors.elements.Post;
import sbu.cs.group4.frontEnd.View.Parent.NotificationController;

import java.io.File;
import java.io.IOException;

public class AddStoryController extends NotificationController{

        @FXML
        private JFXTextField postAddressField;

        @FXML
        private JFXTextField CaptionField;

        @FXML
        void doneButton(ActionEvent event) throws IOException
        {
            //initialize the jsonOutput
            jsonOutput = new JsonObject();

            //get the post's text information
            String caption = CaptionField.getText();

            //fill the jsonOutput
            jsonOutput.addProperty("request", "post");
            jsonOutput.addProperty("caption", caption);

            //send the jsonOutput to server
            dtp.sendJson(jsonOutput);

            //wait for a response
            while(jsonInput == null)
            {
            }

            //the operation was successful
            if(checkJsonResult())
            {
                //initialize the fileOutput
                fileOutput = new JsonObject();

                //get the post's file information
                String address = postAddressField.getText();

                //fill the fileOutput
                fileOutput.addProperty("file", dtp.encodeFile(new File(address)));

                //send the fileOutput to server
                dtp.sendJson(fileOutput);

                //wait for a response
                while(jsonInput == null)
                {
                }

                //the file operation was successful
                if(checkJsonResult())
                {
                    //get the post object
                    Post post = gson.fromJson(jsonInput.get("post").getAsString(), Post.class);

                    //add the post to the activeUser
                    activeUser.addPost(post);

                    //go to the profile tab
                    switchScene(event, "ProfileTab(Posts)");
                }

                //the fil  operation was not successful
                else
                {
                    showError("the fil  operation was not successful",3000);
                }
            }

            //the text operation was successful
            else
            {
                showError("the text operation was successful",3000);
            }

            jsonInput = null;
        }

}
