package com.example.piotrek.voicerecording.Tools;

/**
 * Created by Piotrek on 2015-10-23.
 */
public class VoiceConfiguration {
    private int audioTrackSampleRate = 0;
    private int audioTrackChannels = 0;
    private int audioTrackEncoding = 0;

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
