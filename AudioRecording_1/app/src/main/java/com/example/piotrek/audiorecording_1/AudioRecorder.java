package com.example.piotrek.audiorecording_1;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Piotrek on 2015-08-18.
 */
public class AudioRecorder {
    private AudioRecord mAudioRecord;
    private int mMinBufferSize;
    private boolean mDoRecord = false;
    public AudioRecorder()
    {
        setmMinBufferSize(AudioTrack.getMinBufferSize(16000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT));
        setmAudioRecord(new AudioRecord(MediaRecorder.AudioSource.DEFAULT, 16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, getmMinBufferSize() *2));

    }

    public void record(String fileName)
    {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/recordsound");
        if (file.exists())
        {
            file.delete();
        }
        try {
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);

            short[] buffer = new short[getmMinBufferSize()];

            getmAudioRecord().startRecording();
            while (!ismDoRecord())
            {
                int bufferReadResult = getmAudioRecord().read(buffer, 0, getmMinBufferSize());
                for (int i=0;i<bufferReadResult;++i)
                {
                    dos.write(buffer[i]);
                }
            }

            getmAudioRecord().stop();
            dos.flush();
            dos.close();
            bos.close();
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void startRecording()
    {
        setmDoRecord(true);
    }

    public void stopRecording()
    {
        setmDoRecord(false);
    }

    public AudioRecord getmAudioRecord() {
        return mAudioRecord;
    }

    public void setmAudioRecord(AudioRecord mAudioRecord) {
        this.mAudioRecord = mAudioRecord;
    }

    public int getmMinBufferSize() {
        return mMinBufferSize;
    }

    public void setmMinBufferSize(int mMinBufferSize) {
        this.mMinBufferSize = mMinBufferSize;
    }

    public boolean ismDoRecord() {
        return mDoRecord;
    }

    public void setmDoRecord(boolean mDoRecord) {
        this.mDoRecord = mDoRecord;
    }
}
