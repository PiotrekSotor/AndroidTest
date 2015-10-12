package com.example.piotrek.voicerecording;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Piotrek on 2015-10-12.
 */
public class RecordButton extends Button {
    private MainActivity mainActivity;
    boolean mStartRecording = true;
    private static final String LOG_TAG = "RecordButton";

    OnClickListener clicker = new OnClickListener() {
        public void onClick(View v) {
            Log.i(LOG_TAG, "recordButton on click");
            getMainActivity().onRecord(mStartRecording);
            if (mStartRecording) {
                setText("Stop recording");
                getMainActivity().timer.setEndTime(Long.MAX_VALUE);
                getMainActivity().timer.startTimer();

            } else {
                setText("Start recording");
                getMainActivity().timer.stopTimer();
                getMainActivity().timer.resetTimer();
            }
            mStartRecording = !mStartRecording;
        }
    };

    public RecordButton(Context ctx) {
        super(ctx);
        setText("Start recording");
        setOnClickListener(clicker);
    }
    public RecordButton(Context ctx,AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
        setText("Start recording");
        setOnClickListener(clicker);

    }
    public RecordButton(Context ctx,AttributeSet attrs) {
        super(ctx, attrs);
        setText("Start recording");
        setOnClickListener(clicker);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
