package sbu.cs.group4.frontEnd.View.Children;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import sbu.cs.group4.frontEnd.View.Parent.ControllerClass;

import java.io.IOException;

public class SplashPageController extends ControllerClass
{
    @FXML
    void SplashButton1(ActionEvent event) throws IOException
    {
        switchScene(event, "Login");
    }

    @FXML
    void SplashButton2(ActionEvent event) throws IOException
    {
        switchScene(event, "Login");
    }
}

