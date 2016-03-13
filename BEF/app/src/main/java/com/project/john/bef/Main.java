package com.project.john.bef;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.john.bef.activity.Login;
import com.project.john.bef.activity.Option;
import com.project.john.bef.component.Constant;
import com.project.john.bef.component.OptionItem;
import com.project.john.bef.enumeration.LogType;
import com.project.john.bef.manager.AlarmReceiver;
import com.project.john.bef.manager.AlarmRegister;
import com.project.john.bef.manager.DbHelper;
import com.project.john.bef.manager.Logger;
import com.project.john.bef.manager.TtsHelper;

import java.util.ArrayList;

/**
 * Follow Field Naming Conventions
 * <p/>
 * public class MyClass {
 * public static final int SOME_CONSTANT = 42;
 * public int publicField;
 * private static MyClass sSingleton;
 * int mPackagePrivate;
 * private int mPrivate;
 * protected int mProtected;
 * }
 */
public class Main extends AppCompatActivity
        implements TextToSpeech.OnInitListener, SensorEventListener {
    public static DbHelper sDbHelper;
    public static TtsHelper sTtsHelper;
    public static TextToSpeech sTts;

    OptionItem mOpItem;
    public static boolean sFlag = false;

    private long mExitModeTime = 0L;
    private ArrayList<String> mResults;

    AlarmRegister mAlarmRegister;
    AlarmReceiver mAlarmReceiver;

    private long mLastTime;
    private float mSpeed;
    private float mLastX;
    private float mLastY;
    private float mLastZ;
    private float mX, mY, mZ;

    private static final int sSHAKE_THRESHOLD = 800;
        private static final int sDATA_X = SensorManager.DATA_X;
    private static final int sDATA_Y = SensorManager.DATA_Y;
    private static final int sDATA_Z = SensorManager.DATA_Z;

    private SensorManager mSensorManager;
    private Sensor mAccelerormeterSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atvt_main);

        sDbHelper = new DbHelper(this);
        sDbHelper.open( );
        Logger.record(LogType.VERBOSE, Constant.OPENED_DB_MSG);

        sTts = new TextToSpeech(this, this);
        sTtsHelper = new TtsHelper(this, sTts);

        mAlarmRegister = new AlarmRegister( );
        mAlarmReceiver = new AlarmReceiver( );
        mAlarmReceiver.setOnAlarmListener(new AlarmReceiver.OnAlarmListener( ) {
            public void onAlarm(OptionItem opItem) {
                mOpItem = opItem;
                Logger.record(LogType.VERBOSE,
                              Constant.SUCCESSED_ALARM_MSG + "(" + mOpItem.getOprHour( ) + ":" +
                              mOpItem.getOprMinute( ) + ")");
                sTtsHelper.startSpeak(mOpItem.mGuideVoice);
                SystemClock.sleep(3000);
                recognizeVoice( );
            }
        });
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerormeterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onDestroy( ) {
        sDbHelper.close( );
        Logger.record(LogType.VERBOSE, Constant.CLOSED_DB_MSG);
        super.onDestroy( );
    }

    public void onClick(View view) {
        switch (view.getId( )) {
            case R.id.btn_login:
                Intent intent1 = new Intent(this, Login.class);
                startActivity(intent1);
                break;
            case R.id.btn_play:
                if (Login.sLoginState) {
                    Toast.makeText(this, Constant.STARTED_RUN_MSG, Toast.LENGTH_LONG).show( );
                    mAlarmRegister.setAlarm(this);
                } else {
                    Toast.makeText(this, Constant.LOGIN_ERROR_MSG, Toast.LENGTH_LONG).show( );
                }
                break;
            case R.id.btn_option:
                if (Login.sLoginState) {
                    Intent intent2 = new Intent(this, Option.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(this, Constant.LOGIN_ERROR_MSG, Toast.LENGTH_LONG).show( );
                }
                break;
        }
    }

    @Override
    public void onBackPressed( ) {
        if (mExitModeTime != 0 && SystemClock.uptimeMillis( ) - mExitModeTime < 3000) {
            super.onBackPressed( );
        } else {
            Toast.makeText(this, Constant.FINISHED_APP_MSG, Toast.LENGTH_LONG).show( );
            mExitModeTime = SystemClock.uptimeMillis( );
        }
    }

    @Override
    public void finish( ) {
        if (sTts != null) {
            sTts.stop( );
            sTts.shutdown( );
        }
        mAlarmRegister.releaseAlarm(this);

        if (mSensorManager != null) mSensorManager.unregisterListener(this);
        super.finish( );
    }

    @Override
    public void onInit(int status) {
        TtsHelper.sIsInit = status == TextToSpeech.SUCCESS;
        String msg = TtsHelper.sIsInit ? Constant.SUCCESSED_INIT_MSG : Constant.FAILED_INIT_MSG;
        Logger.record(LogType.VERBOSE, msg);
    }

    public void recognizeVoice( ) {
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
                sTtsHelper.startSpeak("오케이구글");
                SystemClock.sleep(3000);
                sTtsHelper.startSpeak(mOpItem.mRunVoice);
                break;
            } else
                if (Constant.NEGATIVE_RESPONSE.equals(str)) {
                    break;
                } else {
                    recognizeVoice( );
                }
        }
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater( ).inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId( );
        switch (id) {
            case R.id.google:
                Intent intent = getPackageManager( )
                        .getLaunchIntentForPackage("com.google.android.googlequicksearchbox");
                startActivity(intent);
                break;
            case R.id.ok_google:
                if (sFlag) {
                    sTtsHelper.stopTimerTask( );
                    sFlag = false;
                } else {
                    sTtsHelper.startSpeak2("오케이구글");
                    sFlag = true;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart( ) {
        super.onStart( );
        if (mAccelerormeterSensor != null) {
            mSensorManager
                    .registerListener(this, mAccelerormeterSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType( ) == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis( );
            long gabOfTime = (currentTime - mLastTime);

            if (gabOfTime > 100) {
                mLastTime = currentTime;
                mX = event.values[SensorManager.DATA_X];
                mY = event.values[SensorManager.DATA_Y];
                mZ = event.values[SensorManager.DATA_Z];

                mSpeed = Math.abs(mX + mY + mZ - mLastX - mLastY - mLastZ) / gabOfTime * 10000;

                if (mSpeed > sSHAKE_THRESHOLD) {
                    sTtsHelper.startSpeak("검색어를 말씀하세요!");
                    SystemClock.sleep(3000);
                    sTtsHelper.startSpeak("오케이구글");
                }
                mLastX = event.values[sDATA_X];
                mLastY = event.values[sDATA_Y];
                mLastZ = event.values[sDATA_Z];
            }
        }
    }

    @Override
    public void onStop( ) {
        super.onStop( );
        //if (mSensorManager != null) mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
