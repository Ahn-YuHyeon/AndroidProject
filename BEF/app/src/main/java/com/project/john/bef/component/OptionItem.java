package com.project.john.bef.component;

public class OptionItem {
    public int mOprHour = 0;
    public int mOprMinute = 0;
    public String mGuideVoice = "";
    public String mRunVoice = "";

    public OptionItem(int oprHour, int oprMinute, String guideVoice, String runVoice) {
        this.setOprHour(oprHour);
        this.setOprMinute(oprMinute);
        this.setGuideVoice(guideVoice);
        this.setRunVoice(runVoice);
    }

    public int getOprHour( ) {
        return mOprHour;
    }

    public void setOprHour(int oprHour) {
        mOprHour = oprHour;
    }

    public int getOprMinute( ) {
        return mOprMinute;
    }

    public void setOprMinute(int oprMinute) {
        mOprMinute = oprMinute;
    }

    public String getGuideVoice( ) {
        return mGuideVoice;
    }

    public void setGuideVoice(String guideVoice) {
        mGuideVoice = guideVoice;
    }

    public String getRunVoice( ) {
        return mRunVoice;
    }

    public void setRunVoice(String runVoice) {
        mRunVoice = runVoice;
    }
}