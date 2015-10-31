package com.example.piotrek.voicerecording.Tools;

import android.util.Log;
import android.widget.Toast;

import com.example.piotrek.voicerecording.Enumerators.FilterTypeEnum;
import com.example.piotrek.voicerecording.Enumerators.UnifyEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Piotrek on 2015-10-20.
 */
public class FilterConfiguration {

//    private static FilterConfiguration instance = null;

    private UnifyEnum unifyMode;
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
        unifyMode = UnifyEnum.Linear;
        filterType = FilterTypeEnum.BlurFilter;

        blurRange = 5;
        scaleFactor = 1.5f;
        if (capacityPoints == null)
            capacityPoints = new ArrayList<Point>();
        capacityPoints.add(new Point(0, 1));
    }
    public FilterConfiguration (FilterConfiguration filter)
    {
        this.capacityPoints = new ArrayList<Point>(filter.getCapacityPoints());
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
        if (capacityPoints != null)
            backupCapacityPoints = new ArrayList<Point>(capacityPoints);
        backupBlurRange = blurRange;
        backupScaleFactor = scaleFactor;

    }

    public void restoreBackup() {
        if (backupCapacityPoints != null)
            capacityPoints = new ArrayList<Point>(backupCapacityPoints);
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


    public UnifyEnum getUnifyMode() {
        return unifyMode;
    }

    public void setUnifyMode(UnifyEnum unifyMode) {
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


}
