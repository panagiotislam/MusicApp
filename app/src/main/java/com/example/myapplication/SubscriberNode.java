package com.example.myapplication;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.*;
import java.net.Socket;
import com.example.myapplication.impl.*;


import org.apache.commons.codec.binary.Base64;

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
    private PrintWriter outputStream;

    private Value v;

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
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("waking");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
                v = parseIncomingMessage(receivedMessages.split(" Value: ")[1]);
                //consumedMessages.add(v);
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

    public Value getV() {
        return v;
    }

}


