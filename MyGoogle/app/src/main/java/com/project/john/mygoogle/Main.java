/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.project.john.mygoogle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.PaintDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.BaseColumns;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.project.john.mygoogle.component.ApplicationController;
import com.project.john.mygoogle.component.Cmd;
import com.project.john.mygoogle.component.CmdAdapter;
import com.project.john.mygoogle.component.Constant;
import com.project.john.mygoogle.component.Db;
import com.project.john.mygoogle.component.DbHelper;
import com.project.john.mygoogle.component.MyLogger;
import com.project.john.mygoogle.component.TtsHelper;
import com.project.john.mygoogle.enumeration.CmdInsultTaskState;
import com.project.john.mygoogle.enumeration.DbState;
import com.project.john.mygoogle.enumeration.LogType;
import com.project.john.mygoogle.enumeration.RunState;

import java.util.ArrayList;

public class Main extends Activity
        implements TextToSpeech.OnInitListener, AdapterView.OnItemClickListener,
                   SensorEventListener {
    public static DbHelper sDbHelper;

    private Cursor mCursor;
    private Cmd mCmd;
    private ArrayList<Cmd> mCmds;
    private CmdAdapter mCmdAdapter;
    private ListView mLvCmd;

    public static TextToSpeech sTts;
    public static TtsHelper sTtsHelper;

    private SensorManager mSensorManager;
    private Sensor mAccelerormeterSensor;
    private static long sSystemTime = 0L;
    private static long sShakeModeTime = 0L;
    private static long sClickedTime = 0L;
    private long mLastTime;
    private float mSpeed;
    private float mLastX;
    private float mLastY;
    private float mLastZ;
    private float mX, mY, mZ;
    private static final int sSHAKE_THRESHOLD = 1000;
    private static final int sDATA_X = SensorManager.DATA_X;
    private static final int sDATA_Y = SensorManager.DATA_Y;
    private static final int sDATA_Z = SensorManager.DATA_Z;
    RunState mSpeakState = RunState.STOP;
    RunState mShakeMode = RunState.STOP;

    private long mExitModeTime = 0L;

    cmdInitializeTask mCmdInitializeTask;
    cmdInsultTask mCmdInsultTask;
    selectedCmdSpeakTask mSelectedCmdSpeakTask;

    static BroadcastReceiver mScreenOnReceiver = null;

    public class cmdInitializeTask extends AsyncTask<String, CmdInsultTaskState, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            for (int i = 0; i < params.length; i++) {
                String serial;
                mCmd = new Cmd( );
                sSystemTime = System.currentTimeMillis( );
                serial = "#" + Long.toString(sSystemTime);
                mCmd.setSerial(serial);
                mCmd.setCmd(params[i]);
                boolean result = sDbHelper.insertColumn(mCmd.getSerial( ), mCmd.getCmd( ));
                if (result) {
                    Cursor cursor = sDbHelper.getColumnToSerial(mCmd.getSerial( ));
                    if (cursor == null) {
                        publishProgress(CmdInsultTaskState.FAILED_QUERY);
                        return false;
                    }
                    Cmd cmd = new Cmd(cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)),
                                      cursor.getString(cursor.getColumnIndex(Db.CreateDb.SERIAL)),
                                      cursor.getString(cursor.getColumnIndex(Db.CreateDb.CMD)));
                    mCmds.add(cmd);
                    cursor.close( );
                } else {
                    publishProgress(CmdInsultTaskState.FAILED_ADD);
                    return false;
                }
                SystemClock.sleep(100);
            }
            publishProgress(CmdInsultTaskState.SUCCESSED_ADD);
            return true;
        }

        @Override
        protected void onProgressUpdate(CmdInsultTaskState... values) {
            switch (values[0]) {
                case FAILED_QUERY:
                    Toast.makeText(Main.this, Constant.FAILED_QUERY_MSG, Toast.LENGTH_SHORT)
                         .show( );
                    break;
                case SUCCESSED_ADD:
                    Toast.makeText(Main.this, Constant.REGISTER_CMD_MSG, Toast.LENGTH_SHORT)
                         .show( );
                    break;
                case FAILED_ADD:
                    Toast.makeText(Main.this, Constant.FAILED_ADD_MSG, Toast.LENGTH_SHORT).show( );
                    break;
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mCmdAdapter = new CmdAdapter(Main.this, mCmds);
            mLvCmd = (ListView) findViewById(R.id.lv_cmd);
            mLvCmd.setAdapter(mCmdAdapter);
            mLvCmd.setSelector(new PaintDrawable(0xFF52FDB3));
            mLvCmd.setOnItemLongClickListener(longClickListener);
            mLvCmd.setOnItemClickListener(Main.this);
            super.onPostExecute(aBoolean);
        }
    }

    public class cmdInsultTask extends AsyncTask<String, CmdInsultTaskState, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            for (int i = 0; i < params.length; i++) {
                String serial;
                mCmd = new Cmd( );
                sSystemTime = System.currentTimeMillis( );
                serial = "#" + Long.toString(sSystemTime);
                mCmd.setSerial(serial);
                mCmd.setCmd(params[i]);
                boolean result = sDbHelper.insertColumn(mCmd.getSerial( ), mCmd.getCmd( ));
                if (result) {
                    Cursor cursor = sDbHelper.getColumnToSerial(mCmd.getSerial( ));
                    if (cursor == null) {
                        publishProgress(CmdInsultTaskState.FAILED_QUERY);
                        return false;
                    }
                    Cmd cmd = new Cmd(cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)),
                                      cursor.getString(cursor.getColumnIndex(Db.CreateDb.SERIAL)),
                                      cursor.getString(cursor.getColumnIndex(Db.CreateDb.CMD)));
                    mCmds.add(cmd);
                    cursor.close( );
                    publishProgress(CmdInsultTaskState.SUCCESSED_ADD);
                } else {
                    publishProgress(CmdInsultTaskState.FAILED_ADD);
                    return false;
                }
                SystemClock.sleep(100);
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(CmdInsultTaskState... values) {
            switch (values[0]) {
                case FAILED_QUERY:
                    Toast.makeText(Main.this, Constant.FAILED_QUERY_MSG, Toast.LENGTH_SHORT)
                         .show( );
                    break;
                case SUCCESSED_ADD:
                    mCmdAdapter.notifyDataSetChanged( );
                    Toast.makeText(Main.this, Constant.SUCCESSED_ADD_MSG, Toast.LENGTH_SHORT)
                         .show( );
                    break;
                case FAILED_ADD:
                    Toast.makeText(Main.this, Constant.FAILED_ADD_MSG, Toast.LENGTH_SHORT).show( );
                    break;
            }
            super.onProgressUpdate(values);
        }
    }

    public class selectedCmdSpeakTask extends AsyncTask<Integer, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            sTtsHelper.startSpeak(Constant.OKGOOGLE);
            SystemClock.sleep(3000);
            sTtsHelper.startSpeak(mCmds.get(params[0]).getCmd( ));
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atvt_main);

        // 삼성 TTS 엔진 생성
        sTts = new TextToSpeech(this, this);
        sTtsHelper = new TtsHelper(this, sTts);

        // 흔들림 감시 센서 생성
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerormeterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // 명령어 데이터베이스 열기
        sDbHelper = new DbHelper(this);
        sDbHelper.open( );

        mCmds = new ArrayList<Cmd>( );
        if (DbHelper.sDbState == DbState.FIRST) {
            // 초기 명령어 설정
            mCmdInitializeTask = new cmdInitializeTask( );
            mCmdInitializeTask.execute(Constant.CMDS);
        } else {
            // 명령어 읽기
            doWhileCursorToArray( );
            mCmdAdapter = new CmdAdapter(this, mCmds);

            // 리스트에 명령어 표시
            mLvCmd = (ListView) findViewById(R.id.lv_cmd);
            mLvCmd.setAdapter(mCmdAdapter);

            // 리스트 옵션 설정
            mLvCmd.setSelector(new PaintDrawable(0xFF52FDB3));
            mLvCmd.setOnItemLongClickListener(longClickListener);
            mLvCmd.setOnItemClickListener(this);
        }

        Tracker t = ((ApplicationController) getApplication( ))
                .getTracker(ApplicationController.TrackerName.APP_TRACKER);
        t.setScreenName("MainActivity");
        t.send(new HitBuilders.AppViewBuilder( ).build( ));

            }

    private void doWhileCursorToArray( ) {
        mCursor = null;
        mCursor = Main.sDbHelper.getAllColumns( );

        while (mCursor.moveToNext( )) {
            mCmd = new Cmd(mCursor.getInt(mCursor.getColumnIndex("_id")),
                           mCursor.getString(mCursor.getColumnIndex("serial")),
                           mCursor.getString(mCursor.getColumnIndex("cmd")));
            mCmds.add(mCmd);
        }
        mCursor.close( );
    }

    private AdapterView.OnItemLongClickListener longClickListener =
            new AdapterView.OnItemLongClickListener( ) {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
                                               long arg3) {
                    Tracker t = ((ApplicationController) getApplication( ))
                            .getTracker(ApplicationController.TrackerName.APP_TRACKER);
                    t.send(new HitBuilders.EventBuilder( ).setCategory("MainActivity")
                                                          .setAction("Press Delete")
                                                          .setLabel("Delete").build( ));

                    boolean result = Main.sDbHelper.deleteColumn(mCmds.get(position).getSerial( ));
                    if (result) {
                        mCmds.remove(position);
                        mCmdAdapter.setCmds(mCmds);
                        mCmdAdapter.notifyDataSetChanged( );
                    } else {
                        Toast.makeText(getApplicationContext( ), Constant.FAILED_DELETE_MSG,
                                       Toast.LENGTH_SHORT).show( );
                    }
                    return true;
                }
            };

    @Override
    protected void onDestroy( ) {
        sDbHelper.close( );
        unregisterReceiver(mScreenOnReceiver);
        super.onDestroy( );
    }

    @Override
    public void onBackPressed( ) {
        if (mExitModeTime != 0 && SystemClock.uptimeMillis( ) - mExitModeTime < 3000) {
            super.onBackPressed( );
        } else {
            Toast.makeText(this, Constant.FINISHED_APP_MSG, Toast.LENGTH_SHORT).show( );
            mExitModeTime = SystemClock.uptimeMillis( );
        }
    }

    @Override
    public void finish( ) {
        if (sTts != null) {
            sTts.stop( );
            sTts.shutdown( );
        }
        if (mSensorManager != null) mSensorManager.unregisterListener(this);
        super.finish( );
    }

    @Override
    public void onInit(int status) {
        TtsHelper.sIsInit = status == TextToSpeech.SUCCESS;
        String msg = TtsHelper.sIsInit ? Constant.SUCCESSED_INIT_MSG : Constant.FAILED_INIT_MSG;
        MyLogger.record(LogType.VERBOSE, msg);
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
        Tracker t = ((ApplicationController) getApplication( ))
                .getTracker(ApplicationController.TrackerName.APP_TRACKER);
        switch (id) {
            case R.id.google:
                t.send(new HitBuilders.EventBuilder( ).setCategory("MainActivity")
                                                      .setAction("Press Google").setLabel("Google")
                                                      .build( ));
                Intent intent =
                        getPackageManager( ).getLaunchIntentForPackage(Constant.GOOGLE_PACKAGE);
                startActivity(intent);
                break;
            case R.id.shake:
                if (mShakeMode == RunState.STOP) {
                    t.send(new HitBuilders.EventBuilder( ).setCategory("MainActivity")
                                                          .setAction("Press Shake On")
                                                          .setLabel("Shake").build( ));
                    item.setTitle(Constant.END_STUDY);
                    mShakeMode = RunState.RUN;
                    Toast.makeText(Main.this, Constant.START_STUDY_MSG, Toast.LENGTH_SHORT).show( );
                } else {
                    t.send(new HitBuilders.EventBuilder( ).setCategory("MainActivity")
                                                          .setAction("Press Shake Off")
                                                          .setLabel("Shake").build( ));
                    item.setTitle(Constant.START_STUDY);
                    mShakeMode = RunState.STOP;
                    Toast.makeText(Main.this, Constant.END_STUDY_MSG, Toast.LENGTH_SHORT).show( );
                }
                break;
            case R.id.add:
                t.send(new HitBuilders.EventBuilder( ).setCategory("MainActivity")
                                                      .setAction("Press Add").setLabel("Add")
                                                      .build( ));
                showTextDialog(Constant.INPUT_TEXT_MSG);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if ((sClickedTime == 0) || (SystemClock.uptimeMillis( ) - sClickedTime > 3000)) {
            Tracker t = ((ApplicationController) getApplication( ))
                    .getTracker(ApplicationController.TrackerName.APP_TRACKER);
            t.send(new HitBuilders.EventBuilder( ).setCategory("MainActivity").setAction(
                    "Press Cmd(" + mCmds.get(position).getCmd( ) + ")").setLabel("Cmd").build( ));
            mSelectedCmdSpeakTask = new selectedCmdSpeakTask( );
            mSelectedCmdSpeakTask.execute(position);

            sClickedTime = SystemClock.uptimeMillis( );
        } else {
            Toast.makeText(this, Constant.FAILED_CLICK_MSG, Toast.LENGTH_SHORT).show( );
        }
    }

    public void showTextDialog(String string) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("");
        alert.setMessage(string);
        final EditText input = new EditText(this);

        input.setText("");
        alert.setView(input);

        alert.setNegativeButton(Constant.OK, new DialogInterface.OnClickListener( ) {
            public void onClick(DialogInterface dialog, int which) {
                mCmdInsultTask = new cmdInsultTask( );
                mCmdInsultTask.execute(input.getText( ).toString( ));
            }
        });
        alert.setPositiveButton(Constant.CANCEL, new DialogInterface.OnClickListener( ) {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show( );
    }

    @Override
    public void onStart( ) {
        super.onStart( );
        if (mAccelerormeterSensor != null) {
            mSensorManager
                    .registerListener(this, mAccelerormeterSensor, SensorManager.SENSOR_DELAY_GAME);
        }
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
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

                if ((mSpeed > sSHAKE_THRESHOLD) && (mShakeMode == RunState.RUN)) {
                    if ((sShakeModeTime == 0) ||
                        (SystemClock.uptimeMillis( ) - sShakeModeTime > 3000)) {
                        switch (mSpeakState) {
                            case STOP:
                                sTtsHelper.startSpeak2(Constant.OKGOOGLE);
                                mSpeakState = RunState.RUN;
                                break;
                            case RUN:
                                sTtsHelper.stopTimerTask( );
                                mSpeakState = RunState.STOP;
                                break;
                        }
                        sShakeModeTime = SystemClock.uptimeMillis( );
                    } else {
                        MyLogger.record(LogType.VERBOSE, Constant.SHAKE_ERROR_MSG);
                    }
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
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
