package com.project.john.bef.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.john.bef.Main;
import com.project.john.bef.R;
import com.project.john.bef.component.Constant;
import com.project.john.bef.enumeration.LogType;
import com.project.john.bef.manager.Logger;
import com.project.john.bef.component.DuplicateException;
import com.project.john.bef.component.InputException;

public class Membership extends AppCompatActivity {
    EditText[] mEditTexts;
    String[] mStrings;
    String mSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atvt_membership);

        mEditTexts = new EditText[] {
                (EditText) findViewById(R.id.et_email), (EditText) findViewById(R.id.et_pw),
                (EditText) findViewById(R.id.et_name), (EditText) findViewById(R.id.et_birth),
                (EditText) findViewById(R.id.et_city), (EditText) findViewById(R.id.et_job)};

        findViewById(R.id.cb_male).setOnClickListener(new Button.OnClickListener( ) {
            public void onClick(View view) {
                mSex = Constant.MALE;
            }
        });
        findViewById(R.id.cb_female).setOnClickListener(new Button.OnClickListener( ) {
            public void onClick(View view) {
                mSex = Constant.FEMALE;
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId( )) {
            case R.id.btn_save:
                mStrings = editTextToString(mEditTexts);
                try {
                    if (Main.sDbHelper.insertColumn(mStrings, mSex) == false) {
                        Toast.makeText(this, Constant.INSERT_ERROR_MSG, Toast.LENGTH_LONG).show( );
                    }
                } catch (InputException e) {
                    Toast.makeText(this, Constant.BLANK_ERROR_MSG, Toast.LENGTH_LONG).show( );
                    Logger.record(LogType.VERBOSE, e.toString( ), e.getCause( ));
                    break;
                } catch (DuplicateException e) {
                    Toast.makeText(this, Constant.DUPLICATE_ERROR_MSG, Toast.LENGTH_LONG).show( );
                    Logger.record(LogType.VERBOSE, e.toString( ), e.getCause( ));
                    break;
                }
                Intent intent = new Intent(this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public String[] editTextToString(EditText[] editTexts) {
        String[] strings = new String[editTexts.length];

        for (int i = 0; i < editTexts.length; i++) {
            strings[i] = editTexts[i].getText( ).toString( ).trim( );
            if (strings[i].equals("")) return null;
        }
        return strings;
    }
}