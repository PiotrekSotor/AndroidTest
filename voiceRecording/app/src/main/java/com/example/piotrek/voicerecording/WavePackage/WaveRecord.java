package com.example.piotrek.voicerecording.WavePackage;

import android.media.AudioFormat;

import com.example.piotrek.voicerecording.Tools.WavFile;
import com.example.piotrek.voicerecording.Tools.WavFileException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Piotrek on 2015-10-13.
 */
public class WaveRecord implements Serializable {



    private int frequency = 0;
    private int numOfChannels = 0;
    private int numOfBytesPerSample = 0;
    private int bytesPerFrame = 0;
    private short[] dataShort = null;
    private byte[] dataByte = null;

    private int internalDataIndex = 0;


    public void reserInternalDataIndex() {
        this.internalDataIndex = 0;
    }

    public void appendData(byte[] newData) {
        byte[] temp = new byte[dataShort.length + newData.length];
        System.arraycopy(dataShort, 0, temp, 0, dataShort.length);
        System.arraycopy(newData, 0, temp, dataShort.length, newData.length);
        internalDataIndex = temp.length;
        dataByte = temp.clone();
    }

    public void appendData(short[] newData) {
        short[] temp = new short[dataShort.length + newData.length];
        System.arraycopy(dataShort, 0, temp, 0, dataShort.length);
        System.arraycopy(newData, 0, temp, dataShort.length, newData.length);
        internalDataIndex = temp.length;
        dataShort = temp.clone();
    }

    public short[] getDataPackShort(int numOfShorts) {
        short[] result = new short[numOfShorts];
        if (internalDataIndex + numOfShorts < dataShort.length) {
            System.arraycopy(dataShort, internalDataIndex, result, 0, numOfShorts);
            internalDataIndex += numOfShorts;
        } else {
            Arrays.fill(result, (byte) 0);
            System.arraycopy(dataShort, internalDataIndex, result, 0, dataShort.length - internalDataIndex);
            internalDataIndex = dataShort.length;
        }
        return result;
    }
    public byte[] getDataPackByte(int numOfBytes) {
        byte[] result = new byte[numOfBytes];
        if (internalDataIndex + numOfBytes < dataShort.length) {
            System.arraycopy(dataShort, internalDataIndex, result, 0, numOfBytes);
            internalDataIndex += numOfBytes;
        } else {
            Arrays.fill(result, (byte) 0);
            System.arraycopy(dataShort, internalDataIndex, result, 0, dataShort.length - internalDataIndex);
            internalDataIndex = dataShort.length;
        }
        return result;
    }

    public WaveRecord() {
        frequency = 8000;
        numOfChannels = AudioFormat.CHANNEL_IN_MONO;
        setNumOfBytesPerSample(AudioFormat.ENCODING_PCM_16BIT);
        dataShort = null;
        dataByte = null;
        internalDataIndex=0;
    }

    public WaveRecord(int frequency, int channelConfiguration, int audioEncoding) {
        this.frequency = frequency;
        this.numOfChannels = channelConfiguration;
        this.setNumOfBytesPerSample(audioEncoding);
        dataShort = null;
        dataByte = null;
        internalDataIndex=0;
    }
    public int getNumOfFrames()
    {
        if (dataByte != null)
            return dataByte.length/(getNumOfBytesPerSample() * getNumOfBytesPerSample());
        else if (dataShort != null)
            return dataShort.length/(getNumOfBytesPerSample() * getNumOfBytesPerSample());
        else
            return 0;
    }

    /**
     *
     * @return time in millis
     */
    public long getDuration()
    {
        return (long)((double)getNumOfFrames()/(double)frequency*1000);
    }

    public void clear() {
        frequency = 8000;
        numOfChannels = AudioFormat.CHANNEL_IN_MONO;
        setNumOfBytesPerSample(AudioFormat.ENCODING_PCM_16BIT);
        dataShort = null;
        internalDataIndex=0;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getNumOfChannels() {
        return numOfChannels;
    }

    public void setNumOfChannels(int numOfChannels) {
        this.numOfChannels = numOfChannels;
    }

    public int getNumOfBytesPerSample() {
        return numOfBytesPerSample;
    }

    public void setNumOfBytesPerSample(int numOfBytesPerSample) {
        this.numOfBytesPerSample = numOfBytesPerSample;
    }
    public void saveAsRealWaveFile()
    {
        File file = new File(WaveActivity.recordFileName);
        try {
            WavFile wavFile = WavFile.newWavFile(file, getNumOfChannels(), getNumOfFrames(), getNumOfBytesPerSample(), getFrequency());
            if (dataShort != null)
            {
                for (int curIndex=0;curIndex<dataShort.length;++curIndex)
                {
                    int numOfShorts = 256;
                    if (wavFile.getFramesRemaining() < 256)
                        numOfShorts = (int) wavFile.getFramesRemaining();
                    if (numOfChannels == 1)
                    {
                        int[] buffer = new int[256];
                        for (int i=0;i<numOfShorts;++i)
                        {
                            buffer[i] = (int)dataShort[curIndex+i];
                        }

                        wavFile.writeFrames(buffer, numOfShorts);
                    }
                    else if (numOfChannels == 2)
                    {
//                         to do or not to do
                    }

                }
            }
            else if (dataByte != null)
            {
//                to do or not to do
//                for (int curIndex=0;curIndex<dataByte.length;++curIndex)
//                {
//
//                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (WavFileException e) {
            e.printStackTrace();
        }
    }

}
