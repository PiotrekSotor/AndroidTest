package com.example.piotrek.voicerecording.WavePackage;

import android.media.AudioFormat;
import android.util.Log;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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

    private int numOfSave=0;
    private int numOfSaveFFT=0;


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


    public void appendData(short[] newData) {
        if (data == null)
            data = new float[0];
        try {
            FileWriter fw = new FileWriter(WaveActivity.recordFileName, true);

            float[] temp = new float[data.length + newData.length];
            System.arraycopy(data, 0, temp, 0, data.length);
            String buffer = new String();
            for (int i = 0; i < newData.length; ++i) {
                temp[i + data.length] = (float) newData[i] / 0x7fff;
//                Log.i(getClass().getName(), "short newData[i]: " + Short.toString(newData[i]) + "  data[i]: " + Float.toString(temp[data.length + i]));
//                buffer +=Short.toString(newData[i]) + "\n";
//                fw.write(Short.toString(newData[i]) + "\n");
            }
            internalDataIndex = temp.length;
            data = temp.clone();
//            fw.write(buffer);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.arraycopy(newData, 0, temp, dataShort.length, newData.length);

        Log.i(getClass().getName(), "appendData data.length: " + Integer.toString(data.length));
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

    public void saveInFile()
    {
        try {
            FileWriter fw = new FileWriter(WaveActivity.recordFileName + Integer.toString(numOfSave++));
            fw.write("Samplerate: "+Integer.toString(audioTrackSampleRate) + "\n");
            fw.write("Encoding: "+Integer.toString(audioTrackEncoding) + "\n");
            fw.write("Channels: "+Integer.toString(audioTrackChannels) + "\n");
            for (int i=0;i<data.length;++i)
            {
                fw.write(Float.toString(data[i]) + "\n");
            }
            fw.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }
    public void saveInFile(double[] array)
    {
        try {
            FileWriter fw = new FileWriter(WaveActivity.recordFileName + Integer.toString(numOfSaveFFT++)+"_short");
            fw.write("Samplerate: "+Integer.toString(audioTrackSampleRate) + "\n");
            fw.write("Encoding: "+Integer.toString(audioTrackEncoding) + "\n");
            fw.write("Channels: "+Integer.toString(audioTrackChannels) + "\n");
            fw.write("FFT: " + Integer.toString(array.length)+"\n");
            for (int i=0;i<array.length;++i)
            {
                fw.write(Double.toString(array[i]) + "\n");
            }
            fw.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

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
            Arrays.fill(result, 0.0f);
            System.arraycopy(data, internalDataIndex, result, 0, data.length - internalDataIndex);
            internalDataIndex = data.length;
        }
        Log.i(this.getClass().getName(), "getDataPack internalDataIndex: " + Integer.toString(internalDataIndex));
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
        File file = new File(WaveActivity.recordFileName);
        file.delete();
        setAudioTrackSampleRate(8000);
        setAudioTrackChannels(AudioFormat.CHANNEL_OUT_MONO);
        setAudioTrackEncoding(AudioFormat.ENCODING_PCM_8BIT);

//        Log.e(getClass().getName(),"getAudioTrackSampleRate() : "+Integer.toString(WaveRecord.getInstance().getAudioTrackSampleRate()));
//        Log.e(getClass().getName(), "getAudioTrackChannels() : " + Integer.toString(WaveRecord.getInstance().getAudioTrackChannels()));
//        Log.e(getClass().getName(), "getAudioTrackEncoding() : " + Integer.toString(WaveRecord.getInstance().getAudioTrackEncoding()));
//        Log.e(getClass().getName(), "AudioFormat.CHANNEL_OUT_MONO : " + Integer.toString(AudioFormat.CHANNEL_OUT_MONO));


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
            if (getAudioTrackChannels() == AudioFormat.CHANNEL_OUT_MONO)
                return data.length;
            else if (getAudioTrackChannels() == AudioFormat.CHANNEL_OUT_STEREO)
                return data.length / 2;
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
        Log.e(getClass().getName(), "setAudioTrackChannels : audioTrackChannels : " + Integer.toString(audioTrackChannels));
        switch (audioTrackChannels) {
            case AudioFormat.CHANNEL_IN_STEREO:
                this.audioTrackChannels = AudioFormat.CHANNEL_OUT_STEREO;
                break;
            case AudioFormat.CHANNEL_IN_MONO:
                this.audioTrackChannels = AudioFormat.CHANNEL_OUT_MONO;
                break;
            default:
                this.audioTrackChannels = audioTrackChannels;
                Log.e(getClass().getName(), "default");
        }
        Log.e(getClass().getName(), "setAudioTrackChannels : this.audioTrackChannels : " + Integer.toString(this.audioTrackChannels));

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

    /**
     *
     * @return progress as float value [0 ... 1]
     */
    public float getProgress()
    {
        return (float)internalDataIndex/(float)data.length;
    }
}

