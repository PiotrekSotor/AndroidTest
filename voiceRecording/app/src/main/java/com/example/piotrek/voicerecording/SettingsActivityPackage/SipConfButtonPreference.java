package com.example.piotrek.voicerecording.SettingsActivityPackage;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Piotrek on 2015-10-28.
 */
public class SipConfButtonPreference extends Preference implements Preference.OnPreferenceClickListener {
    public SipConfButtonPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SipConfButtonPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SipConfButtonPreference(Context context) {
        super(context);
        init();
    }
    private void init()
    {
        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        Log.i(getClass().getName(), "SipConfButtonPreference onClickListener");
        return false;
    }
}
