package com.example.piotrek.voicerecording.WavePackage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * Created by Piotrek on 2015-10-14.
 */
public class RecordButton extends Button {

    private WaveActivity waveActivity;
    private static final String LOG_TAG = "WP.RecordButton";

    private OnClickListener clicker = new OnClickListener() {
        @Override
        public void onClick(View v) {
            waveActivity.onRecord();
            if (waveActivity.ismStartRecording())
            {
                setText("Stop recording");
            }
            else
            {
                setText("Start recording");
            }
        }
    };




    private void constructor()
    {
        setOnClickListener(clicker);
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        constructor();
    }

    public RecordButton(Context context) {
        super(context);
        constructor();
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        constructor();
    }

    public WaveActivity getWaveActivity() {
        return waveActivity;
    }

    public void setWaveActivity(WaveActivity waveActivity) {
        this.waveActivity = waveActivity;
    }
}
