import impl.Hash;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;

public class SubscriberRunner {
    //Kevin MacLeod-Pickled Pink
    private final static String BROKER_IP = "127.0.0.1";
    private final static int BROKER_PORT = 8000;
    private final static int SUB_ID = 1;
    private final static String brokerid = "2";


    public static void main(String[] args) {

        System.out.println("Welcome to the music app!");
        while (true) {
            String input = "empty";
            Scanner in = new Scanner(System.in);
            System.out.print("Enter ArtistName-SongName: ");
            input = in.nextLine();
            deletefile();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Thank you for using our app!");
                break;
            }

            if (input != "empty" && input != " " && input != null) {
                SubscriberNode subscriberNode = new SubscriberNode(SUB_ID, input, BROKER_IP, 7999 + Hash.getBroker(input.split("-")[0]));
                subscriberNode.connect();

            } else {
                System.out.println("Sorry!This is not a valid track" + input);
            }
        }
    }


    private static void deletefile() {
        String help ="";
        for (int i = 0; i < 20; i++) {
            try {
                help= i+ "test.mp3";
                Files.deleteIfExists(Paths.get("C:\\Users\\Dragon\\Desktop\\ERGASIA\\" +help));
            } catch (NoSuchFileException e) {
                System.out.println("No such file/directory exists");
            } catch (DirectoryNotEmptyException e) {
                System.out.println("Directory is not empty.");
            } catch (IOException e) {
                System.out.println("Invalid permissions.");
            }

            System.out.println("Deletion successful.");
        }
    }

}
