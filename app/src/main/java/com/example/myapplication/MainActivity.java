package com.example.myapplication;

import com.example.myapplication.Adapter;
import com.example.myapplication.R;
import com.example.myapplication.SubscriberNode;
import com.example.myapplication.impl.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String ARTIST_NAME = "artist_name";
    TextView songText;
    ListView artistNames;
    List<String> names = Arrays.asList("Alexander Nakarada", "Anonymous for Good Reasons", "Arthur Fordsworthy", "Brett Van Donsel", "Brian Boyko", "dogsounds", "James Anderson",
            "Jason Shaw", "Kevin MacLeod", "Komiku", "Orchestralis", "Phase Shift", "Rafael Krux", "Severed Personality", "statusq", "Train Robbin' Scoundrels", "Unknown");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        songText = findViewById(R.id.songText);
        artistNames = findViewById(R.id.artistView);

        Adapter myadapter = new Adapter(this, names);
        artistNames.setAdapter(myadapter);


//        requestButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                songText.setText("Please enter song name.");
//                requestButton.setText("Kevin MacLeod-Pickled Pink");
//                MyTask asyncTask = new MyTask();
//                asyncTask.execute("Kevin MacLeod-Pickled Pink");
//
//            }
//        });

        songText.setText("LOL");

        artistNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String artist_name = ((TextView) view.findViewById(R.id.lines)).getText().toString();
                songText.setText("Select song.");
                Intent intent = new Intent(MainActivity.this,SongsActivity.class);
                Bundle b = new Bundle();
                b.putString(ARTIST_NAME, artist_name);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }



}


