package com.project.john.bef.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.project.john.bef.Main;
import com.project.john.bef.R;
import com.project.john.bef.component.Constant;
import com.project.john.bef.enumeration.LogType;
import com.project.john.bef.enumeration.OptionType;
import com.project.john.bef.manager.Logger;
import com.project.john.bef.component.OptionItem;
import com.project.john.bef.component.OptionDialog;

import java.util.ArrayList;

public class Option extends AppCompatActivity {
    OptionDialog mOpDialog;
    public static ArrayList<OptionItem> sOpItems = null;
    static int[] sBtnTimeIds = new int[Constant.BUTTON_CNT];
    static int[] sBtnGuideIds = new int[Constant.BUTTON_CNT];
    static int[] sBtnRunIds = new int[Constant.BUTTON_CNT];
    static int[] sBtnTestIds = new int[Constant.BUTTON_CNT];
    private ArrayList<String> mResults;
    static int sIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atvt_option);

        mOpDialog = new OptionDialog(this);

        if (sOpItems == null) {
            sOpItems = new ArrayList<OptionItem>( );

            for (int i = 0; i < 9; i++) {
                OptionItem _OptionItem = new OptionItem( );
                sOpItems.add(i, _OptionItem);
            }
        }
        initId( );
    }

    public void initId( ) {
        sBtnTimeIds[0] = R.id.btn_time1;
        sBtnTimeIds[1] = R.id.btn_time2;
        sBtnTimeIds[2] = R.id.btn_time3;
        sBtnTimeIds[3] = R.id.btn_time4;
        sBtnTimeIds[4] = R.id.btn_time5;
        sBtnTimeIds[5] = R.id.btn_time6;
        sBtnTimeIds[6] = R.id.btn_time7;
        sBtnTimeIds[7] = R.id.btn_time8;
        sBtnTimeIds[8] = R.id.btn_time9;

        sBtnGuideIds[0] = R.id.btn_guide1;
        sBtnGuideIds[1] = R.id.btn_guide2;
        sBtnGuideIds[2] = R.id.btn_guide3;
        sBtnGuideIds[3] = R.id.btn_guide4;
        sBtnGuideIds[4] = R.id.btn_guide5;
        sBtnGuideIds[5] = R.id.btn_guide6;
        sBtnGuideIds[6] = R.id.btn_guide7;
        sBtnGuideIds[7] = R.id.btn_guide8;
        sBtnGuideIds[8] = R.id.btn_guide9;

        sBtnRunIds[0] = R.id.btn_run1;
        sBtnRunIds[1] = R.id.btn_run2;
        sBtnRunIds[2] = R.id.btn_run3;
        sBtnRunIds[3] = R.id.btn_run4;
        sBtnRunIds[4] = R.id.btn_run5;
        sBtnRunIds[5] = R.id.btn_run6;
        sBtnRunIds[6] = R.id.btn_run7;
        sBtnRunIds[7] = R.id.btn_run8;
        sBtnRunIds[8] = R.id.btn_run9;

        sBtnTestIds[0] = R.id.btn_test1;
        sBtnTestIds[1] = R.id.btn_test2;
        sBtnTestIds[2] = R.id.btn_test3;
        sBtnTestIds[3] = R.id.btn_test4;
        sBtnTestIds[4] = R.id.btn_test5;
        sBtnTestIds[5] = R.id.btn_test6;
        sBtnTestIds[6] = R.id.btn_test7;
        sBtnTestIds[7] = R.id.btn_test8;
        sBtnTestIds[8] = R.id.btn_test9;
    }

    public void onClick(View v) {
        if (v.getId( ) == R.id.btn_save) {
            int i = 0;
            for (OptionItem _OptionItem : sOpItems) {
                Logger.record(LogType.VERBOSE, ++i + "> " + _OptionItem.mOprHour + ":" +
                                               _OptionItem.mOprMinute + ", " +
                                               _OptionItem.mGuideVoice +
                                               ", " +
                                               _OptionItem.mRunVoice);
            }
            Intent intent = new Intent(this, Main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            for (int i = 0; i < Constant.BUTTON_CNT; i++) {
                if (v.getId( ) == sBtnTestIds[i]) {
                    sIndex = i;
                    testOkgoogle();
                } else {
                    if (v.getId( ) == sBtnTimeIds[i]) {
                        mOpDialog.showTimePicker(sOpItems.get(i));
                    } else {
                        if (v.getId( ) == sBtnGuideIds[i]) {
                            mOpDialog.showTextDialog(sOpItems.get(i), Constant.INPUT_GUIDE_MSG,
                                                     OptionType.GUIDE_VOICE);
                        } else {
                            if (v.getId( ) == sBtnRunIds[i]) {
                                mOpDialog.showTextDialog(sOpItems.get(i), Constant.INPUT_RUN_MSG,
                                                         OptionType.RUN_VOICE);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void testOkgoogle() {
        Main.sTtsHelper.startSpeak(Option.sOpItems.get(sIndex).getGuideVoice( ));
        SystemClock.sleep(3000);
        recognizeVoice();
    }

    public void recognizeVoice() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName( ));
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, Constant.INPUT_VOICE_MSG);
        startActivityForResult(intent, Constant.GOOGLE_STT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && (requestCode == Constant.GOOGLE_STT)) {
            checkRecogVoice(requestCode, data);
        }
    }

    private void checkRecogVoice(int requestCode, Intent data) {
        String key = "";
        if (requestCode == Constant.GOOGLE_STT) key = RecognizerIntent.EXTRA_RESULTS;

        mResults = data.getStringArrayListExtra(key);
        String[] result = new String[mResults.size( )];
        mResults.toArray(result);

        for (String str : mResults) {
            if (Constant.POSITIVE_RESPONSE.equals(str)) {
                Logger.record(LogType.VERBOSE, "11");
                Main.sTtsHelper.startSpeak("오케이구글");
                SystemClock.sleep(3000);
                Main.sTtsHelper.startSpeak(Option.sOpItems.get(sIndex).getRunVoice( ));
                break;
            } else
                if (Constant.NEGATIVE_RESPONSE.equals(str)) {
                    Logger.record(LogType.VERBOSE, "22");
                    break;
                } else {
                    Logger.record(LogType.VERBOSE, "33");
                    recognizeVoice( );
                }
        }
    }
}
