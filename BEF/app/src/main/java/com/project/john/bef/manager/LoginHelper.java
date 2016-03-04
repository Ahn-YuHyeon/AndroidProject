package com.project.john.bef.manager;

import android.content.Context;
import android.database.Cursor;

import com.project.john.bef.Main;
import com.project.john.bef.component.Constant;
import com.project.john.bef.activity.Login;
import com.project.john.bef.component.InputException;
import com.project.john.bef.enumeration.LogType;

public class LoginHelper {
    Context mContext;
    Cursor mCursor;

    public LoginHelper(Context context) {
        mContext = context;
    }

    public void checkEmail(String string) throws InputException {
        if ((mCursor = Main.sDbHelper.getColumnToLogin(string)) == null) {
            throw new InputException(Constant.EMAIL_ERROR_MSG);
        }
    }

    public void checkPw(String string) throws InputException {
        try {
            if (string.equals(mCursor.getString(mCursor.getColumnIndex("password")))) {
                Login.sLoginState = true;
            } else {
                throw new InputException(Constant.PW_ERROR_MSG);
            }
        } catch (Exception e) {
            Logger.record(LogType.VERBOSE, e.toString( ), e.getCause( ));
        }
    }
}
