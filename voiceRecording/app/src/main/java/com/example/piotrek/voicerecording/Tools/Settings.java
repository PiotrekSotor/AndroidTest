package com.example.piotrek.voicerecording.Tools;

import android.media.AudioFormat;
import android.os.Environment;
import android.util.Log;

import com.example.piotrek.voicerecording.Enumerators.FilterTypeEnum;
import com.example.piotrek.voicerecording.Enumerators.UnifyEnum;
import com.example.piotrek.voicerecording.SettingsActivityPackage.FilterParameterActivityPackage.PreparedCapacityFilter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Piotrek on 2015-10-22.
 */
public class Settings {
    private static Settings instance = null;

    private List<Profile> profiles = null;
    //    private List<List<Point>> preparedPointsLists = null;
    private List<PreparedCapacityFilter> preparedCapacityFilters = null;
    private Profile activeProfile = null;
    private SipConfiguration sipConfiguration = null;
    private int activeProfileIndex;

    private String xmlProfilesFilePath;
    private String xmlPrepareFiltersFilePath;

    private void xmlFilePathInit() {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            Log.i(getClass().getName(), "SD is present");
            xmlProfilesFilePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
            xmlPrepareFiltersFilePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        } else {
            Log.e(getClass().getName(), "SD is not present");
            xmlProfilesFilePath = Environment.getDataDirectory().getAbsolutePath().toString();
            xmlPrepareFiltersFilePath = Environment.getDataDirectory().getAbsolutePath().toString();

        }

