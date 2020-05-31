import java.io.*;
import java.net.Socket;
import impl.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SubscriberNode  {

    private int subscriberId;

    private String input;

    private Socket clientSocket;
    private BufferedReader inputStream;

    private int port;
    private String host;

    private List<Value> consumedMessages = new ArrayList<Value>();
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
        String p = "";
        try {
            int i =0;
            boolean flag = false;
            while ((receivedMessages = inputStream.readLine()) != null) {
                flag=true;
                Value v = parseIncomingMessage(receivedMessages.split(" Value: ")[1]);
                consumedMessages.add(v);
                p=v.getMusicFile().getMusicFileExtract();
                musictest = Base64.getMimeDecoder().decode(p);
                writeByte(musictest, i);
                i++;
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!flag) {
                System.out.println("Sorry, but we don't have this song.");
            } else {
                System.out.println("You can now play the song from the project file. The files will be deleted if you enter another song or exit the app.");
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
        String musicFileExtract = (split[4]).split("=")[1];
        MusicFile music=new MusicFile(trackName,artistName,albumInfo,genre,musicFileExtract);
        return new Value(music);

    }



    static void writeByte(byte[] bytes, int i){
    String FILEPATH = ( "ERGASIA"  + i+ "test.mp3");
    File file = new File(FILEPATH.split("A")[2]);

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


}


