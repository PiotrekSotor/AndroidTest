package com.example.piotrek.voicerecording.SettingsActivityPackage;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

import com.example.piotrek.voicerecording.R;
import com.example.piotrek.voicerecording.Tools.Settings;

import java.lang.reflect.AccessibleObject;

/**
 * Created by Piotrek on 2015-10-21.
 */
public class SettingsActivity extends Activity {

    ListPreference sampleFrequency = null;
    ListPreference encoding = null;
    ListPreference filterType = null;
    ListPreference unifyMode = null;

    PrefsFragment prefsFragment;
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Log.e(this.getClass().getName(), this.getClass().getName() + " : On Create");
        // miejsce na ustawienie ustawień zgodnie z FilterConfiguration, WaveRecorder, ProfileManager

        getFragmentManager().beginTransaction().replace(android.R.id.content,new PrefsFragment() ).commit();

    }

    public static class PrefsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        @Override
        public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            Log.e(this.getClass().getName(), this.getClass().getName()+" : On Create");
            addPreferencesFromResource(R.xml.settings);


        }

        @Override
        public boolean onPreferenceClick(Preference preference) {

            Toast.makeText(null,this.getClass().getName()+"onPreferenceClickListener", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onStop() {
        Settings.getInstance().clearPreferencesList();
        super.onStop();
    }
}