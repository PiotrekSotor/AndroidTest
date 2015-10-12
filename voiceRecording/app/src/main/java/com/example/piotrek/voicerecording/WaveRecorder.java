package com.example.piotrek.voicerecording;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.provider.MediaStore;

/**
 * Created by Piotrek on 2015-10-13.
 */
public class WaveRecorder {
    private static final String LOG_TAG = "WaveRecorder";
    private static final String temporaryFileName = "voiceRecorderTempWave.myFile";
    private int frequency = 0;
    private int channelConfiguration = 0;
    private int audioEncoding = 0;

    private AudioRecord recordAudio = null;
    private int bufferSize = 0;

    public WaveRecorder ()
    {
        frequency = 8000;
        channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
        audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        initRecorder();
    }

    public  WaveRecorder(int audioEncoding, int channelConfiguration, int frequency)
    {
        this.frequency = frequency;
        this.channelConfiguration = channelConfiguration;
        this.audioEncoding = audioEncoding;
        initRecorder();
    }

    private void initRecorder()
    {
        bufferSize = AudioRecord.getMinBufferSize(frequency,channelConfiguration,audioEncoding);
        recordAudio = new AudioRecord(MediaRecorder.AudioSource.MIC,frequency,channelConfiguration,audioEncoding, bufferSize);
    }

    public void startRecording()
    {
        short[] buffer = new short[bufferSize];

    }

    public void stopRecording()
    {

    }

}
