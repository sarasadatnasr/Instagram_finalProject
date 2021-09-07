package sbu.cs.group4.frontEnd.clientClasses;

import com.google.gson.JsonObject;
import sbu.cs.group4.connectors.dataTransfer.DataTransferProcessor;
import sbu.cs.group4.frontEnd.View.Parent.ControllerClass;

import java.io.IOException;

public class ServerListener implements Runnable
{
    private DataTransferProcessor dtp;
    private JsonObject input;

    public ServerListener(DataTransferProcessor dtp)
    {
        this.dtp = dtp;
    }

    @Override
    public void run()
    {
        try
        {
            //receive json messages from server
            while ((input = dtp.receiveJson()) != null)
            {
                //the received json message is a notification
                if (input.has("notification"))
                {
                    //add the notification to the ControllerClass's list of new notifications
                    ControllerClass.addNotification(input);
                }

                //the received json message is a response to a request of the client
                else if (input.has("result"))
                {
                    //assign the jsonInput to the ControllerClass's jsonInput variable
                    ControllerClass.setJsonInput(input);
                }

                //the received json message is a file
                else if(input.has("file"))
                {
                    ControllerClass.setFileInput(input);
                }
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
