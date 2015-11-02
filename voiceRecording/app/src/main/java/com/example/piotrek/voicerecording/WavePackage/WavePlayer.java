package com.example.piotrek.voicerecording.WavePackage;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;


/**
 * Created by Piotrek on 2015-10-19.
 */
public class WavePlayer {
    private AudioTrack audioTrack = null;
    private int buffSize = 0;
    private boolean playing = false;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (playing) {
                try {
                    FileWriter fw = new FileWriter(WaveActivity.playFileName, true);
                    int frameSize = 0x10000;

                    if (WaveRecord.getInstance().getAudioTrackEncoding() == AudioFormat.ENCODING_PCM_16BIT)
                        while (!WaveRecord.getInstance().eof()) {
                            float[] dataPackFloat = WaveRecord.getInstance().getDataPack(frameSize);
                            short[] dataPackShort = new short[frameSize];
                            for (int i = 0; i < frameSize; ++i) {
                                dataPackShort[i] = (short) (dataPackFloat[i] * 0x7fff);
    //                            Log.i(this.getClass().getName(), Float.toString(dataPackFloat[i]) + " : " + Short.toString(dataPackShort[i]));
                                //fw.write(Float.toString(dataPackFloat[i]) + " ; " + Short.toString(dataPackShort[i])+"\n");
                            }

                            audioTrack.write(dataPackShort, 0, dataPackShort.length);
                            handler.postDelayed(this, 1);

                        }
                    else if (WaveRecord.getInstance().getAudioTrackEncoding() == AudioFormat.ENCODING_PCM_8BIT)
                        while (!WaveRecord.getInstance().eof()) {
                            float[] dataPackFloat = WaveRecord.getInstance().getDataPack(frameSize);
                            byte[] dataPackByte = new byte[frameSize];
                            for (int i = 0; i < frameSize; ++i) {
                                dataPackByte[i] = (byte) (dataPackFloat[i] * 0x7f);
                            }

                            audioTrack.write(dataPackByte, 0, dataPackByte.length);
                            handler.postDelayed(this, 1);

                        }
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    ;
    private Handler handler = new Handler();

    public void startPlaying() {
        File file = new File(WaveActivity.playFileName);
        file.delete();
        WaveRecord.getInstance().reserInternalDataIndex();
        Log.i(this.getClass().getName(), Integer.toString(WaveRecord.getInstance().getAudioTrackSampleRate()) + "  " + Integer.toString(WaveRecord.getInstance().getAudioTrackChannels()) + "  " + Integer.toString(WaveRecord.getInstance().getAudioTrackEncoding()));
        buffSize = AudioTrack.getMinBufferSize(WaveRecord.getInstance().getAudioTrackSampleRate(),
                WaveRecord.getInstance().getAudioTrackChannels(),
                WaveRecord.getInstance().getAudioTrackEncoding());
//        try{
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                WaveRecord.getInstance().getAudioTrackSampleRate(),
                WaveRecord.getInstance().getAudioTrackChannels(),
                WaveRecord.getInstance().getAudioTrackEncoding(),
                buffSize,
                AudioTrack.MODE_STREAM);
//        }
//        catch (IllegalArgumentException e)
//        {
//            Log.e(getClass().getName(),"getAudioTrackSampleRate() : "+Integer.toString(WaveRecord.getInstance().getAudioTrackSampleRate()));
//            Log.e(getClass().getName(),"getAudioTrackChannels() : "+Integer.toString(WaveRecord.getInstance().getAudioTrackChannels()));
//            Log.e(getClass().getName(),"getAudioTrackEncoding() : "+Integer.toString(WaveRecord.getInstance().getAudioTrackEncoding()));
//            Log.e(getClass().getName(),"AudioFormat.CHANNEL_OUT_MONO : "+Integer.toString(AudioFormat.CHANNEL_OUT_MONO));
//        }
//        audioTrack.setStereoVolume(audioTrack.getMaxVolume(), audioTrack.getMaxVolume());
        if (audioTrack != null && audioTrack.getState() == AudioTrack.STATE_INITIALIZED) {
            playing = true;
            handler.postDelayed(runnable, 10);
            audioTrack.play();
        } else {
            Log.e(this.getClass().getName(), "audioTrack ERROR");
        }
    }

    public void stopPlaying() {
        playing = false;
        audioTrack.stop();
        handler.removeCallbacks(runnable);
    }


}
