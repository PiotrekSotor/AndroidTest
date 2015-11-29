package com.example.piotrek.voicerecording.Tools;

import android.media.AudioFormat;

import com.example.piotrek.voicerecording.Enumerators.CrossfadeEnum;
import com.example.piotrek.voicerecording.Enumerators.FilterTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotrek on 2015-10-23.
 */
public class Profile {
    private FilterConfiguration filterConfiguration;
    private VoiceConfiguration voiceConfiguration;
    private String profileName;


    public static List<Profile> readFromXML()
    {
        List<Profile> result = new ArrayList<Profile>();
        Profile tmp = new Profile();
        tmp.profileName = "My profile 1";
        tmp.getFilterConfiguration().setUnifyMode(CrossfadeEnum.Linear);
        tmp.getFilterConfiguration().setScaleFactor(1.1f);
        tmp.getFilterConfiguration().setFilterType(FilterTypeEnum.BlurFilter);
        tmp.getFilterConfiguration().setBlurRange(10);
        tmp.getFilterConfiguration().getCapacityPoints().clear();
        tmp.getFilterConfiguration().getCapacityPoints().add(new Point(1000, 0.5f));
        tmp.getFilterConfiguration().getCapacityPoints().add(new Point(4000, 0.0f));
        tmp.getFilterConfiguration().getCapacityPoints().add(new Point(2000, 0.3f));
        tmp.getFilterConfiguration().getCapacityPoints().add(new Point(6000, 0.7f));
        tmp.getFilterConfiguration().getCapacityPoints().add(new Point(7000, 0.2f));
        tmp.getVoiceConfiguration().setAudioTrackEncoding(AudioFormat.ENCODING_PCM_8BIT);
        tmp.getVoiceConfiguration().setAudioTrackSampleRate(8000);
        tmp.getVoiceConfiguration().setAudioTrackChannels(AudioFormat.CHANNEL_OUT_MONO);
        result.add(tmp);
        tmp = new Profile();
        tmp.profileName = "My profile 2";
        tmp.getFilterConfiguration().setUnifyMode(CrossfadeEnum.Trigonometric);
        tmp.getFilterConfiguration().setScaleFactor(1.1f);
        tmp.getFilterConfiguration().setFilterType(FilterTypeEnum.ScaleFilter);
        tmp.getFilterConfiguration().setBlurRange(10);
        tmp.getFilterConfiguration().getCapacityPoints().add(new Point(100, 0.5f));
        tmp.getFilterConfiguration().getCapacityPoints().add(new Point(2000,1.0f));
        tmp.getFilterConfiguration().getCapacityPoints().add(new Point(8000,0.5f));
        tmp.getVoiceConfiguration().setAudioTrackEncoding(AudioFormat.ENCODING_PCM_16BIT);
        tmp.getVoiceConfiguration().setAudioTrackSampleRate(8000);
        tmp.getVoiceConfiguration().setAudioTrackChannels(AudioFormat.CHANNEL_OUT_STEREO);
        result.add(tmp);
        return result;
    }

    public Profile()
    {
        filterConfiguration = new FilterConfiguration();
        voiceConfiguration = new VoiceConfiguration();
        profileName = "Default name";
    }

    public Profile (Profile prof)
    {
        this.filterConfiguration = new FilterConfiguration(prof.getFilterConfiguration());
        this.voiceConfiguration = new VoiceConfiguration(prof.getVoiceConfiguration());
        this.profileName = new String(prof.getProfileName().toString());
    }



    public FilterConfiguration getFilterConfiguration() {
        return filterConfiguration;
    }

    public void setFilterConfiguration(FilterConfiguration filterConfiguration) {
        this.filterConfiguration = filterConfiguration;
    }

    public VoiceConfiguration getVoiceConfiguration() {
        return voiceConfiguration;
    }

    public void setVoiceConfiguration(VoiceConfiguration voiceConfiguration) {
        this.voiceConfiguration = voiceConfiguration;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
}
