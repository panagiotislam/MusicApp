package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String ARTIST_NAME = "artist_name";
    TextView songText;
    ListView artistNamesListView;
    List<String> artistNamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        songText = findViewById(R.id.songText);
        artistNamesListView = findViewById(R.id.artistView);
        initializeArtistNames();
        Adapter myAdapter = new Adapter(this, artistNamesList);
        artistNamesListView.setAdapter(myAdapter);

        artistNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                artistNamesListView.setClickable(false);
                String artist_name = ((TextView) view.findViewById(R.id.lines)).getText().toString();
                openSongActivity(artist_name);
            }
        });
    }

    private void  initializeArtistNames() {
         artistNamesList = Arrays.asList("Alexander Nakarada", "Anonymous for Good Reasons", "Arthur Fordsworthy", "Brett VanDonsel", "Brian Boyko", "dogsounds", "James Anderson",
                "Jason Shaw", "Kevin MacLeod", "Komiku", "Orchestralis", "Phase Shift", "Rafael Krux", "Severed Personality", "statusq", "Train Robbin' Scoundrels", "Unknown");

    }

    private void openSongActivity(String artistName) {
        Intent intent = new Intent(MainActivity.this,SongsActivity.class);
        Bundle b = new Bundle();
        b.putString(ARTIST_NAME, artistName);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        artistNamesListView.setClickable(true);
        artistNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                artistNamesListView.setClickable(false);
                String artist_name = ((TextView) view.findViewById(R.id.lines)).getText().toString();
                openSongActivity(artist_name);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        artistNamesListView.setOnItemClickListener(null);
    }

}


