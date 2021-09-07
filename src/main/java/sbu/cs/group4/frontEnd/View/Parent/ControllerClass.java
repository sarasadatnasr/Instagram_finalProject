package sbu.cs.group4.frontEnd.View.Parent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXSnackbar;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sbu.cs.group4.connectors.dataTransfer.DataTransferProcessor;
import sbu.cs.group4.connectors.elements.User;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerClass
{
    protected static JsonObject jsonInput = null;
    protected JsonObject jsonOutput;

    protected static JsonObject fileInput = null;
    protected JsonObject fileOutput;

    protected static User activeUser = null;
    protected static ArrayList<JsonObject> notifications = new ArrayList<>();

    protected static DataTransferProcessor dtp = null;
    protected static final Gson gson = new GsonBuilder().serializeNulls().create();

    protected final String PROFILE_PATH = "src/main/resources/media/profiles/";
    protected final String POST_PATH = "src/main/resources/media/posts/";
    protected final String STORY_PATH = "src/main/resources/media/stories/";


    public static void setDtp(DataTransferProcessor newDtp)
    {
        dtp = newDtp;
    }

    public static void setJsonInput(JsonObject newJsonInput)
    {
        jsonInput = newJsonInput;
    }

    public static void setFileInput(JsonObject newFileInput)
    {
        fileInput = newFileInput;
    }

    public static void addNotification(JsonObject notification)
    {
        notifications.add(notification);
    }

    public static boolean checkJsonResult()
    {
        if (jsonInput.has("result"))
        {
            return jsonInput.get("result").getAsBoolean();
        }

        return false;
    }

    public static boolean checkFileResult()
    {
        if (fileInput.has("result"))
        {
            return fileInput.get("result").getAsBoolean();
        }

        return false;
    }

    public void switchScene(ActionEvent event, String fxmlFileName) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + fxmlFileName + ".fxml"));
        Stage s1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        s1.setScene(scene);
        s1.show();
    }

    public void showError(String errorTXT,int time){
        JFXSnackbar snack = new JFXSnackbar();
        snack.show(errorTXT, time);
        snack.prefHeight(320.0);
        snack.prefWidth(320.0);
    }



}