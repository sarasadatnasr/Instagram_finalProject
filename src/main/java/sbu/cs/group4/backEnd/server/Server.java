package sbu.cs.group4.backEnd.server;

import com.google.gson.JsonObject;
import sbu.cs.group4.backEnd.database.SqlHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class Server
{
    private final int PORT_NUMBER = 5010;

    private SqlHandler sqlHandler;
    private ArrayList<ClientHandler> onlineConnections;

    private StoryDeleter storyDeleter;


    public Server()
    {
        sqlHandler = new SqlHandler();
        onlineConnections = new ArrayList<>();
    }

    public SqlHandler getSqlHandler()
    {
        return sqlHandler;
    }

    public static void main(String args[])
    {
        try
        {
            new Server().start();
        }

        catch (SQLException e)
        {
            System.out.println("could not connect to database");
            e.printStackTrace();
        }

        catch (IOException e)
        {
            System.out.println("could not start the server");
            e.printStackTrace();
        }
    }

    public void start() throws SQLException, IOException
    {
        System.out.println("Running server.");

        sqlHandler.init();
        System.out.println("Database is initialized.");

        storyDeleter = new StoryDeleter(this);
        new Thread(storyDeleter).start();

        ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);

        System.out.println("Waiting for clients...");
        while (true)
        {
            Socket clientSocket = serverSocket.accept();
            System.out.println("got a connection");

            ClientHandler clientHandler = new ClientHandler(clientSocket, this);
            onlineConnections.add(clientHandler);

            ClientListener clientListener = new ClientListener(clientHandler);

            new Thread(clientListener).start();
            new Thread(clientHandler).start();
        }
    }

    public void sendNotification(String username, JsonObject notification)
    {
        for (ClientHandler clientHandler : onlineConnections)
        {
            if (clientHandler.getUsername().equals(username))
            {
                clientHandler.getDtp().sendJson(notification);
                return;
            }
        }
    }

    public void removeClient(ClientHandler clientHandler)
    {
        onlineConnections.remove(clientHandler);
    }
}