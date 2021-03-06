package Tools;

/**
 * Created by Piotrek on 2015-10-23.
 */
public class VoiceConfiguration {
    private int audioTrackSampleRate;
    private int audioTrackChannels;
    private int audioTrackEncoding;

    public VoiceConfiguration()
    {
        audioTrackChannels = 0;
        audioTrackEncoding = 0;
        audioTrackSampleRate = 0;
    }

    public int getAudioTrackSampleRate() {
        return audioTrackSampleRate;
    }

    public void setAudioTrackSampleRate(int audioTrackSampleRate) {
        this.audioTrackSampleRate = audioTrackSampleRate;
    }

    public int getAudioTrackChannels() {
        return audioTrackChannels;
    }

    public void setAudioTrackChannels(int audioTrackChannels) {
        this.audioTrackChannels = audioTrackChannels;
    }

    public int getAudioTrackEncoding() {
        return audioTrackEncoding;
    }

    public void setAudioTrackEncoding(int audioTrackEncoding) {
        this.audioTrackEncoding = audioTrackEncoding;
    }
}
