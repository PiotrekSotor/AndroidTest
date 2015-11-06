package com.example.piotrek.voicerecording.WavePackage;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;


/**
 * Created by Piotrek on 2015-10-19.
 */
public class WavePlayer {
    private AudioTrack audioTrack = null;
    private int buffSize = 0;
    private boolean playing = false;
    private SeekBar seekBar = null;
    private Runnable runnablePlayer = null;

    private Handler handlerPlayer = null;
    private Handler handlerSeekBar = null;
    private Runnable runnableSeekBar = new Runnable() {
        @Override
        public void run() {
            if (seekBar != null) {
                if (playing)
                    seekBar.setProgress((int) (WaveRecord.getInstance().getProgress() * 100));
                else
                    seekBar.setProgress(0);
            }
        }
    };

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    public void startPlaying() {
//        File file = new File(WaveActivity.playFileName);
//        file.delete();
        if (seekBar != null)
            seekBar.setMax(100);
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
        if (audioTrack != null && audioTrack.getState() == AudioTrack.STATE_INITIALIZED) {

            runnablePlayer = new Runnable() {
                @Override
                public void run() {
                    if (playing) {
                        seekBar.setMax(100);
//                try {
//                    FileWriter fw = new FileWriter(WaveActivity.playFileName, true);
                        int frameSize = 0x100;

                        if (WaveRecord.getInstance().getAudioTrackEncoding() == AudioFormat.ENCODING_PCM_16BIT)
                            while (!WaveRecord.getInstance().eof() && playing) {
                                float[] dataPackFloat = WaveRecord.getInstance().getDataPack(frameSize);
                                short[] dataPackShort = new short[frameSize];
                                for (int i = 0; i < frameSize; ++i) {
                                    dataPackShort[i] = (short) (dataPackFloat[i] * 0x7fff);
                                    //                            Log.i(this.getClass().getName(), Float.toString(dataPackFloat[i]) + " : " + Short.toString(dataPackShort[i]));
                                    //fw.write(Float.toString(dataPackFloat[i]) + " ; " + Short.toString(dataPackShort[i])+"\n");
                                }
                                audioTrack.write(dataPackShort, 0, dataPackShort.length);
                                seekBar.setProgress((int) (100 * WaveRecord.getInstance().getProgress()));
//                                Log.e(getClass().getName(), "seekBar progress: " + Integer.toString((int) (100 * WaveRecord.getInstance().getProgress())));
                            }
                        handlerPlayer.postDelayed(this, 10);
//                    fw.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                    }
                }
            };
            handlerPlayer = new Handler();
            handlerPlayer.postDelayed(runnablePlayer, 1);
            runnableSeekBar = new Runnable() {
                @Override
                public void run() {
                    if (seekBar != null && false)
                    {
                        seekBar.setMax(100);
                        if (playing)
                        {
                            seekBar.setProgress((int)(WaveRecord.getInstance().getProgress() * 100));
                            Log.i(getClass().getName(),"seekBar progress: " + Integer.toString((int)(WaveRecord.getInstance().getProgress() * 100)));
                        }
                        else
                        {
                            seekBar.setProgress(0);
                        }
                        handlerSeekBar.postDelayed(this,1);
                    }
                    Log.e(getClass().getName(),"notification ... : " + Integer.toString(audioTrack.getPositionNotificationPeriod()));
                }
            };
            handlerSeekBar = new Handler();
            handlerSeekBar.postDelayed(runnableSeekBar, 1);



            playing = true;
            audioTrack.play();
        } else {
            Log.e(this.getClass().getName(), "audioTrack ERROR");
        }
    }

    public void stopPlaying() {
        playing = false;
        audioTrack.stop();
        handlerPlayer.removeCallbacks(runnablePlayer);
        handlerSeekBar.removeCallbacks(runnableSeekBar);
    }


}
