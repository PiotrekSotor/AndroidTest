package com.example.piotrek.voicerecording.SettingsActivityPackage;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

import com.example.piotrek.voicerecording.R;

import java.lang.reflect.AccessibleObject;

/**
 * Created by Piotrek on 2015-10-21.
 */
public class SecondSettingsActivity extends Activity implements Preference.OnPreferenceClickListener{

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Log.e(this.getClass().getName(), "On Create");
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        if (getResources().getString(R.string.list_pref_filter_unify_mode_key).equals(preference.getKey()))
        {
            Toast.makeText(this,"kliknięto na load profile :D",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            Log.e(this.getClass().getName(), "On Create");
            addPreferencesFromResource(R.xml.settings);
        }
    }
}