package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.myapplication.impl.Hash;
import com.example.myapplication.impl.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity {

    final String ARTIST_SONG = "artist_song";
    Button playBtn;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    String artist_song;
    SeekBar positionBar;
    SeekBar volumeBar;
    MediaPlayer mp;
    int totalTime;
    int currentTime;
    TextView songTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.player);
        getSupportActionBar().hide();


        playBtn = (Button) findViewById(R.id.playBtn);
        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);
        songTxt = findViewById(R.id.songText);
        positionBar = findViewById(R.id.positionBar);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            artist_song = b.getString(ARTIST_SONG);
        }

        MyTask asyncTask = new MyTask(MusicPlayerActivity.this);
        asyncTask.execute(artist_song);

        currentTime=0;

        //player
//        mp = MediaPlayer.create(this, R.raw.tick_tock);
//        mp.setLooping(true);
//        mp.seekTo(0);
//        totalTime = mp.getDuration();
//        mp.start();


        // Position Bar
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mp.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );


        // Volume Bar
//        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
//        volumeBar.setOnSeekBarChangeListener(
//                new SeekBar.OnSeekBarChangeListener() {
//                    @Override
//                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                        float volumeNum = progress / 100f;
//                        mp.setVolume(volumeNum, volumeNum);
//                    }
//
//                    @Override
//                    public void onStartTrackingTouch(SeekBar seekBar) {
//
//                    }
//
//                    @Override
//                    public void onStopTrackingTouch(SeekBar seekBar) {
//
//                    }
//                }
//        );

//         Thread (Update positionBar & timeLabel)
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (mp != null) {
//                    try {
//                        Message msg = new Message();
//                        msg.what = mp.getCurrentPosition();
//                        handler.sendMessage(msg);
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {}
//                }
//            }
//        }).start();



    }

    private class MyTask extends AsyncTask<String, Integer, Value> {

        private final static String BROKER_IP = "10.0.2.2";
        private final static int SUB_ID = 1;
        private MediaPlayer mediaPlayer = new MediaPlayer();
        ProgressDialog progressDialog ;
        MusicPlayerActivity musicPlayerActivity;

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
            songTxt.setText(result.getMusicFile().getAlbumInfo()+"|" +result.getMusicFile().getArtistName()+"|"+result.getMusicFile().getGenre());
            String p = result.getMusicFile().getMusicFileExtract();
            playMp3(Base64.decode(p, 0));
            // Do things like hide the progress bar or change a TextView
        }

        private void playMp3(byte[] mp3SoundByteArray) {
            try {
// create temp file that will hold byte array
                File tempMp3 = File.createTempFile("kurchina", "mp3", getCacheDir());
                tempMp3.deleteOnExit();
                FileOutputStream fos = new FileOutputStream(tempMp3);
                fos.write(mp3SoundByteArray);
                fos.close();

// resetting mediaplayer instance to evade problems
                mediaPlayer.reset();

// In case you run into issues with threading consider new instance like:
// MediaPlayer mediaPlayer = new MediaPlayer();

// Tried passing path directly, but kept getting
// "Prepare failed.: status=0x1"
// so using file descriptor instead
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

//    private Handler handler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            int currentPosition = msg.what;
//            // Update positionBar.
//            positionBar.setProgress(currentPosition);
//
//            // Update Labels.
//            String elapsedTime = createTimeLabel(currentPosition);
//            elapsedTimeLabel.setText(elapsedTime);
//
//            String remainingTime = createTimeLabel(totalTime-currentPosition);
//            remainingTimeLabel.setText("- " + remainingTime);
//        }
//    };
//
//    public String createTimeLabel(int time) {
//        String timeLabel = "";
//        int min = time / 1000 / 60;
//        int sec = time / 1000 % 60;
//
//        timeLabel = min + ":";
//        if (sec < 10) timeLabel += "0";
//        timeLabel += sec;
//
//        return timeLabel;
//    }

    public void playBtnClick(View view) {

        if (!mp.isPlaying()) {
            // Stopping
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);

        } else {
            // Playing
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(mp!=null){
            mp.pause();
            currentTime=mp.getCurrentPosition();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mp!=null){
            mp.seekTo(currentTime);
            mp.start();
        }
    }

    @Override
    public void onBackPressed() {
        if(mp!=null) {
            mp.stop();
        }
    }
}
