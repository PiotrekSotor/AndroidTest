package com.example.piotrek.voicerecording.WavePackage;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;

;import com.example.piotrek.voicerecording.Tools.Settings;

/**
 * Created by Piotrek on 2015-10-13.
 */
public class WaveRecorder {
    private static final String LOG_TAG = "WaveRecorder";

    private int frequency = 0;
    private int channelConfiguration = 0;
    private int audioEncoding = 0;

    private AudioRecord audioRecord = null;
    private int bufferSize = 0;
    private boolean isRecording = false;
    private int packegeLength = 256;

//    private WaveRecord waveRecord = null;

    private Runnable recordingRunnable = null;
    private Handler recordingHandler;

    public WaveRecorder() {
        frequency = 8000;
        channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
        audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        initRecorder();
    }

    public WaveRecorder(int audioEncoding, int channelConfiguration, int frequency) {
        this.frequency = frequency;
        this.channelConfiguration = channelConfiguration;
        this.audioEncoding = audioEncoding;
        initRecorder();
    }

    private void initRecorder() {
        bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, bufferSize);
    }

    public void startRecording() {
        short[] buffer = new short[bufferSize];

//        waveRecord = new WaveRecord(this.frequency, this.channelConfiguration, this.audioEncoding);
        WaveRecord.getInstance().setAudioTrackSampleRate(Settings.getInstance().getCurSampleRate());
        WaveRecord.getInstance().setAudioTrackChannels(Settings.getInstance().getCurChannelConfiguration());
        WaveRecord.getInstance().setAudioTrackEncoding(Settings.getInstance().getCurAudioEncoding());
        WaveRecord.getInstance().reserInternalDataIndex();
        initRecorder();
        audioRecord.startRecording();

        recordingHandler = new Handler();
        recordingRunnable = new Runnable() {
            @Override
            public void run() {
                if (isRecording ) {
                    if (audioEncoding == AudioFormat.ENCODING_PCM_16BIT) {
                        short[] buffer = new short[256];

                        int numOfSamples = audioRecord.read(buffer, 0, packegeLength);
//                        waveRecord.appendData(buffer);
                        WaveRecord.getInstance().appendData(buffer);
                        recordingHandler.postDelayed(this, 20);
                    }
                    else if (audioEncoding == AudioFormat.ENCODING_PCM_8BIT)
                    {
                        byte[] buffer = new byte[256];

                        int numOfSamples = audioRecord.read(buffer, 0, packegeLength);
//                        waveRecord.appendData(buffer);
                        WaveRecord.getInstance().appendData(buffer);
                        recordingHandler.postDelayed(this, 20);
                    }
                }

            }
        };

        isRecording = true;
        recordingHandler.postDelayed(recordingRunnable, 0);


    }

    public void stopRecording() {
        isRecording = false;
        recordingHandler.removeCallbacks(recordingRunnable);
        audioRecord.release();
    }




}
