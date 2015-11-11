package com.example.piotrek.voicerecording.SettingsActivityPackage;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.os.Build;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;

import com.example.piotrek.voicerecording.Enumerators.FilterTypeEnum;
import com.example.piotrek.voicerecording.Enumerators.UnifyEnum;
import com.example.piotrek.voicerecording.R;
import com.example.piotrek.voicerecording.Tools.Settings;

/**
 * Created by Piotrek on 2015-10-22.
 */
public class MyListPreference extends ListPreference implements  Preference.OnPreferenceChangeListener {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyListPreference(Context context) {
        super(context);
        init();
    }

    public  void init() {
        if (!Settings.getInstance().getPreferencesList().contains(this))
        Settings.getInstance().getPreferencesList().add(this);
        setOnPreferenceChangeListener(this);
        Log.i(getClass().getName(),"init()" + getKey());

        Context ctx = getContext();
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        if (getKey().equalsIgnoreCase(getContext().getResources().getResourceEntryName(R.string.list_pref_profile_load_key))) {
            CharSequence[] profileNames = Settings.getInstance().getProfileListAsCharSequence();
            setEntries(profileNames);
            setEntryValues(profileNames);
            setValueIndex(Settings.getInstance().getActiveProfileIndex());
            sharedPreferences.putString("list_pref_profile_load_key",Settings.getInstance().getCurProfileName());
        }
        else if (this.getKey().equals(ctx.getString(R.string.list_pref_filter_type_key)))
        {
            if (Settings.getInstance().getCurFilterType().equals(FilterTypeEnum.BlurFilter)) {
                setValueIndex(findIndexOfValue("blur"));
                sharedPreferences.putString(getKey(), "blur");
            }
            else if (Settings.getInstance().getCurFilterType().equals(FilterTypeEnum.ScaleFilter)){
                setValueIndex(findIndexOfValue("scale"));
                sharedPreferences.putString(getKey(),"scale");
            }

            else if (Settings.getInstance().getCurFilterType().equals(FilterTypeEnum.CapacityFilter))
            {
                setValueIndex(findIndexOfValue("capacity"));
                sharedPreferences.putString(getKey(), "capacity");
            }
        }
        else if (this.getKey().equals(ctx.getString(R.string.list_pref_filter_unify_mode_key)))
        {
            if (Settings.getInstance().getCurUnifyMode().equals(UnifyEnum.Linear))
            {
                setValueIndex(findIndexOfValue("linear"));
                sharedPreferences.putString(getKey(), "linear");
            }
            else if (Settings.getInstance().getCurUnifyMode().equals(UnifyEnum.Trigonometric))
            {
                setValueIndex(findIndexOfValue("trigonometric"));
                sharedPreferences.putString(getKey(), "trigonometric");
            }

        }
//        Profile category
        else if (this.getKey().equals(ctx.getString(R.string.list_pref_profile_load_key)))
        {
            try{

                setValueIndex(findIndexOfValue(Settings.getInstance().getCurProfileName()));
                sharedPreferences.putString(getKey(), Settings.getInstance().getCurProfileName());
            }
            catch (ArrayIndexOutOfBoundsException ex)
            {
                Log.e(getClass().getName(),"ArrayIndexOutOfBoundException: profile.size: " + Integer.toString(Settings.getInstance().getProfileListAsCharSequence().length));
            }
        }
//         Voice config category
        else if (this.getKey().equals(ctx.getString(R.string.list_pref_voice_channel_key)))
        {

            if (Settings.getInstance().getCurChannelConfiguration() == AudioFormat.CHANNEL_OUT_MONO)
            {
                setValueIndex(findIndexOfValue("mono"));
                sharedPreferences.putString(getKey(), "mono");
            }
            else if (Settings.getInstance().getCurChannelConfiguration() == AudioFormat.CHANNEL_OUT_STEREO)
            {
                setValueIndex(findIndexOfValue("stereo"));
                sharedPreferences.putString(getKey(), "stereo");
            }
        }
        else if (this.getKey().equals(ctx.getString(R.string.list_pref_voice_sampling_frequency_key)))
        {
            if (Settings.getInstance().getCurSampleRate() == 8000)
                setValueIndex(findIndexOfValue("8000"));
            else if (Settings.getInstance().getCurSampleRate() == 16000)
                setValueIndex(findIndexOfValue("16000"));
            else if (Settings.getInstance().getCurSampleRate() == 44100)
                setValueIndex(findIndexOfValue("44100"));
            sharedPreferences.putString(getKey(), Integer.toString(Settings.getInstance().getCurSampleRate()));
        }
        sharedPreferences.commit();
        if (findIndexOfValue(getValue()) == -1)
        {
            Log.e(getClass().getName(),getKey());
        }
        else
            setSummary(getEntries()[findIndexOfValue(getValue())]);

    }




    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // korzystanie z sharedPreferenes do odczytu czegokolwiek jest bez sensu, bo jeszcze nie ma tam nowych wartosci
        String string;
        int selectedIndex = findIndexOfValue(newValue.toString());
        string = getEntries()[selectedIndex].toString();
        Log.i(getClass().getName(), "onPreferenceChange entries: " + string);
        setSummary(string);

