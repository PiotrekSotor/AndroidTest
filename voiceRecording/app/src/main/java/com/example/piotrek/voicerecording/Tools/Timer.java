package com.example.piotrek.voicerecording.Tools;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Piotrek on 2015-10-07.
 */
public class Timer extends TextView {

    private long endTime = 0;
    private long startTime = 0;
    private boolean running = false;

    public Handler timerHandler = new Handler();
    public Runnable timerRunnable = new Runnable(){

        @Override
        public void run() {
            if (running) {
                long millis = System.currentTimeMillis() - startTime;
                if (millis > endTime)
                {
                    running = false;
                    millis = 0;
                }
                int seconds = (int) millis / 1000;
                setText(Integer.toString(seconds) + "."+Integer.toString((int)(millis/10)%100));
                timerHandler.postDelayed(this, 50);
            }
        }
    };


    public Timer(Context ctx, AttributeSet attrs)
    {
        super(ctx,attrs);

        setBackgroundColor(0xf0f0f0);
        setText("0");
    }

    public Timer(Context ctx, AttributeSet attrs, int defStyle)
    {
        super(ctx,attrs, defStyle);

        setBackgroundColor(0xf0f0f0);
        setText("0");
    }
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

    }
    public void startTimer()
    {
        startTime = System.currentTimeMillis();
        Log.e(getClass().getName(), "timer max: " + Long.toString(endTime));
        timerHandler.postDelayed(timerRunnable, 0);
        running = true;
    }
    public void stopTimer()
    {
        running = false;
        timerHandler.removeCallbacks(timerRunnable);
    }
    public void resetTimer()
    {
        startTime = System.currentTimeMillis();
        endTime = System.currentTimeMillis();
    }
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
