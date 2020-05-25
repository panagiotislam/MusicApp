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
                artistSongs.setClickable(false);
                String artist_song = ((TextView) view.findViewById(R.id.lines)).getText().toString();
//                MyTask asyncTask = new MyTask(SongsActivity.this);
//                asyncTask.execute(artist_name+"-"+artist_song);
                Intent intent = new Intent(SongsActivity.this, MusicPlayerActivity.class);
                Bundle b = new Bundle();
                b.putString(ARTIST_SONG, artist_name+"-"+artist_song);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }


    private void initializeSongs() {
        ArrayList<String> song = new ArrayList<String>(
                Arrays.asList("Ambient Bongos", "Apex", "Be Chillin", "Be Jammin", "Blacksmith", "Bonfire", "Brothers Unite",
                        "Burt's Requiem", "Celebration", "Chronos", "Consecrated Ground", "Construction", "Creepy Hallow", "Favorite", "Fireworks", "Foam Rubber",
                        "Hippety Hop", "Hor Hor", "Horizon Flare", "Journey of Hope", "Jökull (Metal Version)", "Le Baguette","Marked","Nightmare","Nomadic Sunset",
                        "Novus Initium", "Putin's Lullaby", "Silent Night (Unholy Night)", "Silly Intro", "The Crown", "The Lagoon", "The Story", "Uberpunch"));
        songs.put("Alexander Nakarada",song);

        song = new ArrayList<>(Arrays.asList("null"));
        songs.put("Anonymous for Good Reasons",song);

        song = new ArrayList<>(Arrays.asList("Footsteps in the Attic"));
        songs.put("Arthur Fordsworthy",song);

        song = new ArrayList<>(Arrays.asList("Quick Metal Riff 1"));
        songs.put("Brett VanDonsel",song);

        song = new ArrayList<>(Arrays.asList("null","Groovin"));
        songs.put("Brian Boyko",song);

        song = new ArrayList<>(Arrays.asList("A Waltz For Naseem", "Brandenburg Concerto III, Alle", "Brandenburg Concerto III, Allegro II"));
        songs.put("dogsounds",song);

        song = new ArrayList<>(Arrays.asList("Goldcrest"));
        songs.put("James Anderson",song);

        song = new ArrayList<>(Arrays.asList("Landra's Dream", "River Meditation"));
        songs.put("Jason Shaw",song);

        song = new ArrayList<>(Arrays.asList("", "A Surprising Encounter", "Alternative Clock Dimension", "Amazing Grace", "Backbeat", "Baltic Levity", "Beat One",
                "Bit Bit Loop", "Bollywood Groove", "Breaking Bollywood", "City Sunshine", "Connecting Rainbows", "Dancing at the Inn", "Dark Hallway", "Driving Concern",
                "Drunken Party", "Evil Incoming", "Fenster's Explanation", "Forest Frolic Loop", "Funky Energy Loop", "Funshine", "Goodnightmare", "Horns", "Icicles Melting",
                "Improved Ice Cream Track", "Infinite Peace", "Infinite Wonder", "Joey's Song", "Lukewarm Banjo","Meditating Beat", "Midnight in the Green House",
                "Night in the Castle", "Organ Filler", "Painful Disorientation", "Painting Room", "Pickled Pink", "Sad Drunken Party", "Satin Danger", "The Britons"
                , "Village Tarantella", "Windy Old Weather", "Wisdom in the Sun", "Witch Waltx","null"));
        songs.put("Kevin MacLeod",song);

        song = new ArrayList<>(Arrays.asList("A good bass for gambling", "Bleu", "Champ de tournesol", "La Citadelle"));
        songs.put("Komiku",song);

        song = new ArrayList<>(Arrays.asList("Motions"));
        songs.put("Orchestralis",song);

        song = new ArrayList<>(Arrays.asList("Forest Night"));
        songs.put("Phase Shift",song);

        song = new ArrayList<>(Arrays.asList("After the End", "Asking Questions", "Barnville", "Beginning of Conflict", "Dreams of Vain", "Eye of Forgiveness", "Final Step",
                "Ghost Town 2", "Hidden Truth", "Hopeful", "Llama in Pajama", "Madness of Linda", "Magic in the Garden", "One Step Closer", "Silly Boy", "Stereotype News2",
                "The Celebrated Minuet", "The Drama", "The Land of the Dead", "The Range"));
        songs.put("Rafael Krux",song);

        song = new ArrayList<>(Arrays.asList("Abstract Anxiety"));
        songs.put("Severed Personality",song);

        song = new ArrayList<>(Arrays.asList("3 am West End"));
        songs.put("statusq",song);

        song = new ArrayList<>(Arrays.asList("Groovin"));
        songs.put("Train Robbin' Scoundrels",song);

        song = new ArrayList<>(Arrays.asList("Coy Koi"));
        songs.put("Unknown",song);

    }

    @Override
    protected void onResume() {
        super.onResume();
        artistSongs.setClickable(true);
    }
}

