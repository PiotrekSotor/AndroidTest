package com.example.piotrek.voicerecording.WavePackage;

/**
 * Created by Piotrek on 2015-10-20.
 */
public class FilterConfiguration {

    private static FilterConfiguration instance = null;

    private UnifyEnum unifyMode = UnifyEnum.Linear;

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



}
