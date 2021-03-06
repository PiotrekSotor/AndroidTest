/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulationtestapp;


import Enumerators.UnifyEnum;
import Tools.FilterConfiguration;
import Tools.Settings;
import fftpack1.RealDoubleFFT;

/**
 * Created by Piotrek on 2015-10-16.
 */
public class Modulator {

    RealDoubleFFT transform = null;
    private String LOG_TAG = this.getClass().getName();
    private int counter = 0;
    
    public void doIt()
    {
//        Log.i(LOG_TAG, "clicker 1st");
            if (WaveRecord.getInstance().isReadyForModulation()) {
                
                long timeStart = System.currentTimeMillis();
                int dataPackLength = calculateDataPackLength();
                System.out.println("dataPackLength: " + Integer.toString(dataPackLength));
                
                if (WaveRecord.getInstance().getAudioTrackChannels() ==1) {
                    System.out.println("CHANNEL_OUT_MONO");
                    float[] firstHalf = new float[dataPackLength / 2];
                    float[] secondHalf = new float[dataPackLength / 2];
                    float[] oldSecondHalf = new float[dataPackLength / 2];
                    int index = 0;
                    WaveRecord.getInstance().reserInternalDataIndex();
                    while (!WaveRecord.getInstance().eof()) {
                        
                        float[] temp = WaveRecord.getInstance().getDataPack(index, dataPackLength);
//                        firstHalf = WaveRecord.getInstance().getDataPack(index, dataPackLength / 2);
//                        secondHalf = WaveRecord.getInstance().getDataPack(dataPackLength / 2);

                        temp = performModulation(temp);
                        System.arraycopy(temp, 0, firstHalf, 0, dataPackLength / 2);
                        System.arraycopy(temp, dataPackLength / 2, secondHalf, 0, dataPackLength / 2);
                        if (index != 0)
                            firstHalf = unifyHalfs(oldSecondHalf, firstHalf);
                        WaveRecord.getInstance().replaceDataPack(firstHalf, index);

                        System.arraycopy(temp, dataPackLength / 2, oldSecondHalf, 0, dataPackLength / 2);
                        index += dataPackLength / 2;
                    }
                    // ostatni fragment należy wstawić ręcznie
                    WaveRecord.getInstance().replaceDataPack(oldSecondHalf, index);

                }
                if (WaveRecord.getInstance().getAudioTrackChannels() == 2) {
//                    Log.i(LOG_TAG,"CHANNEL_OUT_STEREO");
                    int index = 0;
                    float[] firstHalfFirstChannel = new float[dataPackLength / 2];
                    float[] secondHalfFirstChannel = new float[dataPackLength / 2];
                    float[] firstHalfSecondChannel = new float[dataPackLength / 2];
                    float[] secondHalfSecondChannel = new float[dataPackLength / 2];
                    float[] oldSecondHalfFirstChannel = new float[dataPackLength / 2];
                    float[] oldSecondHalfSecondChannel = new float[dataPackLength / 2];

                    while (!WaveRecord.getInstance().eof()) {
                        float[] temp = WaveRecord.getInstance().getDataPack(index, 2 * dataPackLength);
                        //rozdzielenie kanałów
                        for (int i = 0; i < dataPackLength / 2; ++i) {
                            firstHalfFirstChannel[i] = temp[2 * i];
                            firstHalfSecondChannel[i] = temp[2 * i + 1];
                        }
                        for (int i = dataPackLength / 2; i < dataPackLength; ++i) {
                            secondHalfFirstChannel[i - dataPackLength / 2] = temp[2 * i];
                            secondHalfSecondChannel[i - dataPackLength / 2] = temp[2 * i + 1];
                        }
                        //modulowanie kanałów
                        float[] tempFirstChannel = performModulation(floatArraysConcatenate(firstHalfFirstChannel, secondHalfFirstChannel));
                        float[] tempSecondChannel = performModulation(floatArraysConcatenate(firstHalfSecondChannel, secondHalfSecondChannel));

                        System.arraycopy(tempFirstChannel, 0, firstHalfFirstChannel, 0, dataPackLength / 2);
                        System.arraycopy(tempFirstChannel, dataPackLength / 2, secondHalfFirstChannel, 0, dataPackLength / 2);
                        System.arraycopy(tempSecondChannel, 0, firstHalfSecondChannel, 0, dataPackLength / 2);
                        System.arraycopy(tempSecondChannel, dataPackLength / 2, secondHalfSecondChannel, 0, dataPackLength / 2);

                        firstHalfFirstChannel = unifyHalfs(oldSecondHalfFirstChannel, firstHalfFirstChannel);
                        firstHalfSecondChannel = unifyHalfs(oldSecondHalfSecondChannel, firstHalfSecondChannel);

                        float[] result = new float[dataPackLength];
                        for (int i = 0; i < dataPackLength / 2; ++i) {
                            result[2 * i] = firstHalfFirstChannel[i];
                            result[2 * i + 1] = firstHalfSecondChannel[i];
                        }
                        WaveRecord.getInstance().replaceDataPack(result, index);

                        System.arraycopy(secondHalfFirstChannel, 0, oldSecondHalfFirstChannel, 0, dataPackLength / 2);
                        System.arraycopy(secondHalfSecondChannel, 0, oldSecondHalfSecondChannel, 0, dataPackLength / 2);

                        index += dataPackLength;
                    }

                }
                transform = null;
                System.out.println("Modulationdone in "+ Long.toString(System.currentTimeMillis()-timeStart));
                WaveRecord.getInstance().saveInFile();
            }
    }

    

