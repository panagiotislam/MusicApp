import impl.*;

import java.io.*;
import java.net.Socket;

import java.util.Queue;
import java.util.Scanner;

public class ConnectionHandler implements Runnable {

    private BrokerNode broker;
    private Socket socket;

    public ConnectionHandler(BrokerNode broker, Socket socket) {
        this.broker = broker;
        this.socket = socket;
    }


    @Override
    public void run() {
        System.out.println("[Server] Received Connection: " + socket);
        try {
            Scanner inputStream = new Scanner(socket.getInputStream());
            String isConnectionRequest = inputStream.nextLine();
            if (isConnectionRequest.equalsIgnoreCase("Connection Request")) {
                String[] pubInfo = inputStream.nextLine().split(",");
                if (pubInfo[2].equalsIgnoreCase("0")) {
                    System.out.println("[Broker " + broker.getBrokerId() + "] Handling Publisher Connection");
                    broker.acceptConnection(pubInfo[0], pubInfo[1]);//artist name,publisherid

                    String input1 = inputStream.nextLine();
                    parseIncomingMessage(input1);


                    inputStream.close();
                } else {
                    System.out.println("[Broker " + broker.getBrokerId() + "] Handling Subscriber Connection");
                    String[] NameSong = pubInfo[0].split("-");
                    broker.acceptConnection(NameSong[0], Integer.parseInt(pubInfo[1]));
                    PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);
                    //System.out.println(pubInfo[0]);
                    boolean found = false;
                    while (true) {

                        Queue<Value> messages = broker.getTopics().get(NameSong[0]);
                        for (Value s : messages) {
                            System.out.println("Sending");
                            if (s.getMusicFile().getTrackName().equals(NameSong[1])) {
                                outputStream.println(" Value: " + s);
                                found = true;
                            }
                        }
                        if(found == false)  System.out.println("No messages available");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error:" + socket);
        } finally {
            try { socket.close(); } catch (IOException e) {}
            System.out.println("Closed: " + socket);
        }
    }

    private void parseIncomingMessage(String input2) {
        String[] valueInfo = input2.split("Value: ");
        String artistName = valueInfo[0].split(":")[1].trim().replace(",", "");
        artistName=artistName.split("-")[0];
		Value value = extractvalue_values(valueInfo[1]);
        broker.storeValueToTopic(artistName,value);
    }

    private Value extractvalue_values(String input3) {

        //byte[] musicFileExtract= new byte[' '];
        String[] split = input3.split(",");
		String trackName = split[0].split("='")[1].replace("'", "");
		String artistName=split[1].split("='")[1].replace("'", "");
        String albumInfo=split[2].split("='")[1].replace("'", "");
        String genre=split[3].split("='")[1].replace("'", "");
        String musicFileExtract = (split[4]).split("=")[1];

        MusicFile music=new MusicFile(trackName,artistName,albumInfo,genre,musicFileExtract);
		return new Value(music);

    }

    
}
