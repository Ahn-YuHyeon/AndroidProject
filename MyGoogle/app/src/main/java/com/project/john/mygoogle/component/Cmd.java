package com.project.john.mygoogle.component;

public class Cmd {
    private int mId;
    private String mSerial;
    private String mCmd;

    public Cmd( ) {
    }

    public Cmd(int id, String serial, String cmd) {
        mId = id;
        mCmd = cmd;
        mSerial = serial;
    }

    public int getId( ) {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getSerial( ) {
        return mSerial;
    }

    public void setSerial(String serial) {
        mSerial = serial;
    }

    public String getCmd( ) {
        return mCmd;
    }

    public void setCmd(String cmd) {
        mCmd = cmd;
    }
}
