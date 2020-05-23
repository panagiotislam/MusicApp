package com.example.myapplication;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.impl.Hash;
import com.example.myapplication.impl.Value;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SongsActivity extends AppCompatActivity {

    final String ARTIST_NAME = "artist_name";
    String artist_name;
    TextView selectSongTxt;
    HashMap<String, List<String>> songs;
    ListView artistSongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        getSupportActionBar().hide();
        selectSongTxt=findViewById(R.id.selectSongTxt);

        artistSongs = findViewById(R.id.songListView);
        songs = new HashMap<>();
        initializeSongs();
        Bundle b = getIntent().getExtras();
        if(b != null) {
            artist_name = b.getString(ARTIST_NAME);
        }
        selectSongTxt.setText(artist_name);

        Adapter myadapter = new Adapter(this, songs.get(artist_name));
        artistSongs.setAdapter(myadapter);

        artistSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String artist_song = ((TextView) view.findViewById(R.id.lines)).getText().toString();
                MyTask asyncTask = new MyTask(SongsActivity.this);
                asyncTask.execute(artist_name+"-"+artist_song);
            }
        });

    }

    private class MyTask extends AsyncTask<String, Integer, Value> {

        private final static String BROKER_IP = "10.0.2.2";
        private final static int SUB_ID = 1;
        ProgressDialog progressDialog ;

        public MyTask(SongsActivity activity) {
            progressDialog = new ProgressDialog(activity);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading song...");
            progressDialog.show();
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
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            selectSongTxt.setText(result.getMusicFile().getAlbumInfo() + " " + result.getMusicFile().getGenre());
            // Do things like hide the progress bar or change a TextView
        }
    }
    private void initializeSongs() {
        ArrayList<String> song = new ArrayList<String>(
                Arrays.asList("Ambient Bongos", "Apex", "Be Chillin", "Be Jammin", "Blacksmith", "Bonfire", "Brothers Unite",
                        "Burt's Requiem", "Celebration", "Chronos", "Consecrated Ground", "Construction", "Creepy Hallow", "Favorite", "Fireworks", "Foam Rubber",
                        "Hippety Hop", "Hor Hor", "Horizon Flare", "Journey of Hope", "Jökull (Metal Version)", "Le Baguette","Marked","Nightmare","Nomadic Sunset",
                        "Novus Initium", "Putin's Lullaby", "Silent Night (Unholy Night)", "Silly Intro", "The Crown", "The Lagoon", "The Story", "Uberpunch"));
        songs.put("Alexander Nakarada",song);
//        songs.put();
//        songs.put();
//        songs.put();
//        songs.put();
//        songs.put();
//        songs.put();
//        songs.put();
//        songs.put();
//        songs.put();

    }
}