    /**
     * @param dataPack - paczka o rozmiarze zgodnym z wzorem do modulowania
     * @return - zmodulowana paczka
     */
    private float[] performModulation(float[] dataPack) {
        System.out.println("performModulation : dataPack.length = " + Integer.toString(dataPack.length));
        float[] result = null;
        if (dataPack != null) {
            result = dataPack.clone();
            result = windowing(result);
            result = modulate(result);
        }
        return result;
    }

    /**
     * wykonanie krokow:
     * fft
     * modulacja
     * ifft
     *
     * @param dataPack - fragment sygnału po okienkowaniu
     * @return
     */
    private float[] modulate(float[] dataPack) {
        System.out.println("modulate: dataPack.length:" + Integer.toString(dataPack.length));
        float[] result = null;
        if (dataPack != null) {
            //fft
            if (transform == null)
                transform = new RealDoubleFFT(dataPack.length);
            
            double[] tranformDataPack = floatToDouble(dataPack);
            transform.ft(tranformDataPack);
            ModulationTestApp.saveInFile("textFiles/beforeModulation_" + Integer.toString(counter++)+".txt",tranformDataPack);
            //perform filter
            switch(Settings.getInstance().getCurFilterType())
            {
                case BlurFilter:
                    tranformDataPack = filteringBlur(tranformDataPack);
                    break;
                case ScaleFilter:
                    tranformDataPack = filteringScale(tranformDataPack);
                    break;
                case CapacityFilter:
                    tranformDataPack = filteringCapacity(tranformDataPack);
                    break;
            }
            ModulationTestApp.saveInFile("./textFiles/afterModulation_" + Integer.toString(counter++)+".txt",tranformDataPack);
            //ifft
            transform.bt(tranformDataPack);
            result = doubleToFloat(tranformDataPack);
        }

        return result;
    }

    /**
     *
     * @param spectrum - widmo fragmentu, dziedzina y: (-1,1)
     * @return
     */
    private double[] filteringBlur(double[] spectrum)
    {
        double[] result = null;
        if (spectrum != null)
        {
            result = new double[spectrum.length];
            int blurRange = Settings.getInstance().getCurBlurRange();
            for (int i=0;i<spectrum.length;++i)
                result[i] = averageValue(spectrum,i,blurRange);
        }
        return result;
    }
    /**
     *
     * @param spectrum - widmo fragmentu, dziedzina y: (-1,1)
     * @return
     */
    private double[] filteringScale(double[] spectrum)
    {
        double[] result = null;
        if (spectrum != null)
        {
            result = new double[spectrum.length];
            float scaleFactor = Settings.getInstance().getCurScaleFactor();
            for (int i=0;i<spectrum.length;++i)
            {
                if (i*scaleFactor<spectrum.length)
                    result[(int)(i*scaleFactor)] = spectrum[i];
                else
                    result[i] = 0;
            }
        }
        return result;
    }
    private double[] filteringCapacity(double[] spectrum)
    {
        return spectrum;
    }

