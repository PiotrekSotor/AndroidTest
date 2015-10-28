package com.example.piotrek.voicerecording.SettingsActivityPackage;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

/**
 * Created by Piotrek on 2015-10-28.
 */
public class TestSipConnection extends Preference implements Preference.OnPreferenceClickListener {
    public TestSipConnection(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TestSipConnection(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestSipConnection(Context context) {
        super(context);
        init();
    }
    private void init()
    {
        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {


        return false;
    }
}
