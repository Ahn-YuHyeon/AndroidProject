package com.project.john.bef.component;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.TimePicker;

import com.project.john.bef.enumeration.OptionType;

import java.util.Calendar;

public class OptionDialog {
    private Context mContext = null;
    Calendar mCalendar = Calendar.getInstance( );
    OptionItem mOpItem = null;
    OptionType mOpType;

    public OptionDialog(Context context) {
        mContext = context;
    }

    public void showTimePicker(Object object) {
        mOpItem = (OptionItem) object;

        TimePickerDialog.OnTimeSetListener mTimeSetListener =
                new TimePickerDialog.OnTimeSetListener( ) {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        mCalendar.set(Calendar.MINUTE, minute);
                        mOpItem.mOprHour = hourOfDay;
                        mOpItem.mOprMinute = minute;
                    }
                };
        new TimePickerDialog(mContext, mTimeSetListener, mOpItem.mOprHour,
                             mOpItem.mOprMinute, true).show( );
    }

    public void showTextDialog(Object object, String string, OptionType _OptionType) {
        mOpItem = (OptionItem) object;
        mOpType = _OptionType;

        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("");
        alert.setMessage(string);
        final EditText input = new EditText(mContext);

        switch (mOpType) {
            case GUIDE_VOICE:
                input.setText(mOpItem.mGuideVoice);
                break;
            case RUN_VOICE:
                input.setText(mOpItem.mRunVoice);
                break;
        }
        alert.setView(input);

        alert.setNegativeButton(Constant.OK, new DialogInterface.OnClickListener( ) {
            public void onClick(DialogInterface dialog, int which) {
                switch (mOpType) {
                    case GUIDE_VOICE:
                        mOpItem.mGuideVoice = input.getText( ).toString( );
                        break;
                    case RUN_VOICE:
                        mOpItem.mRunVoice = input.getText( ).toString( );
                        break;
                }

            }
        });
        alert.setPositiveButton(Constant.CANCEL, new DialogInterface.OnClickListener( ) {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show( );
    }
}
