package com.example.myapplication;

import com.example.myapplication.impl.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button requestButton;
    TextView songText;
    ListView artistNames;
    List<String> names = Arrays.asList("Alexander Nakarada", "Anonymous for Good Reasons", "Arthur Fordsworthy", "Brett Van Donsel", "Brian Boyko", "dogsounds", "James Anderson",
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
                MyTask asyncTask = new MyTask();
                asyncTask.execute("Kevin MacLeod-Pickled Pink");
            }
        });


    }


    private class MyTask extends AsyncTask<String, Integer, Value> {

        private final static String BROKER_IP = "10.0.2.2";
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
        protected Value doInBackground(String... params) {

            String input = params[0];

            SubscriberNode subscriberNode = new SubscriberNode(SUB_ID, input, BROKER_IP, 7999 + Hash.getBroker(input.split("-")[0]));
            subscriberNode.connect();

            return subscriberNode.getV();
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(Value result) {
            super.onPostExecute(result);
            songText.setText(result.getMusicFile().getAlbumInfo() + " " + result.getMusicFile().getGenre());
            // Do things like hide the progress bar or change a TextView
        }
    }
}


