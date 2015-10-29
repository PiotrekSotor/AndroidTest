package com.example.piotrek.voicerecording.Tools;

import android.preference.Preference;

/**
 * Created by Piotrek on 2015-10-29.
 */
public class Point implements Comparable<Point> {
    private int frequency;
    private float value;

    public Point() {
        frequency = 0;
        value = 0;

    }

    public Point(int frequency, float value) {
        this.frequency = frequency;
        this.value = value;
    }


    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }


    @Override
    public int compareTo(Point another) {
        if (frequency < another.frequency)
            return -1;
        if (frequency > another.frequency)
            return 1;
        return 0;
    }
}
