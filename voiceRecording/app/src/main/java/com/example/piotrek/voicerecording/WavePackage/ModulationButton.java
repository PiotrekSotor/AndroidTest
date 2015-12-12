package com.example.piotrek.voicerecording.WavePackage;

import android.content.Context;
import android.media.AudioFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.piotrek.voicerecording.Enumerators.CrossfadeEnum;
import com.example.piotrek.voicerecording.Tools.Point;
import com.example.piotrek.voicerecording.Tools.Settings;
import com.example.piotrek.voicerecording.fftpack.RealDoubleFFT;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Piotrek on 2015-10-16.
 */
public class ModulationButton extends Button {

    RealDoubleFFT transform = null;
    private String LOG_TAG = this.getClass().getName();

    private OnClickListener clicker = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(LOG_TAG, "clicker 1st");
            if (WaveRecord.getInstance().isReadyForModulation()) {
                Log.i(LOG_TAG, "WaveRecord is ready for modulation");
                setText("In progress");
                setEnabled(false);
                long timeStart = System.currentTimeMillis();
                int dataPackLength = calculateDataPackLength();
                Log.i(LOG_TAG, "dataPackLength: " + Integer.toString(dataPackLength));
                WaveRecord.getInstance().reserInternalDataIndex();
                if (WaveRecord.getInstance().getAudioTrackChannels() == AudioFormat.CHANNEL_OUT_MONO) {
                    Log.i(LOG_TAG, "CHANNEL_OUT_MONO");
                    float[] firstHalf = new float[dataPackLength / 2];
                    float[] secondHalf = new float[dataPackLength / 2];
                    float[] oldSecondHalf = new float[dataPackLength / 2];
                    int index = 0;
                    Log.i(LOG_TAG, "saving file");
                    WaveRecord.getInstance().saveInFile();
                    Log.i(LOG_TAG, "saved in file");
                    while (!WaveRecord.getInstance().eof()) {
                        long startTime = System.currentTimeMillis();
                        Log.i(getClass().getName(), "index: " + Integer.toString(index));

                        firstHalf = WaveRecord.getInstance().getDataPack(index, dataPackLength / 2);
                        secondHalf = WaveRecord.getInstance().getDataPack(dataPackLength / 2);

                        Log.e(getClass().getName(), "clicker - getDataPack: " + Long.toString(System.currentTimeMillis() - startTime));
                        startTime = System.currentTimeMillis();

                        float[] temp = performModulation(floatArraysConcatenate(firstHalf, secondHalf));
                        Log.e(getClass().getName(), "clicker - preformModulation: " + Long.toString(System.currentTimeMillis() - startTime));

                        System.arraycopy(temp, 0, firstHalf, 0, dataPackLength / 2);
                        System.arraycopy(temp, dataPackLength / 2, secondHalf, 0, dataPackLength / 2);

                        startTime = System.currentTimeMillis();
                        firstHalf = crossfade(oldSecondHalf, firstHalf);
                        Log.e(getClass().getName(), "clicker - unify: " + Long.toString(System.currentTimeMillis() - startTime));


                        for (int i = 0; i < firstHalf.length; ++i)
                            firstHalf[i] /= 120;

                        startTime = System.currentTimeMillis();
                        WaveRecord.getInstance().replaceDataPack(firstHalf, index);
                        Log.e(getClass().getName(), "clicker - replaceDataPack: " + Long.toString(System.currentTimeMillis() - startTime));


                        System.arraycopy(temp, dataPackLength / 2, oldSecondHalf, 0, dataPackLength / 2);
                        index += dataPackLength / 2;
                    }
                    // ostatni fragment należy wstawić ręcznie
                    WaveRecord.getInstance().replaceDataPack(oldSecondHalf, index);

                }
                if (WaveRecord.getInstance().getAudioTrackChannels() == AudioFormat.CHANNEL_OUT_STEREO) {
                    Log.i(LOG_TAG, "CHANNEL_OUT_STEREO");
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

                        firstHalfFirstChannel = crossfade(oldSecondHalfFirstChannel, firstHalfFirstChannel);
                        firstHalfSecondChannel = crossfade(oldSecondHalfSecondChannel, firstHalfSecondChannel);

                        float[] result = new float[dataPackLength];
                        for (int i = 0; i < dataPackLength / 2; ++i) {
                            result[2 * i] = firstHalfFirstChannel[i];
                            result[2 * i + 1] = firstHalfSecondChannel[i];
                        }
                        for (int i = 0; i < result.length; ++i)
                            result[i] /= 120;
                        WaveRecord.getInstance().replaceDataPack(result, index);

                        System.arraycopy(secondHalfFirstChannel, 0, oldSecondHalfFirstChannel, 0, dataPackLength / 2);
                        System.arraycopy(secondHalfSecondChannel, 0, oldSecondHalfSecondChannel, 0, dataPackLength / 2);

                        index += dataPackLength;
                    }

                }
                transform = null;

