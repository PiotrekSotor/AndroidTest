package com.example.piotrek.voicerecording.WavePackage;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.util.Log;

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
                while (!WaveRecord.getInstance().eof()) {
                    float[] dataPackFloat = WaveRecord.getInstance().getDataPack(0x100);
                    short[] dataPackShort = new short[0x100];
                    for (int i = 0; i < 0x100; ++i) {
                        dataPackShort[i] = (short) (dataPackFloat[i] * 25600.0f);
//                        Log.i(this.getClass().getName(),Float.toString(dataPackFloat[i]) + " : " + Short.toString(dataPackShort[i]));
                    }
                    audioTrack.write(dataPackShort, 0, dataPackShort.length);
                    handler.postDelayed(this, 10);

                }
            }
        }
    };
    ;
    private Handler handler = new Handler();

    public void startPlaying() {
        WaveRecord.getInstance().reserInternalDataIndex();
        Log.i(this.getClass().getName(), Integer.toString(WaveRecord.getInstance().getAudioTrackSampleRate())+ "  " + Integer.toString(WaveRecord.getInstance().getAudioTrackChannels())+ "  " + Integer.toString(WaveRecord.getInstance().getAudioTrackEncoding()));
        buffSize = AudioTrack.getMinBufferSize(WaveRecord.getInstance().getAudioTrackSampleRate(),
                WaveRecord.getInstance().getAudioTrackChannels(),
                WaveRecord.getInstance().getAudioTrackEncoding());
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                WaveRecord.getInstance().getAudioTrackSampleRate(),
                WaveRecord.getInstance().getAudioTrackChannels(),
                WaveRecord.getInstance().getAudioTrackEncoding(),
                buffSize,
                AudioTrack.MODE_STREAM);
        audioTrack.setStereoVolume(audioTrack.getMaxVolume(), audioTrack.getMaxVolume());
        if (audioTrack != null && audioTrack.getState() == AudioTrack.STATE_INITIALIZED) {
            handler.postDelayed(runnable, 0);
            playing= true;
            audioTrack.play();
        } else {
            Log.e(this.getClass().getName(), "audioTrack ERROR");
        }
    }
    public void stopPlaying()
    {
        playing = false;
        handler.removeCallbacks(runnable);
    }


}
