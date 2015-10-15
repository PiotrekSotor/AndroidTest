package com.example.piotrek.voicerecording.WavePackage;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import com.example.piotrek.voicerecording.R;
import com.example.piotrek.voicerecording.Tools.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class WaveActivity extends Activity {

    public static String recordFileName;
    private static final String LOG_TAG = "WaveActivity";

    private boolean mStartRecording = false;
    private boolean mStartPlaying = false;
    private RecordButton recordButton = null;
    private PlayButton playButton = null;
    private Timer timer = null;
    private SeekBar seekBar = null;
    private MediaPlayer mediaPlayer = null;

    private WaveRecorder waveRecorder = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);

        recordFileNameInit();

        playButton = (PlayButton)findViewById(R.id.WP_playButton);
        recordButton = (RecordButton)findViewById(R.id.WP_recordButton);
        timer = (Timer) findViewById(R.id.WP_timer);
        seekBar = (SeekBar) findViewById(R.id.WP_seekBar);

        playButton.setWaveActivity(this);
        recordButton.setWaveActivity(this);

    }

    public void onRecord()
    {
        mStartRecording = !mStartRecording;
        if (mStartRecording)
        {
            playButton.setEnabled(false);
            startRecording();
        }
        else
        {
            playButton.setEnabled(true);
            stopRecording();
        }
    }

    public void onPlay()
    {
        mStartPlaying = !mStartPlaying;
        if (mStartPlaying)
        {
            recordButton.setEnabled(false);
            startPlaying();
        }
        else
        {
            recordButton.setEnabled(true);
            stopPlaying();
        }
    }

    public void startRecording()
    {
        waveRecorder = new WaveRecorder();
        waveRecorder.startRecording();
    }
    public void stopRecording()
    {
        if (waveRecorder != null)
        {
            waveRecorder.stopRecording();
        }

    }
    public void startPlaying()
    {
        mediaPlayer = new MediaPlayer();
        FileInputStream fileInputStream = null;
        //perform modulation

        try {
            fileInputStream = new FileInputStream(recordFileName);
            mediaPlayer.setDataSource(fileInputStream.getFD());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPlaying()
    {

    }

    private void recordFileNameInit()
    {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (isSDPresent)
        {
            Log.i(LOG_TAG, "SD is present");
            recordFileName = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        }
        else
        {
            Log.e(LOG_TAG, "SD is not present");
            recordFileName= getFilesDir().getAbsolutePath().toString();

        }

        recordFileName += "/voicerecording_wave.myfile";
        Log.i(LOG_TAG,"recordFileName: "+recordFileName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wave, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean ismStartRecording() {
        return mStartRecording;
    }

    public void setmStartRecording(boolean mStartRecording) {
        this.mStartRecording = mStartRecording;
    }
}
