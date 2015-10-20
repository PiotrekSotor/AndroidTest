package com.example.piotrek.voicerecording.WavePackage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * Created by Piotrek on 2015-10-16.
 */
public class ModulationButton extends Button {


    private OnClickListener clicker = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (WaveRecord.getInstance().isReadyForModulation())
            {
                setText("In progress");
                setEnabled(false);
                int dataPackLength = calculateDataPackLength();
                float[] firstHalf;
                float[] secondHalf;



                setText("Perform modulation");
                setEnabled(true);
            }

        }
    };

    /**
     *
     * @param dataPack - paczka o rozmiarze zgodnym z wzorem do modulowania
     * @return - zmodulowana paczka
     */
    private float[] performModulation(float[] dataPack) {
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
     *  fft
     *  modulacja
     *  ifft
     * @param dataPack - fragment sygnału po okienkowaniu
     * @return
     */
    private float[] modulate(float[] dataPack)
    {
        float[] result = null;
        if (dataPack != null)
        {
            //fft

            //perform filter

            //ifft
        }

        return result;
    }

    /**
     * Okienkowanie fragmentu sygnału, w celu uniknięcia wysokich częstotliwości na końcach przedziału
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
     *
     * @return liczba probek stanowiaca fragment o czasie 10-20 ms jako potęga 2
     */
    private int calculateDataPackLength()
    {
        if (WaveRecord.getInstance().isReadyForModulation())
            return (int) Math.pow(2, Math.log(WaveRecord.getInstance().getAudioTrackSampleRate()/50.0)/Math.log(2));
        return 0;
    }

    public ModulationButton(Context context) {
        super(context);
    }

    public ModulationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ModulationButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
