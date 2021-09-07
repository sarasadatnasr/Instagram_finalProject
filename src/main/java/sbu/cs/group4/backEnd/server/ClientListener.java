package sbu.cs.group4.backEnd.server;

import com.google.gson.JsonObject;
import sbu.cs.group4.connectors.dataTransfer.DataTransferProcessor;

import java.io.IOException;

public class ClientListener implements Runnable
{
    private JsonObject input;
    private DataTransferProcessor dtp;
    private ClientHandler clientHandler;

    public ClientListener(ClientHandler clientHandler)
    {
        this.clientHandler = clientHandler;
        dtp = clientHandler.getDtp();

        input = null;
    }

    @Override
    public void run()
    {
        System.out.println("server listening to clients...");
        try
        {
            while ((input = dtp.receiveJson()) != null)
            {
                if (input.has("request"))
                {
                    clientHandler.setJsonInput(input);
                }

                else if (input.has("file"))
                {
                    clientHandler.setFileInput(input);
                }
            }

            clientHandler.setQuit(true);
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
