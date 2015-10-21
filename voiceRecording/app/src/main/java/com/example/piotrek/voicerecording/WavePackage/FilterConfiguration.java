package com.example.piotrek.voicerecording.WavePackage;

import com.example.piotrek.voicerecording.Enumerators.FilterTypeEnum;
import com.example.piotrek.voicerecording.Enumerators.UnifyEnum;

/**
 * Created by Piotrek on 2015-10-20.
 */
public class FilterConfiguration {

    private static FilterConfiguration instance = null;

    private UnifyEnum unifyMode = UnifyEnum.Linear;
    private FilterTypeEnum filterType = FilterTypeEnum.BlurFilter;

    private int blurRange = 5;
    private float scaleFactor = 1.5f;

    public static FilterConfiguration getInstance()
    {
        if (instance == null)
            instance = new FilterConfiguration();
        return instance;
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
