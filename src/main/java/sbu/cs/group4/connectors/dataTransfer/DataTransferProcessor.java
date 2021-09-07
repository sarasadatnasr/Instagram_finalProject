package sbu.cs.group4.connectors.dataTransfer;

import com.google.gson.*;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Base64;

public class DataTransferProcessor
{
    private Socket socket;

    //writer and reader
    private PrintWriter writer;
    private BufferedReader reader;

    //file writer and reader(local)
    private BufferedInputStream localFileReader;
    private BufferedOutputStream localFileWriter;

    private Gson gson;
    private Base64.Encoder encoder;
    private Base64.Decoder decoder;

    public DataTransferProcessor(Socket socket) throws IOException
    {
        this.socket = socket;
        gson = new GsonBuilder().serializeNulls().create();
        encoder = Base64.getEncoder();
        decoder = Base64.getDecoder();

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendJson(JsonObject jsonObj)
    {
        String jsonStr = gson.toJson(jsonObj);
        String encodedJson = encoder.encodeToString(jsonStr.getBytes());
        writer.println(encodedJson);
    }

    public JsonObject receiveJson() throws IOException
    {
        String encodedStr = reader.readLine();

        if (encodedStr == null)
        {
            return null;
        }

        String decodedStr = new String(decoder.decode(encodedStr));
        JsonElement jsonElement = new JsonParser().parse(decodedStr);

        if (jsonElement.isJsonObject())
        {
            return jsonElement.getAsJsonObject();
        }

        return null;
    }

    public String encodeFile(File file) throws IOException
    {
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        return encoder.encodeToString(fileBytes);
    }

    public File decodeFile(String encodedFile, String savePath) throws IOException
    {
        byte[] decodedBytes = decoder.decode(encodedFile);

        try (FileOutputStream fos = new FileOutputStream(savePath))
        {
            fos.write(decodedBytes);
        }

        return new File(savePath);
    }


    public void close()
    {
        writer.close();
    }
}
