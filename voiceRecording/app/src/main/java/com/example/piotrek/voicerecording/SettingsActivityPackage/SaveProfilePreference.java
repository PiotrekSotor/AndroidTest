package com.example.piotrek.voicerecording.SettingsActivityPackage;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Piotrek on 2015-10-23.
 */
public class SaveProfilePreference extends Preference implements Preference.OnPreferenceClickListener {
    public SaveProfilePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SaveProfilePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SaveProfilePreference(Context context) {
        super(context);
        init();
    }
    private void init()
    {
        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.i(getClass().getName(),"onPreferenceClick" );
        Toast.makeText(getContext(), "Profile saved", Toast.LENGTH_SHORT).show();
        return true;
    }
}
