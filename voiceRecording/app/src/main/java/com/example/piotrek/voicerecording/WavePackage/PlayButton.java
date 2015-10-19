package com.example.piotrek.voicerecording.WavePackage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * Created by Piotrek on 2015-10-14.
 */
public class PlayButton extends Button {

    private WaveActivity waveActivity;


    private OnClickListener clicker = new OnClickListener() {
        @Override
        public void onClick(View v) {
            waveActivity.onPlay();
            if (waveActivity.ismStartPlaying())
            {
                setText("Stop playing");
                waveActivity.getTimer().resetTimer();
                waveActivity.getTimer().startTimer();
            }
            else
            {
                setText("Start playing");
                waveActivity.getTimer().stopTimer();
                waveActivity.getTimer().resetTimer();
            }

        }
    };

    private void contructor()
    {
        setOnClickListener(clicker);
    }

    public PlayButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        contructor();
    }

    public PlayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        contructor();
    }

    public PlayButton(Context context) {
        super(context);
        contructor();
    }

    public WaveActivity getWaveActivity() {
        return waveActivity;
    }

    public void setWaveActivity(WaveActivity waveActivity) {
        this.waveActivity = waveActivity;
    }
}