                Toast.makeText(getContext(), "Modulation done in " + Long.toString(System.currentTimeMillis() - timeStart) + " millis", Toast.LENGTH_SHORT).show();
                setText("Perform modulation");
                setEnabled(true);
//                WaveRecord.getInstance().saveInFile();
            }

        }
    };


    /**
     * @param dataPack - paczka o rozmiarze zgodnym z wzorem do modulowania
     * @return - zmodulowana paczka
     */
    private float[] performModulation(float[] dataPack) {
        float[] result = null;
        if (dataPack != null) {
            long startTime = System.currentTimeMillis();
            result = dataPack.clone();
            Log.e(getClass().getName(), "performModulation - clone time: " + Long.toString(System.currentTimeMillis() - startTime));
            startTime = System.currentTimeMillis();
            result = windowing(result);
            Log.e(getClass().getName(), "performModulation - hanning time: " + Long.toString(System.currentTimeMillis() - startTime));
            startTime = System.currentTimeMillis();
            result = modulate(result);
            Log.e(getClass().getName(), "performModulation - modulate time: " + Long.toString(System.currentTimeMillis() - startTime));

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
        float[] result = null;
        if (dataPack != null) {
            //fft
            long startTime = System.currentTimeMillis();
            if (transform == null) {
                transform = new RealDoubleFFT(calculateDataPackLength());
            }
            double[] tranformDataPack = floatToDouble(dataPack);
            WaveRecord.getInstance().saveInFile(tranformDataPack); // file n%4==0
            transform.ft(tranformDataPack);
//            WaveRecord.getInstance().saveInFile(tranformDataPack); // file n%4==1
            Log.e(getClass().getName(), "modulate - fft time: " + Long.toString(System.currentTimeMillis() - startTime));

//            double max = tranformDataPack[0];
//            for (int i = 0; i < tranformDataPack.length; ++i)
//                if (max < tranformDataPack[i]) {
//                    max = tranformDataPack[i];
//                }
//            Log.i(getClass().getName(), "dataPack after fft maxValue" + Double.toString(max));

//            perform filter
            Log.i(getClass().getName(), "modulate type: " + Settings.getInstance().getCurFilterType());
            startTime = System.currentTimeMillis();
            switch (Settings.getInstance().getCurFilterType()) {
                case BlurFilter:
                    tranformDataPack = filteringBlur(tranformDataPack);
                    break;
                case ScaleFilter:
                    tranformDataPack = filteringScale(tranformDataPack);
                    break;
                case PassFilter:
                    tranformDataPack = filteringCapacity(tranformDataPack);
                    break;
            }
            Log.e(getClass().getName(), "modulate - filtering time: " + Long.toString(System.currentTimeMillis() - startTime));

            startTime = System.currentTimeMillis();
            //ifft
//            WaveRecord.getInstance().saveInFile(tranformDataPack); // file n%4==2
            transform.bt(tranformDataPack);
//            WaveRecord.getInstance().saveInFile(tranformDataPack); // file n%4==3
            result = doubleToFloat(tranformDataPack);

            Log.e(getClass().getName(), "modulate - ifft time: " + Long.toString(System.currentTimeMillis() - startTime));

        }

        return result;
    }

    /**
     * @param spectrum - zapis widma wejściowen
     * @return
     */
    private double[] filteringBlur(double[] spectrum) {
        double[] result = null;
        if (spectrum != null) {
            result = new double[spectrum.length];
            int blurRange = Settings.getInstance().getCurBlurRange();
            for (int i = 0; i < spectrum.length; ++i)
                result[i] = averageValue(spectrum, i, blurRange);
        }
        return result;
    }

    /**
     * @param spectrum  - zapis widma wejściowego
     * @param blurRange - promień przedziału uśredniania
     * @return - rezultat
     */
    private double[] filteringBlur(double[] spectrum, int blurRange) {
        double[] result = null;
        if (spectrum != null && blurRange > -1) {
            result = new double[spectrum.length];
            for (int i = 0; i < spectrum.length; ++i)
                result[i] = averageValue(spectrum, i, blurRange);
        }
        return result;
    }

    /**
     * @param spectrum - zapis widma wejściowego
     * @return
     */
    private double[] filteringScale(double[] spectrum) {
        double[] result = null;
        if (spectrum != null) {
            result = new double[spectrum.length];
            float scaleFactor = Settings.getInstance().getCurScaleFactor();
            for (int i = 0; i < spectrum.length; ++i) {
                if (i * scaleFactor < spectrum.length)
                    result[(int) (i * scaleFactor)] = spectrum[i];
                else
                    result[i] = 0;
            }
        }
        return result;
    }

    /**
     * @param spectrum    - zapis widma wejściowego
     * @param scaleFactor - współczynnik skalowania
     * @return - rezultat modulacji
     */
    private double[] filteringScale(double[] spectrum, float scaleFactor) {
        double[] result = null;
        if (spectrum != null) {
            result = new double[spectrum.length];
            for (int i = 0; i < spectrum.length; ++i) {
                if (i * scaleFactor < spectrum.length)
                    result[(int) (i * scaleFactor)] = spectrum[i];
                else
                    result[i] = 0;
            }
        }
        return result;
    }

    private double[] filteringCapacity(double[] spectrum) {
        double[] result = null;
        if (spectrum != null) {
            result = new double[spectrum.length];
            List<Point> capacityPoints = Settings.getInstance().getCurCapacityPoints();
            List<Point> localCapacityPoints = new ArrayList<>();
            for (int i = 0; i < capacityPoints.size(); ++i)
                localCapacityPoints.add(new Point(capacityPoints.get(i).getFrequency(), capacityPoints.get(i).getValue()));
            if (capacityPoints.size() == 0)
                return spectrum;
            float hzPerSpectrumIndex = WaveRecord.getInstance().getAudioTrackSampleRate() / 2.0f / spectrum.length;

            Point lower;
            Point higher;
            int lowerIndex = 0;
            int higherIndex = 0;
            if (localCapacityPoints.get(0).getFrequency() != 0)
                localCapacityPoints.add(0, new Point(0, localCapacityPoints.get(0).getValue()));
            lower = localCapacityPoints.get(0);
            higher = localCapacityPoints.get(1);

            for (int i = 0; i < spectrum.length; ++i) {
                if (higher.getFrequency() < hzPerSpectrumIndex * i) {
                    // change lower, higher points
                    if (higherIndex + 1 < localCapacityPoints.size())
                        higherIndex++;
                    if (lowerIndex + 1 < localCapacityPoints.size())
                        lowerIndex++;
                    higher = localCapacityPoints.get(higherIndex);
                    lower = localCapacityPoints.get(lowerIndex);
                }
                int deltaFrequency = higher.getFrequency() - lower.getFrequency();
                float deltaValue = higher.getValue() - lower.getValue();
                float factor = lower.getValue() + i * hzPerSpectrumIndex * deltaValue / deltaFrequency;

                result[i] = spectrum[i] * factor;
            }
        }
        return result;
    }

    /**
     * @param spectrum   - zapis widma wejściowego
     * @param passPoints - lista par (częstotliwość, współczynnik)
     *                   posortowana rosnąco według częstotliwośći
     * @return - rezultat modulacji
     */
    private double[] filteringPass(double[] spectrum, List<Point> passPoints) {
        double[] result = null;
        if (spectrum != null) {
            result = new double[spectrum.length];
            List<Point> localPassPoints = new ArrayList<>();
            for (int i = 0; i < passPoints.size(); ++i)
                localPassPoints.add(
                        new Point(passPoints.get(i).getFrequency(),
                                passPoints.get(i).getValue()));
            if (passPoints.size() == 0)
                return spectrum;
            float hzPerSpectrumIndex =
                    WaveRecord.getInstance().getAudioTrackSampleRate() /
                            2.0f / spectrum.length;

            Point lower;
            Point higher;
            int lowerIndex = 0;
            int higherIndex = 0;
            if (localPassPoints.get(0).getFrequency() != 0)
                localPassPoints.add(
                        0,
                        new Point(0, localPassPoints.get(0).getValue()));

            lower = localPassPoints.get(0);
            higher = localPassPoints.get(1);

            for (int i = 0; i < spectrum.length; ++i) {
                if (higher.getFrequency() < hzPerSpectrumIndex * i) {
                    // change lower, higher points
                    if (higherIndex + 1 < localPassPoints.size())
                        higherIndex++;
                    if (lowerIndex + 1 < localPassPoints.size())
                        lowerIndex++;
                    higher = localPassPoints.get(higherIndex);
                    lower = localPassPoints.get(lowerIndex);
                }
                int deltaFrequency = higher.getFrequency() -
                        lower.getFrequency();
                float deltaValue = higher.getValue() - lower.getValue();
                float factor = lower.getValue() +
                        i * hzPerSpectrumIndex * deltaValue / deltaFrequency;

                result[i] = spectrum[i] * factor;
            }
        }
        return result;
    }

    /**
     * @param array       - zapis widma wejściowego
     * @param middleIndex - indeks elementu uśrednianego
     * @param range       - szerokość przedziału usrednianego,
     *                    uśrednianie elementów (middleIndex-range,
     *                    middleIndex+range)
     * @return
     */
    private double averageValue(double[] array, int middleIndex, int range) {
        double result = 0;
        if (array != null && middleIndex >= 0 && range >= 0) {
            if (middleIndex < array.length && range < array.length) {
                int lowerBound = middleIndex - range;
                int upperBound = middleIndex + range + 1;
                if (lowerBound < 0)
                    lowerBound = 0;
                if (upperBound > array.length)
                    upperBound = array.length;
                for (int i = lowerBound; i < upperBound; ++i)
                    result += array[i];
                result /= upperBound - lowerBound;
            }
        }
        return result;
    }

    /**
     * Okienkowanie fragmentu sygnału, w celu uniknięcia wysokich częstotliwości na końcach przedziału
     *
     * @param timeWindow
     * @return
     */
    private float[] windowing(float[] timeWindow) {
        float[] result = null;
        if (timeWindow != null) {
            result = timeWindow.clone();
            for (int i = 0; i < 0.5 * result.length; ++i) {
                float factor = getHanning(i, result.length);
                result[i] *= factor;
                result[result.length - 1 - i] *= factor;
            }
        }
        return result;
    }

    private float getHanning(int n, int windowLength) {
        return (float) (0.5f * (1 - Math.cos((2 * Math.PI * n) / windowLength)));
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
     *
     * @param array
     * @return
     */
    private double[] floatToDouble(float[] array) {
        double[] result = null;
        if (array != null) {
            result = new double[array.length];
            for (int i = 0; i < array.length; ++i)
                result[i] = (double) array[i];
        }
        return result;
    }

    /**
     * konwersja tablicy double na float
     *
     * @param array
     * @return
     */
    private float[] doubleToFloat(double[] array) {
        float[] result = null;
        if (array != null) {
            result = new float[array.length];
            for (int i = 0; i < array.length; ++i)
                result[i] = (float) array[i];
        }
        return result;
    }

    /**
     * @param partA ścieżka ściszana
     * @param partB - ścieżka zgłośniona
     */
    private float[] crossfade(float[] partA, float[] partB) {
        float[] result = null;
        if (partA != null && partB != null) {
            if (partA.length == partB.length && partA.length > 0) {
                result = new float[partA.length];


                for (int i = 0; i < result.length; ++i) {
                    float factor = 1.0f / result.length;
                    CrossfadeEnum unifyMode = Settings.getInstance().getCurUnifyMode();
                    switch (unifyMode) {
                        case Trigonometric:
                            factor = (float) Math.pow(Math.sin(Math.toRadians(((float) i / result.length) / 90.0f)), 2.0);
                            break;
                        case Linear:
                            factor = (float) i / result.length;
                            break;
                    }
                    getCrossFadeFactor(0, 0);
                    result[i] = factor * partB[i] + (1 - factor) * partA[i];
                }
            }
        } else if (partA == null && partB != null) {
            result = partB.clone();
        } else if (partA != null && partB == null) {
            result = partA.clone();
        }
        return result;
    }

    private float getCrossFadeFactor(int n, int N) {
        if (N == 0)
            return 0f;
        return (float) Math.pow(
                Math.sin((double) n / N * Math.PI / 2.0), 2.0
        );
    }

    public ModulationButton(Context context) {
        super(context);
        setOnClickListener(clicker);

    }

    public ModulationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(clicker);
    }

    public ModulationButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(clicker);
    }
}
