package com.project.john.bef.component;

public class MembershipItem {
    public int mId;
    public String mEmail, mPw, mName, mBirth, mCity, mJob, mSex;

    public MembershipItem(int id, String email, String pw, String name, String birth, String city,
                          String job, String sex) {
        mId = id;
        mEmail = email;
        mPw = pw;
        mName = name;
        mBirth = birth;
        mCity = city;
        mJob = job;
        mSex = sex;
    }
}
