package com.example.myapplication.impl;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.SubscriberNode;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.player);

        playBtn = (Button) findViewById(R.id.playBtn);
        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            artist_song = b.getString(ARTIST_SONG);
        }

        getSupportActionBar().setTitle(artist_song);

        MyTask asyncTask = new MyTask(MusicPlayerActivity.this);
        asyncTask.execute(artist_song);

        //player
        mp = MediaPlayer.create(this, R.raw.revelations);
        mp.setLooping(true);
//        mp.seekTo(0);
//        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();
        mp.start();


        // Position Bar
//        positionBar = (SeekBar) findViewById(R.id.positionBar);
//        positionBar.setMax(totalTime);
//        positionBar.setOnSeekBarChangeListener(
//                new SeekBar.OnSeekBarChangeListener() {
//                    @Override
//                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                        if (fromUser) {
//                            mp.seekTo(progress);
//                            positionBar.setProgress(progress);
//                        }
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

        // Thread (Update positionBar & timeLabel)
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
        ProgressDialog progressDialog ;

        public MyTask(MusicPlayerActivity activity) {
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
            // Do things like hide the progress bar or change a TextView
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
//
//    public void playBtnClick(View view) {
//
//        if (!mp.isPlaying()) {
//            // Stopping
//            mp.start();
//            playBtn.setBackgroundResource(R.drawable.stop);
//
//        } else {
//            // Playing
//            mp.pause();
//            playBtn.setBackgroundResource(R.drawable.play);
//        }
//
//    }
}
