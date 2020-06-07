package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.impl.Hash;
import com.example.myapplication.impl.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayerActivity extends AppCompatActivity {

    final String ARTIST_SONG = "artist_song";
    Button playBtn;
    TextView elapsedTimeLabel;

    String artist_song;
    MediaPlayer mediaPlayer;
    Timer T;
    int currentTime;
    TextView songTxt;
    int min;
    int sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.player);
        getSupportActionBar().hide();


        playBtn = (Button) findViewById(R.id.playBtn);
        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
//        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);
        songTxt = findViewById(R.id.songText);
//        positionBar = findViewById(R.id.positionBar);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            artist_song = b.getString(ARTIST_SONG);
        }

        MyTask asyncTask = new MyTask(MusicPlayerActivity.this);
        asyncTask.execute(artist_song);

        currentTime=0;





    }

    private class MyTask extends AsyncTask<String, Integer, List<Value>> {

        private final static String BROKER_IP = "10.0.2.2";
        private final static int SUB_ID = 1;
        ProgressDialog progressDialog ;
        MusicPlayerActivity musicPlayerActivity;
        String songInfo;
        int currentChunk=0;
        int countMin = 0;
        int countSec =0;

        public MyTask(MusicPlayerActivity musicPlayerActivity) {
            this.musicPlayerActivity=musicPlayerActivity;
            progressDialog = new ProgressDialog(musicPlayerActivity);
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
        protected List<Value> doInBackground(String... params) {

            String input = params[0];

            SubscriberNode subscriberNode = new SubscriberNode(SUB_ID, input, BROKER_IP, 7999 + Hash.getBroker(input.split("-")[0]));
            subscriberNode.connect();

            songInfo =subscriberNode.getV().getMusicFile().getTrackName()+"|"+subscriberNode.getV().getMusicFile().getAlbumInfo()+"|" +subscriberNode.getV().getMusicFile().getArtistName()+"|"+subscriberNode.getV().getMusicFile().getGenre();
            return subscriberNode.getConsumedMessages();
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(final List<Value> result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            songTxt.setText(songInfo);
            final int countChunks=result.size();
            if(countChunks>0){
                playMp3(Base64.decode(result.get(currentChunk).getMusicFile().getMusicFileExtract(), 0));
                currentChunk++;
            }
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (currentChunk<countChunks) {
                        mediaPlayer.release();
                        playMp3(Base64.decode(result.get(currentChunk).getMusicFile().getMusicFileExtract(), 0));
                        currentChunk++;
                    }
                    if (currentChunk>=countChunks){
                        playBtn.setBackgroundResource(R.drawable.play);
                        T.cancel();
                        setTime(countMin,countSec);
                    }
                }
            });



            T=new Timer();
            T.scheduleAtFixedRate(new TimerTask() {
                int countMin = 0;
                int countSec =0;
                @Override
                public void run() {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            saveTime(countMin, countSec);
                            if (countSec<10) elapsedTimeLabel.setText(countMin+":0"+countSec);
                            else elapsedTimeLabel.setText(countMin+":"+countSec);
                            if (countSec<59) {
                                countSec++;
                            } else {
                                countMin++;
                                countSec=0;
                            }
                        }
                    });
                }
            }, 10, 1000);

        }

        private void playMp3(byte[] mp3SoundByteArray) {
            try {
                mediaPlayer = new MediaPlayer();
                File tempMp3 = File.createTempFile("kurchina", "mp3", getCacheDir());
                tempMp3.deleteOnExit();
                FileOutputStream fos = new FileOutputStream(tempMp3);
                fos.write(mp3SoundByteArray);
                fos.close();

                mediaPlayer.reset();

                FileInputStream fis = new FileInputStream(tempMp3);
                mediaPlayer.setDataSource(fis.getFD());

                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException ex) {
                String s = ex.toString();
                ex.printStackTrace();
            }
        }



    }


    public void playBtnClick(View view) throws InterruptedException {
        if (!mediaPlayer.isPlaying()) {
            // Stopping
            mediaPlayer.start();
            T= new Timer();
            T.scheduleAtFixedRate(new TimerTask() {
                int countMin = min;
                int countSec = sec;
                @Override
                public void run() {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            saveTime(countMin, countSec);
                            if (countSec<10) elapsedTimeLabel.setText(countMin+":0"+countSec);
                            else elapsedTimeLabel.setText(countMin+":"+countSec);
                            if (countSec<59) {
                                countSec++;
                            } else {
                                countMin++;
                                countSec=0;
                            }
                        }
                    });
                }
            }, 10, 1000);
            playBtn.setBackgroundResource(R.drawable.stop);


        } else {
            // Playing
            mediaPlayer.pause();
            T.cancel();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            currentTime=mediaPlayer.getCurrentPosition();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mediaPlayer!=null){
            mediaPlayer.seekTo(currentTime);
            mediaPlayer.start();
        }
    }

    private void saveTime(int countMin, int countSec){
        min = countMin;
        sec = countSec;
    }

    private void setTime(int countMin, int countSec){
        min = 0;
        sec = 0;
    }
}
