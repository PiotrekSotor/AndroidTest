package com.example.piotrek.voicerecording.WaveRecordActivity;

import android.media.AudioFormat;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by Piotrek on 2015-10-13.
 */
public class WaveRecord implements Serializable {
    private int frequency = 0;
    private int channelConfiguration = 0;
    private int audioEncoding = 0;
    private short[] data = null;

    private int internalDataIndex = 0;


    public void reserInternalDataIndex() {
        this.internalDataIndex = 0;
    }

    public void appendData(short[] newData) {
        short[] temp = new short[data.length + newData.length];
        System.arraycopy(data, 0, temp, 0, data.length);
        System.arraycopy(newData, 0, temp, data.length, newData.length);
        internalDataIndex = temp.length;
        data = temp.clone();
    }

    public short[] getDataPack(int numOfBytes) {
        short[] result = new short[numOfBytes];
        if (internalDataIndex + numOfBytes < data.length) {
            System.arraycopy(data, internalDataIndex, result, 0, numOfBytes);
            internalDataIndex += numOfBytes;
        } else {
            Arrays.fill(result, (byte) 0);
            System.arraycopy(data, internalDataIndex, result, 0, data.length - internalDataIndex);
            internalDataIndex = data.length;
        }
        return result;

    }

    public WaveRecord() {
        frequency = 8000;
        channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
        audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        data = null;
        internalDataIndex=0;
    }

    public WaveRecord(int frequency, int channelConfiguration, int audioEncoding) {
        this.frequency = frequency;
        this.channelConfiguration = channelConfiguration;
        this.audioEncoding = audioEncoding;
    }

    public void clear() {
        frequency = 8000;
        channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
        audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        data = null;
        internalDataIndex=0;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getChannelConfiguration() {
        return channelConfiguration;
    }

    public void setChannelConfiguration(int channelConfiguration) {
        this.channelConfiguration = channelConfiguration;
    }

    public int getAudioEncoding() {
        return audioEncoding;
    }

    public void setAudioEncoding(int audioEncoding) {
        this.audioEncoding = audioEncoding;
    }


}
