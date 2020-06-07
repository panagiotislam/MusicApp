package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SongsActivity extends AppCompatActivity {

    final String ARTIST_NAME = "artist_name";
    final String ARTIST_SONG = "artist_song";
    String artist_name;
    String artist_song;
    TextView selectSongTxt;
    HashMap<String, List<String>> songsMap;
    ListView artistSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        getSupportActionBar().hide();


        selectSongTxt=findViewById(R.id.selectSongTxt);
        artistSongs = findViewById(R.id.songListView);

        initializeSongs();

        Bundle b = getIntent().getExtras();
        if(b != null) {
            artist_name = b.getString(ARTIST_NAME);
        }

        selectSongTxt.setText(artist_name);

        Adapter myadapter = new Adapter(this, songsMap.get(artist_name));
        artistSongs.setAdapter(myadapter);

        artistSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                artistSongs.setClickable(false);
                artist_song = ((TextView) view.findViewById(R.id.lines)).getText().toString();
                openMusicPlayerActivity(artist_song);

            }
        });

    }

    private void initializeSongs() {
        songsMap =new HashMap<>();
        ArrayList<String> song = new ArrayList<String>(
                Arrays.asList("Ambient Bongos", "Apex", "Be Chillin", "Be Jammin", "Blacksmith", "Bonfire", "Brothers Unite",
                        "Celebration", "Chronos", "Consecrated Ground", "Construction", "Creepy Hallow", "Favorite", "Fireworks", "Foam Rubber",
                        "Hippety Hop", "Hor Hor", "Horizon Flare", "Journey of Hope", "Jökull (Metal Version)", "Le Baguette","Marked","Nightmare","Nomadic Sunset",
                        "Novus Initium", "Silent Night (Unholy Night)", "Silly Intro", "The Crown", "The Lagoon", "The Story", "Uberpunch"));
        songsMap.put("Alexander Nakarada",song);

        song = new ArrayList<>(Arrays.asList("null"));
        songsMap.put("Anonymous for Good Reasons",song);

        song = new ArrayList<>(Arrays.asList("Footsteps in the Attic"));
        songsMap.put("Arthur Fordsworthy",song);

        song = new ArrayList<>(Arrays.asList("Quick Metal Riff 1"));
        songsMap.put("Brett VanDonsel",song);

        song = new ArrayList<>(Arrays.asList("Groovin"));
        songsMap.put("Brian Boyko",song);

        song = new ArrayList<>(Arrays.asList("A Waltz For Naseem"));
        songsMap.put("dogsounds",song);

        song = new ArrayList<>(Arrays.asList("Goldcrest"));
        songsMap.put("James Anderson",song);

        song = new ArrayList<>(Arrays.asList("River Meditation"));
        songsMap.put("Jason Shaw",song);

        song = new ArrayList<>(Arrays.asList("A Surprising Encounter", "Alternative Clock Dimension", "Amazing Grace", "Backbeat", "Baltic Levity", "Beat One",
                "Bit Bit Loop", "Bollywood Groove", "Breaking Bollywood", "City Sunshine", "Connecting Rainbows", "Dancing at the Inn", "Dark Hallway", "Driving Concern",
                "Drunken Party", "Evil Incoming", "Forest Frolic Loop", "Funky Energy Loop", "Funshine", "Goodnightmare", "Horns", "Icicles Melting",
               "Infinite Peace", "Infinite Wonder", "Lukewarm Banjo","Meditating Beat", "Midnight in the Green House",
                "Night in the Castle", "Organ Filler", "Painful Disorientation", "Painting Room", "Pickled Pink", "Sad Drunken Party", "Satin Danger", "The Britons"
                , "Village Tarantella", "Windy Old Weather", "Wisdom in the Sun","null"));
        songsMap.put("Kevin MacLeod",song);

        song = new ArrayList<>(Arrays.asList("A good bass for gambling", "Bleu", "Champ de tournesol", "La Citadelle"));
        songsMap.put("Komiku",song);

        song = new ArrayList<>(Arrays.asList("Motions"));
        songsMap.put("Orchestralis",song);

        song = new ArrayList<>(Arrays.asList("Forest Night"));
        songsMap.put("Phase Shift",song);

        song = new ArrayList<>(Arrays.asList("After the End", "Asking Questions", "Barnville", "Beginning of Conflict", "Dreams of Vain", "Eye of Forgiveness", "Final Step",
                "Ghost Town 2", "Hidden Truth", "Hopeful", "Llama in Pajama", "Madness of Linda", "Magic in the Garden", "One Step Closer", "Silly Boy", "The Celebrated Minuet",
                "The Land of the Dead", "The Range"));
        songsMap.put("Rafael Krux",song);

        song = new ArrayList<>(Arrays.asList("Abstract Anxiety"));
        songsMap.put("Severed Personality",song);

        song = new ArrayList<>(Arrays.asList("3 am West End"));
        songsMap.put("statusq",song);

        song = new ArrayList<>(Arrays.asList("Groovin"));
        songsMap.put("Train Robbin' Scoundrels",song);

        song = new ArrayList<>(Arrays.asList("Coy Koi"));
        songsMap.put("Unknown",song);

    }

    private void openMusicPlayerActivity(String artist_song) {
        songsMap = new HashMap<>();
        Intent intent = new Intent(SongsActivity.this, MusicPlayerActivity.class);
        Bundle b = new Bundle();
        b.putString(ARTIST_SONG, artist_name+"-"+artist_song);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    protected void onPause() {

        super.onPause();
        artistSongs.setOnItemClickListener(null);
    }

    @Override
    protected void onResume() {

        super.onResume();
        artistSongs.setClickable(true);
        artistSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                artistSongs.setClickable(false);
                artist_song = ((TextView) view.findViewById(R.id.lines)).getText().toString();
                openMusicPlayerActivity(artist_song);
            }
        });
    }
}

