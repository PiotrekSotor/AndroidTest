package com.example.piotrek.voicerecording.WavePackage;

import android.media.AudioFormat;
import android.util.Log;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Piotrek on 2015-10-13.
 */
public class WaveRecord implements Serializable {


    private static WaveRecord instance = null;

    private int audioTrackSampleRate = 0;
    private int audioTrackChannels = 0;
    private int audioTrackEncoding = 0;
    private float[] data = null;

    private int internalDataIndex = 0;


    public static WaveRecord getInstance() {
        if (instance == null)
            instance = new WaveRecord();
        return instance;
    }


    public void reserInternalDataIndex() {
        this.internalDataIndex = 0;
    }

    public void appendData(byte[] newData) {
        float[] temp = new float[data.length + newData.length];
        System.arraycopy(data, 0, temp, 0, data.length);
//        System.arraycopy(newData, 0, temp, dataShort.length, newData.length);
        for (int i = 0; i < newData.length; ++i)
            temp[data.length + i] = (float) newData[i] / 0xff;
        internalDataIndex = temp.length;
        data = temp.clone();
    }

    public void appendData(short[] newData) {
        if (data == null)
            data = new float[0];
        float[] temp = new float[data.length + newData.length];
        System.arraycopy(data, 0, temp, 0, data.length);
        for (int i = 0; i < newData.length; ++i)
            temp[i + data.length] = (float) newData[i] / 0xffff;
//        System.arraycopy(newData, 0, temp, dataShort.length, newData.length);
        internalDataIndex = temp.length;
        data = temp.clone();
    }

    public void appendData(int[] newData) {
        if (data == null)
            data = new float[0];
        float[] temp = new float[data.length + newData.length];
        System.arraycopy(data, 0, temp, 0, data.length);
        for (int i = 0; i < newData.length; ++i)
            temp[i + data.length] = (float) newData[i] / 0xffffffff;
        internalDataIndex = temp.length;
        data = temp.clone();
    }

    /**
     * @param newData - nowy fragment nagrania do podmienienia
     * @param offset  - index początku fragmentu podmienianego
     */
    public void replaceDataPack(float[] newData, int offset) {
        if (data != null && newData != null) {
            if (offset < data.length) {
                int size = newData.length;
                if (offset + newData.length > data.length)
                    size = data.length - offset;
                System.arraycopy(newData, 0, data, offset, size);
            } else
                throw (new IndexOutOfBoundsException());
        }
    }


    /**
     * @param offset     - indeks początku
     * @param numOfItems
     * @return
     */
    public float[] getDataPack(int offset, int numOfItems) {
        float[] result = null;
        if (data != null) {
            if (offset < data.length)
                internalDataIndex = offset;
            result = getDataPack(numOfItems);
        }
        return result;

    }

    public float[] getDataPack(int numOfItems) {
        if (numOfItems < 1 || data == null)
            return null;
        float[] result = new float[numOfItems];
        if (internalDataIndex + numOfItems < data.length) {
            System.arraycopy(data, internalDataIndex, result, 0, numOfItems);
            internalDataIndex += numOfItems;
        } else {
            Arrays.fill(result, (byte) 0);
            System.arraycopy(data, internalDataIndex, result, 0, data.length - internalDataIndex);
            internalDataIndex = data.length;
        }
        Log.i(this.getClass().getName(), "internalDataIndex: " + Integer.toString(internalDataIndex));
        return result;
    }

    public boolean eof() {
        if (data == null)
            return true;
        if (internalDataIndex == data.length)
            return true;
        return false;
    }

    public WaveRecord() {
        setAudioTrackSampleRate(16000);
        setAudioTrackChannels(AudioFormat.CHANNEL_OUT_MONO);
        setAudioTrackEncoding(AudioFormat.ENCODING_PCM_16BIT);
        data = null;
        internalDataIndex = 0;
    }

    public WaveRecord(int frequency, int channelConfiguration, int audioEncoding) {
        setAudioTrackSampleRate(frequency);
        setAudioTrackChannels(channelConfiguration);
        setAudioTrackEncoding(audioEncoding);
        data = null;
        internalDataIndex = 0;
    }

    public int getNumOfFrames() {
        if (data != null) {
            if (getAudioTrackChannels() == AudioFormat.CHANNEL_IN_MONO && getAudioTrackEncoding() == AudioFormat.ENCODING_PCM_8BIT) {
                return data.length;
            } else if (getAudioTrackChannels() == AudioFormat.CHANNEL_IN_MONO && getAudioTrackEncoding() == AudioFormat.ENCODING_PCM_16BIT) {
                return data.length / 2;
            } else if (getAudioTrackChannels() == AudioFormat.CHANNEL_IN_STEREO && getAudioTrackEncoding() == AudioFormat.ENCODING_PCM_8BIT) {
                return data.length / 2;
            } else if (getAudioTrackChannels() == AudioFormat.CHANNEL_IN_STEREO && getAudioTrackEncoding() == AudioFormat.ENCODING_PCM_16BIT) {
                return data.length / 4;
            }
            return 0;
        } else
            return 0;
    }

    /**
     * @return time in millis
     */
    public long getDuration() {
        return (long) ((double) getNumOfFrames() / (double) audioTrackSampleRate * 1000);
    }

    public void clear() {
        setAudioTrackChannels(AudioFormat.CHANNEL_OUT_MONO);
        setAudioTrackEncoding(AudioFormat.ENCODING_PCM_16BIT);

        data = null;
        internalDataIndex = 0;
    }

    public int getAudioTrackSampleRate() {
        return audioTrackSampleRate;
    }

    public void setAudioTrackSampleRate(int audioTrackSampleRate) {
        this.audioTrackSampleRate = audioTrackSampleRate;
    }

    public int getAudioTrackChannels() {
        return audioTrackChannels;
    }

    public void setAudioTrackChannels(int audioTrackChannels) {
        switch (audioTrackChannels) {
            case AudioFormat.CHANNEL_IN_STEREO:
                this.audioTrackChannels = AudioFormat.CHANNEL_OUT_STEREO;
                break;
            case AudioFormat.CHANNEL_IN_MONO:
                this.audioTrackChannels = AudioFormat.CHANNEL_OUT_MONO;
                break;
            default:
        }

    }

    public int getAudioTrackEncoding() {
        return audioTrackEncoding;
    }

    public void setAudioTrackEncoding(int audioTrackEncoding) {
        this.audioTrackEncoding = audioTrackEncoding;
    }

    public float[] getData() {
        return data;
    }

    public void setData(float[] data) {
        this.data = data;
    }

    public boolean isReadyForModulation() {
        if (data != null)
            if (data.length != 0)
                return true;
        return false;
    }
}

