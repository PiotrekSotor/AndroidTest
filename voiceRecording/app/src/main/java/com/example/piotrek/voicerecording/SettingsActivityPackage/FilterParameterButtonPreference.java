package com.example.piotrek.voicerecording.SettingsActivityPackage;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;

import com.example.piotrek.voicerecording.SettingsActivityPackage.FilterParameterActivityPackage.FilterParameterActivity;

/**
 * Created by Piotrek on 2015-10-29.
 */
public class FilterParameterButtonPreference extends Preference implements Preference.OnPreferenceClickListener {
    public FilterParameterButtonPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FilterParameterButtonPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FilterParameterButtonPreference(Context context) {
        super(context);
        init();
    }
    private void init()
    {
        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.i(getClass().getName(), "onPreferenceClick");
        Intent activity = new Intent(getContext(),FilterParameterActivity.class);
        getContext().startActivity(activity);
        return false;
    }
}
