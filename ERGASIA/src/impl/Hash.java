package impl;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Hash {

    public static String sha(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);

            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getBroker(String artistName) {

        String result = sha(artistName);
        System.out.println(artistName);
        switch (assignment(result.substring(result.length() - 2))) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
        }
        return 3;
    }

    public static int assignment (String s){
        return s.replaceAll("\\D", "").length();
    }
}


//    public static Map<String, String> getBrokersMap(ArrayList<String> artistNames){
//        Map<String,String> brokers = new HashMap<>();
//        for (int i=0; i<artistNames.size(); i++){
//            String result = sha(artistNames.get(i));
//            switch(assignment(result.substring(result.length() - 2))){
//                case 0:
//                    System.out.println("0");
//					brokers.put(artistNames.get(i), "1");
//                case 1:
//                    System.out.println("1");
//					brokers.put(artistNames.get(i), "2");
//                case 2:
//                    System.out.println("2");
//					brokers.put(artistNames.get(i), "3");
//            }
//        }
//        return  brokers;
//    }

