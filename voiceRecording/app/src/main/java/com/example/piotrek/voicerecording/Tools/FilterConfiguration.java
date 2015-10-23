package com.example.piotrek.voicerecording.Tools;

import com.example.piotrek.voicerecording.Enumerators.FilterTypeEnum;
import com.example.piotrek.voicerecording.Enumerators.UnifyEnum;

/**
 * Created by Piotrek on 2015-10-20.
 */
public class FilterConfiguration {

//    private static FilterConfiguration instance = null;

    private UnifyEnum unifyMode;
    private FilterTypeEnum filterType;

    private int blurRange;
    private float scaleFactor;

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
}
