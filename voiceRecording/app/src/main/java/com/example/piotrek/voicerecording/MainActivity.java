package com.example.piotrek.voicerecording;

import android.app.Activity;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.widget.SeekBar;

import java.io.IOException;



public class MainActivity extends Activity
{
    private static final String LOG_TAG = "MainActivity";
    private static String mFileName = null;

    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton   mPlayButton = null;
    private MediaPlayer   mPlayer = null;

    private SeekBar seekBar = null;
    private Handler seekBarHandler = new Handler();
    private Runnable seekBarRunnable = new Runnable(){

        @Override
        public void run() {
            if (mPlayer != null)
            {
                seekBar.setMax(mPlayer.getDuration()); // mPlater.getDuration() returns time in millis
                seekBar.setProgress(mPlayer.getCurrentPosition());
                seekBarHandler.postDelayed(this, 50);
                timer.setEndTime(mPlayer.getDuration());
            }
        }
    };

    public Timer timer = null;

    public  void onRecord(boolean start) {
        if (start) {
            mPlayButton.setEnabled(false);

            startRecording();
        } else {
            mPlayButton.setEnabled(true);
            stopRecording();
        }
    }

    public  void onPlay(boolean start) {
        if (start) {
            mRecordButton.setEnabled(false);
            startPlaying();
            seekBarHandler.postDelayed(seekBarRunnable,0);
        } else {
            mRecordButton.setEnabled(true);
            stopPlaying();
            seekBarHandler.removeCallbacks(seekBarRunnable);
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();

        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {

        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

//    class RecordButton extends Button {
//        boolean mStartRecording = true;
//
//        OnClickListener clicker = new OnClickListener() {
//            public void onClick(View v) {
//                Log.i(LOG_TAG,"recordButton on click");
//                onRecord(mStartRecording);
//                if (mStartRecording) {
//                    setText("Stop recording");
//                    timer.startTimer();
//
//                } else {
//                    setText("Start recording");
//                    timer.stopTimer();
//                }
//                mStartRecording = !mStartRecording;
//            }
//        };
//
//        public RecordButton(Context ctx) {
//            super(ctx);
//            setText("Start recording");
//            setOnClickListener(clicker);
//        }
//        public RecordButton(Context ctx,AttributeSet attrs, int defStyle) {
//            super(ctx, attrs, defStyle);
//            setText("Start recording");
//            setOnClickListener(clicker);
//
//        }
//        public RecordButton(Context ctx,AttributeSet attrs) {
//            super(ctx, attrs);
//            setText("Start recording");
//            setOnClickListener(clicker);
//        }
//    }

//    class PlayButton extends Button {
//        boolean mStartPlaying = true;
//
//        OnClickListener clicker = new OnClickListener() {
//            public void onClick(View v) {
//                onPlay(mStartPlaying);
//                if (mStartPlaying) {
//                    setText("Stop playing");
//                } else {
//                    setText("Start playing");
//                }
//                mStartPlaying = !mStartPlaying;
//            }
//        };
//
//        public PlayButton(Context ctx, AttributeSet attrs, int defStyle) {
//            super(ctx, attrs, defStyle);
//            setText("Start playing");
//            setOnClickListener(clicker);
//        }
//        public PlayButton(Context ctx, AttributeSet attrs) {
//            super(ctx, attrs);
//            setText("Start playing");
//            setOnClickListener(clicker);
//        }
//        public PlayButton(Context ctx) {
//            super(ctx);
//            setText("Start playing");
//            setOnClickListener(clicker);
//        }
//
//    }

    public MainActivity() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
//
//  Log.i(LOG_TAG,"MainActivity on Create()");
        mPlayButton = (com.example.piotrek.voicerecording.PlayButton)findViewById(R.id.playButton);
        Log.i(LOG_TAG, Boolean.toString(mPlayButton != null));
        mPlayButton.setMainActivity(this);
        mRecordButton = (com.example.piotrek.voicerecording.RecordButton) findViewById(R.id.recordButton);
        Log.i(LOG_TAG, Boolean.toString(mRecordButton != null));
        mRecordButton.setMainActivity(this);
        timer = (com.example.piotrek.voicerecording.Timer) findViewById(R.id.timer);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setMax(1);
        seekBar.setProgress(0);


//
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}