    /**
     *
     * @param array - tablica której element jest uśredniany
     * @param middleIndex - index elementu uśrednianego
     * @param range - szerokość przedziału usrednianego, uśrednianie elementów (middleIndex-range, middleIndex+range)
     * @return
     */
    private double averageValue (double[] array, int middleIndex, int range)
    {
        double  result = 0;
        if (array != null && middleIndex >= 0 && range >= 0)
        {
            if (middleIndex < array.length && range < array.length) {
                int lowerBound = middleIndex - range;
                int upperBound = middleIndex + range + 1;
                if (lowerBound < 0)
                    lowerBound = 0;
                if (upperBound > array.length)
                    upperBound = array.length;
                for (int i = lowerBound; i < upperBound; ++i)
                    result += array[i];
                result += array[middleIndex]  * 2;
                result /= upperBound - lowerBound;
            }
        }
        return result;
    }

    /**
     * Okienkowanie fragmentu sygnału, w celu uniknięcia wysokich częstotliwości na końcach przedziału
     *
     * @param dataPack
     * @return
     */
    private float[] windowing(float[] dataPack) {
        float[] result = null;
        if (dataPack != null) {
            result = dataPack.clone();
            for (int i = 0; i < 0.2 * result.length; ++i) {
                float factor = i / (0.2f * result.length);
                result[i] *= factor;
                result[result.length - 1 - i] *= factor;
            }
        }
        return result;
    }

    /**
     * @return liczba probek stanowiaca fragment o czasie 10-20 ms jako potęga 2
     */
    private int calculateDataPackLength() {
        if (WaveRecord.getInstance().isReadyForModulation())
            return (int) Math.pow(2, Math.floor(Math.log(WaveRecord.getInstance().getAudioTrackSampleRate() / 50.0) / Math.log(2)));
        return 0;
    }

    /**
     * @param first  - pierwsza tablica
     * @param second - druga tablica
     * @return
     */
    private float[] floatArraysConcatenate(float[] first, float[] second) {
        float[] result = null;
        if (first != null && second != null) {
            result = new float[first.length + second.length];
            System.arraycopy(first, 0, result, 0, first.length);
            System.arraycopy(second, 0, result, first.length, second.length);
        } else if (first != null && second == null) {
            result = new float[first.length];
            System.arraycopy(first, 0, result, 0, first.length);
        } else if (first == null && second != null) {
            result = new float[second.length];
            System.arraycopy(second, 0, result, 0, second.length);
        }
        return result;
    }

    /**
     * konwersja tablicy float na double
     * @param array
     * @return
     */
    private double[] floatToDouble (float[] array)
    {
        double[] result = null;
        if (array != null)
        {
            result = new double[array.length];
            for (int i=0;i<array.length;++i)
                result[i]=(double)array[i];
        }
        return result;
    }

    /**
     * konwersja tablicy double na float
     * @param array
     * @return
     */
    private float[] doubleToFloat(double[] array)
    {
        float[] result = null;
        if (array != null)
        {
            result = new float[array.length];
            for (int i=0;i<array.length;++i)
                result[i]=(float)array[i];
        }
        return result;
    }

    /**
     * @param older   - fragment wcześniejszej modulacji, były secondHalf
     * @param younger - fragment późniejszej modulacji, obecny firstHalf
     * @return
     */
    private float[] unifyHalfs(float[] older, float[] younger) {
        float[] result = null;
        if (older != null && younger != null) {
            if (older.length == younger.length && older.length > 0) {
                result = new float[older.length];


                for (int i = 0; i < result.length; ++i) {
                    float factor = 1.0f / result.length;
                    UnifyEnum unifyMode = Settings.getInstance().getCurUnifyMode();
                    switch (unifyMode) {
                        case Trigonometric:
                            factor = (float) Math.pow(Math.sin(Math.toRadians(i / 90.0f)), 2.0);
                            break;
                        case Linear:
                            factor = (float) i / result.length;
                            break;
                    }
                    result[i] = factor * younger[i] + (1 - factor) * older[i];
                }
            }
        } else if (older == null && younger != null) {
            result = younger.clone();
        } else if (older != null && younger == null) {
            result = older.clone();
        }
        return result;
    }

}
