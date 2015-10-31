package com.example.piotrek.voicerecording.Tools;

import android.media.AudioFormat;
import android.util.Log;

import com.example.piotrek.voicerecording.Enumerators.FilterTypeEnum;
import com.example.piotrek.voicerecording.Enumerators.UnifyEnum;

import org.w3c.dom.Document;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Piotrek on 2015-10-22.
 */
public class Settings {
    private static Settings instance = null;

    private List<Profile> profiles = null;
    private Profile activeProfile = null;
    private SipConfiguration sipConfiguration =null;
    private int activeProfileIndex;

    public Settings()
    {
        profiles = Profile.readFromXML();
        sipConfiguration = new SipConfiguration();
//        activeProfile = new Profile();
        activeProfileIndex = 0;
        activeProfile = new Profile(profiles.get(activeProfileIndex));

    }

    public CharSequence[] getProfileListAsCharSequence() {
        CharSequence[] result = null;
        if (profiles != null) {
            result = new CharSequence[profiles.size()];
            for (int i = 0; i < result.length; ++i) {
                result[i] = profiles.get(i).getProfileName();
            }
        }
        return result;
    }

    public static Settings getInstance() {
        if (instance == null)
            instance = new Settings();
        return instance;
    }

    public int getCurSampleRate() {
        return getInstance().getCurProfile().getVoiceConfiguration().getAudioTrackSampleRate();
//        return 16000;
    }

    public int getCurChannelConfiguration() {
        return getInstance().getCurProfile().getVoiceConfiguration().getAudioTrackChannels();
//        return AudioFormat.CHANNEL_OUT_MONO;
    }

    public int getCurAudioEncoding() {
        return getInstance().getCurProfile().getVoiceConfiguration().getAudioTrackEncoding();
//        return AudioFormat.ENCODING_PCM_8BIT;
    }

    public FilterTypeEnum getCurFilterType() {
        return getInstance().getCurProfile().getFilterConfiguration().getFilterType();
//        return FilterTypeEnum.BlurFilter;
    }

    public UnifyEnum getCurUnifyMode() {
        return getInstance().getCurProfile().getFilterConfiguration().getUnifyMode();
//        return UnifyEnum.Linear;
    }

    public float getCurScaleFactor() {
        return getInstance().getCurProfile().getFilterConfiguration().getScaleFactor();
//        return 1.1f;
    }

    public int getCurBlurRange() {
        return getInstance().getCurProfile().getFilterConfiguration().getBlurRange();
//        return 10;
    }

    public void setCurSampleRate(int newSampleRate) {
        getCurProfile().getVoiceConfiguration().setAudioTrackSampleRate(newSampleRate);
    }

    public void setCurChannelConfiguration(int newChannelConf) {
        getCurProfile().getVoiceConfiguration().setAudioTrackChannels(newChannelConf);
    }

    public void setCurAudioEncoding(int newEncoding) {
        getCurProfile().getVoiceConfiguration().setAudioTrackEncoding(newEncoding);
    }

    public void setCurFilterType(FilterTypeEnum newFilterType) {
        getCurProfile().getFilterConfiguration().setFilterType(newFilterType);
    }

    public void setCurScaleFactor(float newScaleFactor) {
        getCurProfile().getFilterConfiguration().setScaleFactor(newScaleFactor);
    }

    public void setCurBlurRange(int newBlurRange) {
        getCurProfile().getFilterConfiguration().setBlurRange(newBlurRange);
    }

    public void setCurUnifyMode(UnifyEnum newUnifyMode) {
        getCurProfile().getFilterConfiguration().setUnifyMode(newUnifyMode);
    }

    public String getCurProfileName() {
        return getCurProfile().getProfileName();
    }


    public Profile getCurProfile() {
        return getInstance().activeProfile;
    }



    public int getActiveProfileIndex() {
        return activeProfileIndex;
    }

    public void setActiveProfileIndex(int activeProfileIndex) {
        this.activeProfileIndex = activeProfileIndex;
        this.activeProfile = new Profile(profiles.get(activeProfileIndex));
    }
    public void saveCurrentNewProfile(String profileName)
    {
        activeProfile.setProfileName(profileName);
        profiles.add(activeProfile);
        activeProfileIndex = profiles.size()-1;
        saveSettingsInXML();

    }
    public void saveCurrentExistingProfile()
    {
        profiles.remove(activeProfileIndex);
        profiles.add(activeProfileIndex,activeProfile);
        saveSettingsInXML();
    }
    public void saveSettingsInXML()
    {
        try{
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.newDocument();



        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    public SipConfiguration getSipConfiguration() {
        return sipConfiguration;
    }


    public void log(String tag, String text)
    {
        Log.i(tag,text);
    }
}
