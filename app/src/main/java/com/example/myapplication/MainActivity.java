package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.impl.Hash;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    Button requestButton;
    TextView songText;
    ListView artistNames;
    List<String> names = Arrays.asList("Alexander Nakarada", "Anonymous for Good Reasons", "Arthur Fordsworthy", "Brett Van Donsel", "Brian Boyko", "Brian Boyko", "dogsounds", "James Anderson",
            "Jason Shaw", "Kevin MacLeod", "Komiku", "Orchestralis", "Phase Shift", "Rafael Krux", "Severed Personality", "statusq", "Unknown");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestButton = findViewById(R.id.requestButton);
        songText = findViewById(R.id.songText);
        artistNames = findViewById(R.id.artistView);

        Adapter myadapter = new Adapter(this, names);
        artistNames.setAdapter(myadapter);




        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songText.setText("Please enter song name.");
                requestButton.setText(" Press to continue");
            }
        });
    }


    private class MyTask extends AsyncTask<String, Integer, String> {

        private final static String BROKER_IP = "127.0.0.1";
        private final static int SUB_ID = 1;

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Do something like display a progress bar
        }

        // This is run in a background thread
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
            String myString = params[0];

            // Do something that takes a long time, for example:
            String input = "empty";
            Scanner in = new Scanner(System.in);
            System.out.print("Enter ArtistName-SongName: ");
            input = in.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Thank you for using our app!");
            }

            if (input != "empty" && input != " " && input != null) {
                SubscriberNode subscriberNode = new SubscriberNode(SUB_ID, input, BROKER_IP, 7999 + Hash.getBroker(input.split("-")[0]));
                subscriberNode.connect();
            } else {
                System.out.println("Sorry!This is not a valid track" + input);
            }

            return "this string is passed to onPostExecute";
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Do things like hide the progress bar or change a TextView
        }
    }
}