        Context ctx = getContext();
//         Filter config category
        if (preference.getKey().equals(ctx.getString(R.string.list_pref_filter_type_key)))
        {
            if (string.equals("Blur"))
                Settings.getInstance().setCurFilterType(FilterTypeEnum.BlurFilter);
            else if (string.equals("Scale"))
                Settings.getInstance().setCurFilterType(FilterTypeEnum.ScaleFilter);
            else if (string.equals("Capacity"))
                Settings.getInstance().setCurFilterType(FilterTypeEnum.CapacityFilter);
        }
        else if (preference.getKey().equals(ctx.getString(R.string.list_pref_filter_unify_mode_key)))
        {
            if (string.equals("Linear"))
                Settings.getInstance().setCurUnifyMode(UnifyEnum.Linear);
            else if (string.equals("Trigonometric"))
                Settings.getInstance().setCurUnifyMode(UnifyEnum.Trigonometric);
        }
//        Profile category
        else if (preference.getKey().equals(ctx.getString(R.string.list_pref_profile_load_key)))
        {
            Log.i(getClass().getName(),"Selected progile: " + selectedIndex);
            Log.i(getClass().getName(),"Old profile name: " + Settings.getInstance().getCurProfile().getProfileName());
            Settings.getInstance().setActiveProfileIndex(selectedIndex);
            Log.i(getClass().getName(), "New profile name: " + Settings.getInstance().getCurProfile().getProfileName());


        }
//         Voice config category
        else if (preference.getKey().equals(ctx.getString(R.string.list_pref_voice_channel_key)))
        {
            if (string.equals("Mono"))
                Settings.getInstance().setCurChannelConfiguration(AudioFormat.CHANNEL_OUT_MONO);
            else if (string.equals("Stereo"))
                Settings.getInstance().setCurChannelConfiguration(AudioFormat.CHANNEL_OUT_STEREO);
        }
        else if (preference.getKey().equals(ctx.getString(R.string.list_pref_voice_encoding_key)))
        {
            if (string.equals("8 bit PCM"))
                Settings.getInstance().setCurAudioEncoding(AudioFormat.ENCODING_PCM_8BIT);
            else if (string.equals("16 bit PCM"))
                Settings.getInstance().setCurAudioEncoding(AudioFormat.ENCODING_PCM_16BIT);
        }
        else if (preference.getKey().equals(ctx.getString(R.string.list_pref_voice_sampling_frequency_key)))
        {
            if (string.equals("8000 Hz"))
                Settings.getInstance().setCurSampleRate(8000);
            else if (string.equals("16000 Hz"))
                Settings.getInstance().setCurSampleRate(16000);
            else if (string.equals("44100 Hz"))
                Settings.getInstance().setCurSampleRate(44100);
        }

        return true;
    }
}
