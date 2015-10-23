package com.example.piotrek.voicerecording.SettingsActivityPackage;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;

import com.example.piotrek.voicerecording.R;

/**
 * Created by Piotrek on 2015-10-22.
 */
public class MyListPreference extends ListPreference implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
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
    private void init()
    {
        setOnPreferenceClickListener(this);
        setOnPreferenceChangeListener(this);
        if (getKey().equalsIgnoreCase(getContext().getResources().getResourceEntryName(R.string.list_pref_profile_load_key)))
        {
            CharSequence[] asd = new CharSequence[2];
            for (int i=0;i<2;++i)
                asd[i] = "asdfg";
            setEntryValues(asd);
            for (int i=0;i<2;++i)
                asd[i] = "asdfg";
            setEntries(asd);
        }


    }





    @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.e(getClass().getName(), "onPreferenceClickListener: "+preference.getKey());
        Log.e(getClass().getName(), "onPreferenceClickListener: "+getContext().getResources().getResourceEntryName(R.string.list_pref_profile_load_key));

        if (preference.getKey().equalsIgnoreCase(getContext().getResources().getResourceEntryName(R.string.list_pref_profile_load_key)))
        {

            CharSequence[] asd = new CharSequence[2];
            for (int i=0;i<2;++i)
                asd[i] = "asdfg";
            setEntryValues(asd);
            for (int i=0;i<2;++i)
                asd[i] = "asdfg";
            setEntries(asd);
        }
        for (int i=0;i<getEntryValues().length;++i)
            Log.i(getClass().getName(),getEntries()[i].toString());

        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String string;
        string = getEntries()[findIndexOfValue(newValue.toString())].toString();
        Log.i(getClass().getName(),"onPreferenceChange entries: "+string);

        setSummary(string);

        string = preference.getKey();
        Log.i(getClass().getName(), "onPreferenceChange getKey(): " + string);

        String[] array;
        array = getContext().getResources().getStringArray(R.array.filter_type_array);
        Log.i(getClass().getName(), "onPreferenceChange getKey(): " + array[0]);

        array = getContext().getResources().getStringArray(R.array.filter_type_array_values);
        Log.i(getClass().getName(), "onPreferenceChange getKey(): " + array[0]);


        return true;
    }
}
