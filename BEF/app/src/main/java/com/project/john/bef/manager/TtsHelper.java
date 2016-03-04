package com.project.john.bef.manager;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.widget.Toast;

import com.project.john.bef.R;
import com.project.john.bef.component.Constant;

import java.util.Timer;
import java.util.TimerTask;

public class TtsHelper {
    public static boolean sIsInit, sIsSupport;
    private TextToSpeech mTTS = null;
    private Context mContext = null;

    Timer mTimer = new Timer( );
    TimerTask mTimerTask = null;
    String mText = null;

    public TtsHelper(Context context, TextToSpeech tts) {
        mContext = context;
        mTTS = tts;
    }

    public void startSpeak(String text) {
        int available = mTTS.isLanguageAvailable(Constant.LANGUAGE);
        if (available < 0) {
            Toast.makeText(mContext, R.string.not_support_lang, Toast.LENGTH_SHORT).show( );
            sIsSupport = false;
        } else {
            sIsSupport = true;
        }
        mText = text;

        if (!sIsInit) {
            Toast.makeText(mContext, R.string.fail_init, Toast.LENGTH_SHORT).show( );
        } else {
            if (!sIsSupport) {
                Toast.makeText(mContext, R.string.not_support_lang, Toast.LENGTH_SHORT).show( );
            } else {
                if (TextUtils.isEmpty(mText)) {
                    Toast.makeText(mContext, R.string.success_init, Toast.LENGTH_SHORT).show( );
                } else {
                    mTTS.setLanguage(Constant.LANGUAGE);
                    mTTS.setPitch(Constant.PITCH / 10.0f);
                    mTTS.setSpeechRate(Constant.RATE / 10.0f);
                    mTTS.speak(mText, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        }
    }

    public void startSpeak2(String text) {
        int available = mTTS.isLanguageAvailable(Constant.LANGUAGE);
        if (available < 0) {
            Toast.makeText(mContext, R.string.not_support_lang, Toast.LENGTH_SHORT).show( );
            sIsSupport = false;
        } else {
            sIsSupport = true;
        }

        mText = text;
        stopTimerTask( );

        mTimerTask = new TimerTask( ) {
            @Override
            public void run( ) {
                if (!sIsInit) {
                    Toast.makeText(mContext, R.string.fail_init, Toast.LENGTH_SHORT).show( );
                } else {
                    if (!sIsSupport) {
                        Toast.makeText(mContext, R.string.not_support_lang, Toast.LENGTH_SHORT)
                             .show( );
                    } else {
                        if (TextUtils.isEmpty(mText)) {
                            Toast.makeText(mContext, R.string.success_init, Toast.LENGTH_SHORT)
                                 .show( );
                        } else {
                            mTTS.setLanguage(Constant.LANGUAGE);
                            mTTS.setPitch(Constant.PITCH / 10.0f);
                            mTTS.setSpeechRate(Constant.RATE / 10.0f);
                            mTTS.speak(mText, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                }
            }
        };
        mTimer = new Timer( );
        mTimer.schedule(mTimerTask, 1000, 3000);
    }

    public void stopTimerTask( ) {
        if (mTimerTask != null) {
            mTimerTask.cancel( );
            mTimerTask = null;
        }
    }
}
