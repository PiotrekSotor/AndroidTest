package Tools;


import Enumerators.FilterTypeEnum;
import Enumerators.UnifyEnum;

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
        tmp.getFilterConfiguration().setUnifyMode(UnifyEnum.Linear);
        tmp.getFilterConfiguration().setScaleFactor(1.1f);
        tmp.getFilterConfiguration().setFilterType(FilterTypeEnum.ScaleFilter);
        tmp.getFilterConfiguration().setBlurRange(2);
        tmp.getVoiceConfiguration().setAudioTrackEncoding(8);
        tmp.getVoiceConfiguration().setAudioTrackSampleRate(8000);
        tmp.getVoiceConfiguration().setAudioTrackChannels(1);
        result.add(tmp);
        tmp = new Profile();
        tmp.profileName = "My profile 2";
        tmp.getFilterConfiguration().setUnifyMode(UnifyEnum.Trigonometric);
        tmp.getFilterConfiguration().setScaleFactor(1.1f);
        tmp.getFilterConfiguration().setFilterType(FilterTypeEnum.ScaleFilter);
        tmp.getFilterConfiguration().setBlurRange(10);
        tmp.getVoiceConfiguration().setAudioTrackEncoding(16);
        tmp.getVoiceConfiguration().setAudioTrackSampleRate(8000);
        tmp.getVoiceConfiguration().setAudioTrackChannels(1);
        result.add(tmp);
        return result;
    }

    public Profile()
    {
        filterConfiguration = new FilterConfiguration();
        voiceConfiguration = new VoiceConfiguration();
        profileName = "Default name";
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
