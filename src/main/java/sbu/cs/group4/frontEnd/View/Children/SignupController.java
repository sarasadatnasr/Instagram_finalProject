package sbu.cs.group4.frontEnd.View.Children;

import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import sbu.cs.group4.connectors.elements.User;
import sbu.cs.group4.frontEnd.View.Parent.ControllerClass;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupController extends ControllerClass
{
    private int errorNumber = 0;

    @FXML
    private JFXTextField emailField;
    @FXML
    private Text emailError;
    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private Text usernameError;
    @FXML
    private JFXTextField passwordField;
    @FXML
    private Text passwordError;
    @FXML
    private Text textField;

    @FXML
    void SignupButton(ActionEvent event) throws IOException
    {
        //get the user information

        String email = emailField.getText();
        String fullName = nameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        //email is invalid
        if (!isEmailValid(email))
        {
            emailError.setText("*");
            errorNumber++;
        }

        //username is invalid
        if (!isUsernameValid(username))
        {
            usernameError.setText("*");
            errorNumber++;
        }

        //password is invalid
        if (!isPasswordValid(password))
        {
            passwordError.setText("*");
            errorNumber++;
        }

        //an error exists
        if (errorNumber > 0)
        {
            textField.setText("starred fields are invalid!");
        }

        //everything's fine
        else
        {
            //initialize the jsonOutput
            jsonOutput = new JsonObject();

            //add the information to the jsonOutput
            jsonOutput.addProperty("request", "signUp");
            jsonOutput.addProperty("username", username);
            jsonOutput.addProperty("password", password);
            jsonOutput.addProperty("fullName", fullName);
            jsonOutput.addProperty("email", email);

            //send the jsonOutput to server
            dtp.sendJson(jsonOutput);

            //wait for a response
            while (jsonInput == null)
            {
            }

            //the operation was successful
            if (checkJsonResult())
            {
                //initialize the activeUser
                activeUser = gson.fromJson(jsonInput.get("user").getAsString(), User.class);

                //go to profile tab panel
                switchScene(event, "ProfileTab(Posts)");
            }

            //the operation failed
            else
            {
                //show the error to the user
                textField.setText("the username " + username + " is taken.");
                usernameError.setText("*");
            }
        }

        errorNumber = 0;
        jsonInput = null;
    }

    @FXML
    void loginButton(ActionEvent event) throws IOException
    {
        switchScene(event, "Login");
    }

    //helper methods

    private boolean isUsernameValid(String username)
    {
        return username.length() >= 8 && username.length() <= 20
                && !username.contains(" ");
    }

    private boolean isPasswordValid(String password)
    {
        String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        if (password == null)
        {
            return false;
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }

    private boolean isEmailValid(String email)
    {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }
}


