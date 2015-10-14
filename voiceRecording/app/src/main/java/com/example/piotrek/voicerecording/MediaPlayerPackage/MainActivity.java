package com.example.piotrek.voicerecording.MediaPlayerPackage;

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
import com.example.piotrek.voicerecording.WavePackage.WaveRecorder;

import java.io.FileInputStream;
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
            FileInputStream fileInputStream = new FileInputStream(mFileName);
            mPlayer.setDataSource(fileInputStream.getFD());
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
            mRecorder.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
            Log.i(LOG_TAG, "file path: " + mFileName);
        } catch (RuntimeException e) {
            Log.e(LOG_TAG, "start() failed");
        }


    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

//    public MainActivity() {
//        super();
//        mFileName = "";
//        if (Environment.getExternalStorageDirectory() != null)
//            if (Environment.getExternalStorageDirectory().getAbsolutePath() != null)
//                    mFileName =  Environment.getExternalStorageDirectory().getAbsolutePath();
//            else
//                Log.e(LOG_TAG,"getFilesDir = null");
//        else
//            Log.e(LOG_TAG,"getBaseContext = null");
//        File f = new File(mFileName);
//        if (f.isDirectory())
//        {
//            Log.i(LOG_TAG,"katalog istnieje");
//            Log.i(LOG_TAG,"exist: "+f.exists() );
//        }
//
//        else
//        {
//            Log.e(LOG_TAG,"katalog nie istnieje");
//            Log.e(LOG_TAG, "exist: " + f.exists());
//        }
//        if (MainActivity.this.getBaseContext() == null)
//            Log.e(LOG_TAG,"WAT???");
//        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
//        if (isSDPresent)
//        {
//            Log.i(LOG_TAG,"SD is present");
//        }
//        else
//        {
//            Log.e(LOG_TAG, "SD is not present");
//            Log.i(LOG_TAG, getFilesDir().toString());
//
//        }
//
//        mFileName += "/audiorecordtest.3gp";
//
//    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mFileNameInit();




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

    private void mFileNameInit() {
        mFileName = "";
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (isSDPresent)
        {
            Log.i(LOG_TAG,"SD is present");
            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        }
        else
        {
            Log.e(LOG_TAG, "SD is not present");
            mFileName = getFilesDir().getAbsolutePath().toString();

        }

        mFileName += "/audiorecordtest.3gp";
        Log.i(LOG_TAG,"mFileName: "+mFileName);

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