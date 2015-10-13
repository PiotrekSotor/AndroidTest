package com.example.piotrek.voicerecording.MediaRecordActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * Created by Piotrek on 2015-10-12.
 */
public class PlayButton extends Button {
    private static final String LOG_TAG = "PlayButton";
    boolean mStartPlaying = true;
    private MainActivity mainActivity;


    OnClickListener clicker = new OnClickListener() {
        public void onClick(View v) {
            mainActivity.onPlay(mStartPlaying);
            if (mStartPlaying) {
                setText("Stop playing");
                mainActivity.timer.resetTimer();
                mainActivity.timer.startTimer();
            } else {
                setText("Start playing");
                mainActivity.timer.stopTimer();
                mainActivity.timer.resetTimer();

            }
            mStartPlaying = !mStartPlaying;

        }
    };


    public PlayButton(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
        setText("Start playing");
        setOnClickListener(clicker);
    }

    public PlayButton(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        setText("Start playing");
        setOnClickListener(clicker);
    }

    public PlayButton(Context ctx) {
        super(ctx);
        setText("Start playing");
        setOnClickListener(clicker);
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}