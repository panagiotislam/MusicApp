public class BrokerRunner {
    private final static String BROKER_IP  = "127.0.0.1";
    private final static int BROKER_PORT   = 8000;

    public static void main(String[] args) {

        BrokerNode brokerNode = new BrokerNode(1, BROKER_IP, BROKER_PORT);

        Thread brokerThread = new Thread(brokerNode::initialize);

        brokerThread.start();
    }
}