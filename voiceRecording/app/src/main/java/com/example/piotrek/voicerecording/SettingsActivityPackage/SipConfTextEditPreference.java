package com.example.piotrek.voicerecording.SettingsActivityPackage;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;

import com.example.piotrek.voicerecording.R;
import com.example.piotrek.voicerecording.Tools.Settings;

/**
 * Created by Piotrek on 2015-10-29.
 */
public class SipConfTextEditPreference extends EditTextPreference implements EditTextPreference.OnPreferenceChangeListener {
    public SipConfTextEditPreference(Context context) {
        super(context);
        init();
    }

    public SipConfTextEditPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SipConfTextEditPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init()
    {
        setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        Context ctx = getContext();
        if (preference.getKey().equals(ctx.getString(R.string.edit_text_sip_conf_username_key)))
        {
            Settings.getInstance().getSipConfiguration().setUsername(newValue.toString());
        }
        else if (preference.getKey().equals(ctx.getString(R.string.edit_text_sip_conf_password_key)))
        {
            Settings.getInstance().getSipConfiguration().setPassword(newValue.toString());
        }
        else if (preference.getKey().equals(ctx.getString(R.string.edit_text_sip_conf_server_key)))
        {
            Settings.getInstance().getSipConfiguration().setServerName(newValue.toString());
        }
        else if (preference.getKey().equals(ctx.getString(R.string.edit_text_sip_conf_port_key)))
        {
            Settings.getInstance().getSipConfiguration().setPort(newValue.toString());
        }
        Log.i(getClass().getName(), "onPreferenceChange : newValue = " + newValue.toString());

        setSummary(newValue.toString());
        return false;
    }
}