        xmlProfilesFilePath += "/profiles.xml";
        xmlPrepareFiltersFilePath += "/preparedFilters.xml";
        Log.i(getClass().getName(), "xmlProfilesFilePath: " + xmlProfilesFilePath);
    }

    public Settings() {
        xmlFilePathInit();
//        profiles = Profile.readFromXML();
        readProfilesFromXML();
        readPreparedFiltersFromXML();
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

    public List<String> getPreparedCapacityFilterNamesAsStringList() {
        List<String> result = null;
        if (preparedCapacityFilters != null) {
            result = new ArrayList<String>();
            result.add("");
            for (PreparedCapacityFilter pcf : preparedCapacityFilters)
                result.add(pcf.getName());
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
        getCurProfile().getVoiceConfiguration().setAudioTrackEncoding(AudioFormat.ENCODING_PCM_16BIT);
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

    public void setCurCapacityPoints(List<Point> points) {
        getInstance().getCurProfile().getFilterConfiguration().setCapacityPoints(points);
    }

    public List<Point> getCurCapacityPoints()
    {
        return getInstance().getCurProfile().getFilterConfiguration().getCapacityPoints();
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

    public void saveCurrentNewProfile(String profileName) {
        if (activeProfile == null)
            Log.e(getClass().getName(), "activeProfile == null");
        if (profileName == null)
            Log.e(getClass().getName(), "profileName == null");
        if (profiles == null)
            Log.e(getClass().getName(), "profiles == null");
        activeProfile.setProfileName(new String(profileName.toString()));
        profiles.add(activeProfile);
        activeProfileIndex = profiles.size() - 1;
        saveProfilesInXML();

    }

    public void saveCurrentExistingProfile() {
        profiles.remove(activeProfileIndex);
        profiles.add(activeProfileIndex, activeProfile);
        saveProfilesInXML();
    }

    public void saveProfilesInXML() {
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("profiles");
            doc.appendChild(rootElement);
            rootElement.setAttribute("activeProfile", Integer.toString(activeProfileIndex));
            for (int i = 0; i < profiles.size(); ++i) {
                Element profile = doc.createElement("profile");
                rootElement.appendChild(profile);


//                  profileName

                Element profileName = doc.createElement("profileName");
                profileName.appendChild(doc.createTextNode(getInstance().profiles.get(i).getProfileName()));
                profile.appendChild(profileName);

//                voiceConfiguration
                Element voiceConfiguration = doc.createElement("voiceConfiguration");
                Element sampleRate = doc.createElement("sampleRate");
                sampleRate.appendChild(doc.createTextNode(Integer.toString(getInstance().profiles.get(i).getVoiceConfiguration().getAudioTrackSampleRate())));
                voiceConfiguration.appendChild(sampleRate);
                Element channels = doc.createElement("channels");
                channels.appendChild(doc.createTextNode(Integer.toString(getInstance().profiles.get(i).getVoiceConfiguration().getAudioTrackChannels())));
                voiceConfiguration.appendChild(channels);
//                Element encoding = doc.createElement("encoding");
//                encoding.appendChild(doc.createTextNode(Integer.toString(getInstance().profiles.get(i).getVoiceConfiguration().getAudioTrackEncoding())));
//                voiceConfiguration.appendChild(encoding);
                profile.appendChild(voiceConfiguration);

//                filterConfiguration
                Element filterConfiguration = doc.createElement("filterConfiguration");
                Element blurRange = doc.createElement("blurRange");
                blurRange.appendChild(doc.createTextNode(Integer.toString(getInstance().profiles.get(i).getFilterConfiguration().getBlurRange())));
                filterConfiguration.appendChild(blurRange);
                Element scaleFactor = doc.createElement("scaleFactor");
                scaleFactor.appendChild(doc.createTextNode(Float.toString(getInstance().profiles.get(i).getFilterConfiguration().getScaleFactor())));
                filterConfiguration.appendChild(scaleFactor);
                Element capacityPoints = doc.createElement("capacityPoints");
                List<Point> points = new ArrayList<Point>(getInstance().profiles.get(i).getFilterConfiguration().getCapacityPoints());
                for (Point p : points) {
                    Element point = doc.createElement("point");
                    point.setAttribute("frequency", Integer.toString(p.getFrequency()));
                    point.appendChild(doc.createTextNode(Float.toString(p.getValue())));
                    capacityPoints.appendChild(point);
                }
                filterConfiguration.appendChild(capacityPoints);
                profile.appendChild(filterConfiguration);

//                activeSettings
                Element activeSettings = doc.createElement("activeSettings");
                Element unifyMode = doc.createElement("unifyMode");
                unifyMode.appendChild(doc.createTextNode(getInstance().profiles.get(i).getFilterConfiguration().getUnifyMode().toString()));
                activeSettings.appendChild(unifyMode);
                Element filterType = doc.createElement("filterType");
                filterType.appendChild(doc.createTextNode(getInstance().profiles.get(i).getFilterConfiguration().getFilterType().toString()));
                activeSettings.appendChild(filterType);
                profile.appendChild(activeSettings);
            }
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            DOMSource source = new DOMSource(doc);
            File file = new File(xmlProfilesFilePath);
            Log.e(getClass().getName(), "file: " + file);
            StreamResult result = new StreamResult(file);
            t.transform(source, result);
            Log.e(getClass().getName(), "save profiles in xml: " + file.getAbsolutePath());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    public void readProfilesFromXML() {
        try {
            profiles = new ArrayList<Profile>();
            File file = new File(xmlProfilesFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList profiles = doc.getElementsByTagName("profile");

            for (int i = 0; i < profiles.getLength(); ++i) {
                Node profile = profiles.item(i);
                if (profile.getNodeType() == Node.ELEMENT_NODE) {
                    Profile newProfile = new Profile();
                    newProfile.getFilterConfiguration().getCapacityPoints().clear();
                    NodeList profileChildren = profile.getChildNodes();
                    Log.i(getClass().getName(), "profileChildren.size: " + Integer.toString(profileChildren.getLength()) + "   " + profileChildren.item(0).getTextContent());

                    newProfile.setProfileName(profileChildren.item(0).getTextContent());

                    NodeList voiceConfigurationChildren = profileChildren.item(1).getChildNodes();
                    VoiceConfiguration voiceConfiguration = new VoiceConfiguration();
                    voiceConfiguration.setAudioTrackSampleRate(Integer.parseInt(voiceConfigurationChildren.item(0).getTextContent()));
                    voiceConfiguration.setAudioTrackChannels(Integer.parseInt(voiceConfigurationChildren.item(1).getTextContent()));
//                    voiceConfiguration.setAudioTrackEncoding(Integer.parseInt(voiceConfigurationChildren.item(2).getTextContent()));
                    newProfile.setVoiceConfiguration(voiceConfiguration);

                    Element element = (Element) profileChildren.item(2);
                    NodeList filterConfigurationChildren = profileChildren.item(2).getChildNodes();
                    FilterConfiguration filterConfiguration = new FilterConfiguration();
                    filterConfiguration.setBlurRange(Integer.parseInt(filterConfigurationChildren.item(0).getTextContent()));
                    filterConfiguration.setScaleFactor(Float.parseFloat(filterConfigurationChildren.item(1).getTextContent()));
                    NodeList points = filterConfigurationChildren.item(2).getChildNodes();
                    for (int j = 0; j < points.getLength(); ++j) {
                        Element point = (Element) points.item(j);
                        Log.i(getClass().getName(), "point.getAttribute(\"frequency\") " + point.getTagName() + "  " + point.getTextContent());
                        filterConfiguration.addCapacityPoint(new Point(Integer.parseInt(point.getAttribute("frequency")), Float.parseFloat(point.getTextContent())));
                    }

                    NodeList activeSettingsChildren = profileChildren.item(3).getChildNodes();
                    String unifyMode = activeSettingsChildren.item(0).getTextContent();
                    String filterType = activeSettingsChildren.item(1).getTextContent();
                    if (unifyMode.equals(UnifyEnum.Linear.toString()))
                        filterConfiguration.setUnifyMode(UnifyEnum.Linear);
                    else if (unifyMode.equals(UnifyEnum.Trigonometric.toString()))
                        filterConfiguration.setUnifyMode(UnifyEnum.Trigonometric);
                    if (filterType.equals(FilterTypeEnum.ScaleFilter.toString()))
                        filterConfiguration.setFilterType(FilterTypeEnum.ScaleFilter);
                    else if (filterType.equals(FilterTypeEnum.BlurFilter.toString()))
                        filterConfiguration.setFilterType(FilterTypeEnum.BlurFilter);
                    else if (filterType.equals(FilterTypeEnum.CapacityFilter.toString()))
                        filterConfiguration.setFilterType(FilterTypeEnum.CapacityFilter);

                    newProfile.setFilterConfiguration(filterConfiguration);

                    this.profiles.add(newProfile);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        nie wczytano żadnego profilu, wiec ustawiamy domyślny
        if (this.profiles.size() == 0) {
            Profile tmp = new Profile();
            tmp.setProfileName("Default");
            tmp.getFilterConfiguration().setUnifyMode(UnifyEnum.Linear);
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
            profiles.add(tmp);
        }
        setActiveProfileIndex(0);
        for (Profile p : profiles)
            Log.i(getClass().getName(), p.getProfileName() + " " + p.getFilterConfiguration().getCapacityPoints().size());
    }

    public void readPreparedFiltersFromXML() {
        preparedCapacityFilters = new ArrayList<PreparedCapacityFilter>();
        preparedCapacityFilters.add(new PreparedCapacityFilter("empty"));
        try {

            File file = new File(xmlPrepareFiltersFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList pointSetList = doc.getElementsByTagName("pointset");
            Log.i(getClass().getName(), "pointSetList.size: " + Integer.toString(pointSetList.getLength()));
            for (int i = 0; i < pointSetList.getLength(); ++i) {
                PreparedCapacityFilter preparedCapacityFilter = new PreparedCapacityFilter();

                Element el = (Element) pointSetList.item(i);
                preparedCapacityFilter.setName(el.getAttribute("name"));

                NodeList points = pointSetList.item(i).getChildNodes();
                Log.i(getClass().getName(), "pointSet.size: " + Integer.toString(points.getLength()));
                for (int j = 0; j < points.getLength(); ++j) {
                    Element point = (Element) points.item(j);
                    preparedCapacityFilter.addCapacityPoint(new Point(Integer.parseInt(point.getAttribute("frequency")), Float.parseFloat(point.getTextContent())));
                    Log.i(getClass().getName(), "filterPoint: " + point.getAttribute("frequency") + " | " + point.getTextContent());
                }
                preparedCapacityFilters.add(preparedCapacityFilter);

            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (preparedCapacityFilters.size() == 1) {
            if (preparedCapacityFilters == null)
                preparedCapacityFilters = new ArrayList<PreparedCapacityFilter>();
            PreparedCapacityFilter pcf = new PreparedCapacityFilter();
            pcf.setName("Default");
            pcf.addCapacityPoint(new Point(100, 0.0f));
            pcf.addCapacityPoint(new Point(500, 0.5f));
            pcf.addCapacityPoint(new Point(800, 1.0f));
            pcf.addCapacityPoint(new Point(8000, 1.0f));
            pcf.addCapacityPoint(new Point(16000, 0.0f));
            preparedCapacityFilters.add(pcf);

            pcf = new PreparedCapacityFilter();
            pcf.setName("Default 2");
            pcf.addCapacityPoint(new Point(1000, 0.0f));
            pcf.addCapacityPoint(new Point(5000, 0.5f));
            pcf.addCapacityPoint(new Point(8000, 1.0f));
            preparedCapacityFilters.add(pcf);
        }
    }

    public SipConfiguration getSipConfiguration() {
        return sipConfiguration;
    }


    public void log(String tag, String text) {
        Log.i(tag, text);
    }

    public List<PreparedCapacityFilter> getPreparedCapacityFilters() {
        return preparedCapacityFilters;
    }
}
