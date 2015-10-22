package com.example.piotrek.voicerecording.Tools;

import android.media.AudioFormat;

import com.example.piotrek.voicerecording.Enumerators.FilterTypeEnum;
import com.example.piotrek.voicerecording.Enumerators.UnifyEnum;

import java.util.List;

/**
 * Created by Piotrek on 2015-10-22.
 */
public class Settings {
    private static Settings instance =null;

    private List<Profile> profiles = null;
    private int activeProfileIndex;

    public static Settings getInstance()
    {
        if (instance == null)
            instance = new Settings();
        return instance;
    }
    public int getCurSampleRate()
    {
//        return getInstance().profiles.get(activeProfileIndex).getVoiceConfiguration().getAudioTrackSampleRate();
        return 16000;
    }
    public int getCurChannelConfiguration()
    {
//        return getInstance().profiles.get(activeProfileIndex).getVoiceConfiguration().getAudioTrackChannels();
        return AudioFormat.CHANNEL_OUT_MONO;
    }

    public int getCurAudioEncoding()
    {
//        return getInstance().profiles.get(activeProfileIndex).getVoiceConfiguration().getAudioTrackEncoding();
        return AudioFormat.ENCODING_PCM_8BIT;
    }
    public FilterTypeEnum getCurFilterType()
    {
//        return getInstance().profiles.get(activeProfileIndex).getFilterConfiguration().getFilterType();
        return FilterTypeEnum.BlurFilter;
    }
    public UnifyEnum getCurUnifyMode()
    {
//        return getInstance().profiles.get(activeProfileIndex).getFilterConfiguration().getUnifyMode();
        return UnifyEnum.Linear;
    }
    public float getCurScaleFactor()
    {
//        return getInstance().profiles.get(activeProfileIndex).getFilterConfiguration().getScaleFactor();
        return 1.1f;
    }

    public int getCurBlurRange()
    {
//        return getInstance().profiles.get(activeProfileIndex).getFilterConfiguration().getBlurRange();
        return 10;
    }
}
