package com.example.piotrek.voicerecording.Tools;

import android.util.Log;

import com.example.piotrek.voicerecording.Enumerators.CrossfadeEnum;
import com.example.piotrek.voicerecording.Enumerators.FilterTypeEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Piotrek on 2015-10-20.
 */
public class FilterConfiguration {

//    private static FilterConfiguration instance = null;

    private CrossfadeEnum unifyMode;
    private FilterTypeEnum filterType;


    private int blurRange;
    private float scaleFactor;
    private List<Point> capacityPoints;

    private int backupBlurRange;
    private float backupScaleFactor;
    private List<Point> backupCapacityPoints;

    //    public static FilterConfiguration getInstance()
//    {
//        if (instance == null)
//            instance = new FilterConfiguration();
//        return instance;
//    }
    public FilterConfiguration() {
        unifyMode = CrossfadeEnum.Linear;
        filterType = FilterTypeEnum.BlurFilter;

        blurRange = 0;
        scaleFactor = 1.0f;
        if (capacityPoints == null)
            capacityPoints = new ArrayList<Point>();

    }
    public FilterConfiguration (FilterConfiguration filter)
    {
        this.capacityPoints = new ArrayList<Point>();
        for (Point p : filter.getCapacityPoints())
            this.capacityPoints.add(new Point(p.getFrequency(),p.getValue()));
        this.blurRange = filter.blurRange;
        this.scaleFactor = filter.scaleFactor;
        this.unifyMode = filter.unifyMode;
        this.filterType = filter.filterType;
    }

    public void addCapacityPoint(Point newPoint) {
        if (capacityPoints == null)
            capacityPoints = new ArrayList<Point>();
        capacityPoints.add(newPoint);
    }

    public void makeBackup() {
        if (capacityPoints != null) {
            backupCapacityPoints = new ArrayList<Point>();
            for (Point p : capacityPoints)
                backupCapacityPoints.add(new Point(p.getFrequency(),p.getValue()));
        }
        backupBlurRange = blurRange;
        backupScaleFactor = scaleFactor;
        Log.i(getClass().getName(),"makeBackup bbr: " + Integer.toString(backupBlurRange) + " br: " + Integer.toString(blurRange) + " bsf: "+Float.toString(backupScaleFactor) + " sf: " + Float.toString(scaleFactor));
    }

    public void restoreBackup() {
        Log.i(getClass().getName(),"restoreBackup bbr: " + Integer.toString(backupBlurRange) + " br: " + Integer.toString(blurRange) + " bsf: "+Float.toString(backupScaleFactor) + " sf: " + Float.toString(scaleFactor));

        if (backupCapacityPoints != null) {
            for (int i=0;i<backupCapacityPoints.size();++i)
                Log.i(getClass().getName(),"backup: " + Integer.toString(backupCapacityPoints.get(i).getFrequency()) + "  |  " + Float.toString(backupCapacityPoints.get(i).getValue()));
//            Log.i(getClass().getName(),"restoreBackup bcp.size: " + Integer.toString(backupCapacityPoints.size()) + " cp.size: " + Integer.toString(capacityPoints.size()));
            capacityPoints = new ArrayList<Point>(backupCapacityPoints);
            for (int i=0;i<capacityPoints.size();++i)
                Log.i(getClass().getName(),"after backup: " + Integer.toString(capacityPoints.get(i).getFrequency()) + "  |  " + Float.toString(capacityPoints.get(i).getValue()));
        }
        backupCapacityPoints = null;
        blurRange = backupBlurRange;
        scaleFactor = backupScaleFactor;
    }

    public void cleanRestoreBackupPoints() {
        if (backupCapacityPoints != null) {
            backupCapacityPoints.clear();
            backupCapacityPoints = null;
        }
    }

    /**
     * jesli lista jest posortowana to wystarczy sprawdzic czy kolejne frequency sa rozne
     *
     * @return
     */

    public boolean validateCapacityPoint() {
        if (capacityPoints != null) {
            getCapacityPoints(); // just to sort list
            if (capacityPoints.size() == 1)
                return true;
            for (int i = 1; i < capacityPoints.size(); ++i)
                if (capacityPoints.get(i - 1).getFrequency() == capacityPoints.get(i).getFrequency())
                    return false;
            return true;
        }
        return false;
    }


    public CrossfadeEnum getUnifyMode() {
        return unifyMode;
    }

    public void setUnifyMode(CrossfadeEnum unifyMode) {
        this.unifyMode = unifyMode;
    }


    public FilterTypeEnum getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterTypeEnum filterType) {
        this.filterType = filterType;
    }

    public int getBlurRange() {
        return blurRange;
    }

    public void setBlurRange(int blurRange) {
        this.blurRange = blurRange;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public List<Point> getCapacityPoints() {
        if (capacityPoints == null)
        {
            capacityPoints = new ArrayList<Point>();
        }
        Collections.sort(capacityPoints);
        String sorted = "";
        for (int i = 0; i < capacityPoints.size(); ++i)
            sorted = sorted + "  " + Integer.toString(capacityPoints.get(i).getFrequency()) + "|" + Float.toString(capacityPoints.get(i).getValue());
        Log.i(getClass().getName(), sorted);
        return capacityPoints;
    }

    public Point getPointById(long pointId) {
        if (capacityPoints != null)
            for (int i = 0; i < capacityPoints.size(); ++i)
                if (capacityPoints.get(i).getId() == pointId)
                    return capacityPoints.get(i);
        return null;

    }


    public List<Point> getBackupCapacityPoints() {
        return backupCapacityPoints;
    }


    public void setCapacityPoints(List<Point> capacityPoints) {
        this.capacityPoints = capacityPoints;
    }
}
