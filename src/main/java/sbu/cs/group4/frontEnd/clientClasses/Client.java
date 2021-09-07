package sbu.cs.group4.frontEnd.clientClasses;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sbu.cs.group4.connectors.dataTransfer.DataTransferProcessor;
import sbu.cs.group4.frontEnd.View.Parent.ControllerClass;

import java.io.IOException;
import java.net.Socket;

public class Client extends Application
{
    private final int PORT_NUMBER = 5010;
    private final String IP_ADDRESS = "localhost";

    public static void main(String[] args)
    {
        try
        {
            new Client().setUpConnection();
            launch(args);
        }
        catch (IOException e)
        {
            System.out.println("could not connect to server");
            e.printStackTrace();
        }
    }

    public void setUpConnection() throws IOException
    {
        DataTransferProcessor dtp = new DataTransferProcessor(new Socket(IP_ADDRESS, PORT_NUMBER));
        ControllerClass.setDtp(dtp);
        new Thread(new ServerListener(dtp)).start();
    }

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SplashPage.fxml"));
        primaryStage.setTitle("Instagram");
        primaryStage.setScene(new Scene(root, 340, 490));
        primaryStage.show();
    }
}



