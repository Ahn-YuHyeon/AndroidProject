package com.project.john.bef.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.project.john.bef.Main;
import com.project.john.bef.R;
import com.project.john.bef.component.Constant;
import com.project.john.bef.component.InputException;
import com.project.john.bef.enumeration.LogType;
import com.project.john.bef.manager.Logger;
import com.project.john.bef.manager.LoginHelper;

public class Login extends AppCompatActivity {
    EditText mEtEmail;
    EditText mEtPw;
    LoginHelper mLoginHelper;
    public static boolean sLoginState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atvt_login);

        mEtEmail = (EditText) findViewById(R.id.et_email);
        mEtPw = (EditText) findViewById(R.id.et_pw);

        mLoginHelper = new LoginHelper(this);
    }

    public void onClick(View view) {
        switch (view.getId( )) {
            case R.id.btn_login:
                try {
                    mLoginHelper.checkEmail(mEtEmail.getText( ).toString( ).trim( ));
                } catch (InputException e) {
                    Toast.makeText(this, Constant.EMAIL_ERROR_MSG, Toast.LENGTH_LONG).show( );
                    Logger.record(LogType.VERBOSE, e.toString( ), e.getCause( ));
                    break;
                }

                try {
                    mLoginHelper.checkPw(mEtPw.getText( ).toString( ).trim( ));
                    Intent intent1 = new Intent(this, Main.class);
                    intent1.addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent1);
                } catch (InputException e) {
                    Toast.makeText(this, Constant.PW_ERROR_MSG, Toast.LENGTH_LONG).show( );
                    Logger.record(LogType.VERBOSE, e.toString( ), e.getCause( ));
                }
                break;
            case R.id.btn_membership:
                Intent intent2 = new Intent(this, Membership.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
