package com.example.piotrek.voicerecording.MediaRecordActivity;

import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.widget.SeekBar;

import com.example.piotrek.voicerecording.R;
import com.example.piotrek.voicerecording.Tools.Timer;
import com.example.piotrek.voicerecording.WaveRecordActivity.WaveRecorder;

import java.io.IOException;


public class MainActivity extends Activity {
    public static final String temporaryFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/voiceRecorderTempWave.myFile";
    private static final String LOG_TAG = "MainActivity";
    private static String mFileName = null;

    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton mPlayButton = null;
    private MediaPlayer mPlayer = null;

    private SeekBar seekBar = null;
    private Handler seekBarHandler = new Handler();
    private Runnable seekBarRunnable = new Runnable() {

        @Override
        public void run() {
            if (mPlayer != null) {
                seekBar.setMax(mPlayer.getDuration()); // mPlater.getDuration() returns time in millis
                seekBar.setProgress(mPlayer.getCurrentPosition());
                seekBarHandler.postDelayed(this, 50);
                timer.setEndTime(mPlayer.getDuration());
            }
        }
    };

    private WaveRecorder waveRecorder = null;


    public Timer timer = null;


    public void onRecord(boolean start) {
        if (start) {
            mPlayButton.setEnabled(false);

            startRecording();
        } else {
            mPlayButton.setEnabled(true);
            stopRecording();
        }
    }

    public void onPlay(boolean start) {
        if (start) {
            mRecordButton.setEnabled(false);
            startPlaying();
            seekBarHandler.postDelayed(seekBarRunnable, 0);
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

//

    public MainActivity() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        mPlayButton = (PlayButton) findViewById(R.id.playButton);
        Log.i(LOG_TAG, Boolean.toString(mPlayButton != null));
        mPlayButton.setMainActivity(this);
        mRecordButton = (RecordButton) findViewById(R.id.recordButton);
        Log.i(LOG_TAG, Boolean.toString(mRecordButton != null));
        mRecordButton.setMainActivity(this);
        timer = (Timer) findViewById(R.id.timer);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        waveRecorder = new WaveRecorder();
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