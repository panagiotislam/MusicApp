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
    ListView artistNames;
    List<String> names = Arrays.asList("Alexander Nakarada", "Anonymous for Good Reasons", "Arthur Fordsworthy", "Brett VanDonsel", "Brian Boyko", "dogsounds", "James Anderson",
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

        artistNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                artistNames.setClickable(false);
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


    @Override
    protected void onResume() {
        super.onResume();
        artistNames.setClickable(true);
    }


}


