package com.example.piotrek.voicerecording.WavePackage;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

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
    private int packageLength = 256;

//    private WaveRecord waveRecord = null;

    private Runnable recordingRunnable = null;
    private Handler recordingHandler;

    public WaveRecorder() {
        frequency = 8000;
        channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
        audioEncoding = AudioFormat.ENCODING_PCM_8BIT;

        initRecorder();
    }

    public WaveRecorder(int audioEncoding, int channelConfiguration, int frequency) {
        this.frequency = frequency;
        this.channelConfiguration = channelConfiguration;
        this.audioEncoding = audioEncoding;
        initRecorder();
    }

    private void initRecorder() {
//        frequency = Settings.getInstance().getCurSampleRate();
//        channelConfiguration = channelConfigurationOutToIn(Settings.getInstance().getCurChannelConfiguration());
//        audioEncoding = Settings.getInstance().getCurAudioEncoding();

        Log.i(getClass().getName(),"freq: " + Integer.toString(frequency) + "  channel: " + Integer.toString(channelConfiguration) + "  encoding: " +Integer.toString(audioEncoding));

        bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
        Log.i(getClass().getName(),"bufferSize: " + Integer.toString(bufferSize));
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
                        short[] buffer = new short[packageLength];

                        int numOfSamples = audioRecord.read(buffer, 0, packageLength);
//                        buffer = generateWaveShorts(4000);
//                        waveRecord.appendData(buffer);
                        WaveRecord.getInstance().appendData(buffer);
                        recordingHandler.postDelayed(this, 1);
                    }
                    else if (audioEncoding == AudioFormat.ENCODING_PCM_8BIT)
                    {
                        byte[] buffer = new byte[packageLength];

                        int numOfSamples = audioRecord.read(buffer, 0, packageLength);
//                        buffer = generateWaveBytes(4000);
//                        waveRecord.appendData(buffer);
                        WaveRecord.getInstance().appendData(buffer);
                        recordingHandler.postDelayed(this, 1);
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
        WaveRecord.getInstance().saveInFile();
//        Toast.makeText(, "File saved",Toast.LENGTH_SHORT).show();
        Log.e(getClass().getName(), "File saved");
        audioRecord.release();
    }

    public byte[] generateWaveBytes(int waveFrequency)
    {

        byte[] result = new byte[packageLength];
        for (int i=0;i<packageLength;++i)
        {
            result[i] = (byte)(0x7f * Math.sin(2*Math.PI*waveFrequency*(float)i/packageLength*frequency));
        }
        return result;
    }
    public short[] generateWaveShorts(int waveFrequency)
    {

        short[] result = new short[packageLength];
        for (int i=0;i<packageLength;++i)
        {
            result[i] = (short)(0x7fff * Math.sin(2*Math.PI*waveFrequency*(float)i/packageLength*frequency));
        }
        return result;
    }

    public static int channelConfigurationOutToIn(int channelConfigurationOut)
    {
        int resutlt = 0;
        switch(channelConfigurationOut)
        {
            case AudioFormat.CHANNEL_OUT_MONO:
                resutlt = AudioFormat.CHANNEL_IN_MONO;
                break;
            case AudioFormat.CHANNEL_OUT_STEREO:
                resutlt = AudioFormat.CHANNEL_IN_STEREO;
                break;
        }
        return resutlt;


    }


}
