package com.example.piotrek.voicerecording.Tools;

/**
 * Created by Piotrek on 2015-10-23.
 */
public class Profile {
    private FilterConfiguration filterConfiguration;
    private VoiceConfiguration voiceConfiguration;
    private String profileName;

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
