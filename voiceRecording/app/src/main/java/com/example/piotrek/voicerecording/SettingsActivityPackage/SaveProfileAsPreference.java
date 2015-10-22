package com.example.piotrek.voicerecording.SettingsActivityPackage;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Piotrek on 2015-10-23.
 */
public class SaveProfileAsPreference extends EditTextPreference {


    public SaveProfileAsPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SaveProfileAsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SaveProfileAsPreference(Context context) {
        super(context);
    }

    @Override
    public void onDialogClosed(boolean positiveResult)
    {
        Log.i(getClass().getName(),"onDialogClosed");
        if (positiveResult == true)
        {
            Toast.makeText(getContext(), "Profile saved", Toast.LENGTH_SHORT).show();
        }
    }
}
