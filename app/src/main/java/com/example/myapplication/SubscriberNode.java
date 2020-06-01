package com.example.myapplication;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.*;
import java.net.Socket;
import com.example.myapplication.impl.*;

import java.util.ArrayList;
import android.util.Base64;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SubscriberNode {

    private int subscriberId;

    private String input;

    private Socket clientSocket;
    private BufferedReader inputStream;

    private int port;
    private String host;

    private List<Value> consumedMessages = new ArrayList<Value>();
    private byte[] musictest = new byte[' '];
    private PrintWriter outputStream;
    private Value s;

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

            this.inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            registerToBroker(input, subscriberId + "");

            pull();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
        String receivedMessages;
        String p = "";
        try {
            while ((receivedMessages = inputStream.readLine()) != null) {
                Value v = parseIncomingMessage(receivedMessages.split(" Value: ")[1]);
                consumedMessages.add(v);
                p = v.getMusicFile().getMusicFileExtract();
                musictest = Base64.decode(p, 0);//not used for now, works right
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
        String artistName = split[1].split("='")[1].replace("'", "");
        String albumInfo = split[2].split("='")[1].replace("'", "");
        String genre = split[3].split("='")[1].replace("'", "");
        System.out.println((split[4]).split("=")[1].toString().replace("[", "").replace("}", ""));
        String musicFileExtract = (split[4]).split("=")[1];
        MusicFile music = new MusicFile(trackName, artistName, albumInfo, genre, musicFileExtract);
        return new Value(music);

    }

    public Value getV() {
        try {
            return consumedMessages.get(0);
        } catch (NullPointerException n) {
            return new Value();
        }
    }


}



