import impl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BrokerNode  {

    private int port;
    private String host;
    private int brokerId;

    private Map<String, Queue<Value>>    ArtistsNames;


    private ServerSocket serverSocket;

    public BrokerNode(int brokerId, String host, int port) {
        this.port     = port;
        this.brokerId = brokerId;
        this.host     = host;

        this.ArtistsNames  = new HashMap<>();

    }

    public void initialize() {
        try {
            System.out.println("[Broker " + brokerId + "] Starting... ");
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName(host));
            while (true) {
                Socket socket = null;
                try {
                    synchronized (serverSocket) {
                        socket = serverSocket.accept();
                    }
                    System.out.println("A new client is connected : " + socket );

                    System.out.println("Assigning new thread for this client");

                    new Thread(new ConnectionHandler(this, socket)).start();
                }

                catch (Exception e){
                    socket .close();
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getBrokerId() {
        return this.brokerId;
    }

    public void acceptConnection(String ArtistName, int ConsumerId) {
        System.out.println("[Broker " + brokerId + "] " + "Received Connection from Consumer with id: '" + ConsumerId + "'");
        ArtistsNames.keySet().forEach(System.out::println);
        if (!ArtistsNames.containsKey(ArtistName)) {
            System.out.println("Can't find the ArtistName '" + ArtistName + "' Consumer '" + ConsumerId + "' requested.");
        }else {
            System.out.println("Exists.");

        }
    }

    public void acceptConnection(String ArtistName, String publisherId) {
        System.out.println("[Broker " + brokerId + "] " + "Received Connection from publisher with id: '" + publisherId + "'");
        if (!ArtistsNames.containsKey(ArtistName)) {
            CreateArtistName(ArtistName);
        }

    }

    public void storeValueToTopic(String artistName, Value value) {
        ArtistsNames.forEach((d, k) -> {
            if (d.equals(artistName)) {
                k.add(value);
            }
        });
    }

    public boolean CreateArtistName(String ArtistName) {
        if (!ArtistsNames.containsKey(ArtistName)) {
            System.out.println("[Broker " + brokerId + "] Creating ArtistName: '" + ArtistName + "'");
            ArtistsNames.put(ArtistName, new ConcurrentLinkedQueue<>());
            return true;
        }

        return false;
    }

    public Map<String, Queue<Value>> getTopics() {
        return ArtistsNames;
    }
}
