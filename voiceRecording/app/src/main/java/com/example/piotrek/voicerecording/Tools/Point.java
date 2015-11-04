package com.example.piotrek.voicerecording.Tools;

import android.preference.Preference;

/**
 * Created by Piotrek on 2015-10-29.
 */
public class Point implements Comparable<Point> {
    private int frequency;
    private float value;
    private long id;

    public Point() {
        id = 0;
        frequency = 0;
        value = 0;

    }

    public Point(int frequency, float value) {
        this.frequency = frequency;
        this.value = value;
    }

    public Point (int frequency, float value, long id)
    {
        this.frequency = frequency;
        this.value = value;
        this.id = id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
