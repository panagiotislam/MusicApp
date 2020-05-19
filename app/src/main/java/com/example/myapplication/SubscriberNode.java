package com.example.myapplication;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.*;
import java.net.Socket;
import com.example.myapplication.impl.*;


import org.apache.commons.codec.binary.Base64;

import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SubscriberNode  {

    private int subscriberId;

    private String input;

    private Socket clientSocket;
    private BufferedReader inputStream;

    private int port;
    private String host;

    private List<Value> consumedMessages;
    private byte[] musictest = new byte[' '];
    private PrintWriter outputStream;

    public SubscriberNode(int subscriberId, String input, String host, int port) {
        this.subscriberId = subscriberId;
        this.input = input;

        this.host = host;
        this.port = port;


    }


    public void connect() {
        System.out.println("Starting Consumer on Thread: " + Thread.currentThread().getName());
        try {
            this.clientSocket = new Socket(host, port);
            this.outputStream = new PrintWriter(clientSocket.getOutputStream(), true);

            this.inputStream =new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            registerToBroker(input, subscriberId+"");

            pull();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("waking");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            deleteFile();
            disconnect();
        }
    }

    public void disconnect() {
        try {
            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerToBroker(String input, String publisherId) {
        outputStream.println("Connection Request");
        outputStream.println(input + "," + publisherId + "," + 1);
    }

    public void pull() {
       String receivedMessages ;
        try {
            if ((receivedMessages = inputStream.readLine()) != null) {
                Value v = parseIncomingMessage(receivedMessages.split(" Value: ")[1]);
                //consumedMessages.add(v);
                musictest = v.getMusicFile().getMusicFileExtract();
                writeByte(musictest);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("[Subscriber " + subscriberId + "] Finished Consuming messages for topic: '" + input + "'.");
       } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Value parseIncomingMessage(String input_1) {

        String[] split = input_1.split(",");
        String trackName = split[0].split("='")[1].replace("'", "");
        String artistName=split[1].split("='")[1].replace("'", "");
        String albumInfo=split[2].split("='")[1].replace("'", "");
        String genre=split[3].split("='")[1].replace("'", "");
        System.out.println((split[4]).split("=")[1].toString().replace("[","").replace("}",""));
        byte[] musicFileExtract = Base64.encodeBase64Chunked((split[4]).split("=")[1].getBytes());
        MusicFile music=new MusicFile(trackName,artistName,albumInfo,genre,musicFileExtract);
        return new Value(music);

    }



    static void writeByte(byte[] bytes){
    String FILEPATH = ( "C:\\Users\\Dragon\\Desktop\\ERGASIA\\"  + "chunks.mp3");
    File file = new File(FILEPATH);

        try {
            OutputStream os = new FileOutputStream(file);

            os.write(bytes);
            System.out.println("Successfully"
                    + " byte inserted");

            os.close();
        }

        catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    static void deleteFile(){
        try
        {
            Files.deleteIfExists(Paths.get("C:\\Users\\Dragon\\Desktop\\ERGASIA\\chunks.mp3"));
        }
        catch(NoSuchFileException e)
        {
            System.out.println("No such file/directory exists");
        }
        catch(DirectoryNotEmptyException e)
        {
            System.out.println("Directory is not empty.");
        }
        catch(IOException e)
        {
            System.out.println("Invalid permissions.");
        }

        System.out.println("Deletion successful.");
    }
}


