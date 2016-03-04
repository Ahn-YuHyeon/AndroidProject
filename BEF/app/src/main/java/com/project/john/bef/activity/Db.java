package com.project.john.bef.activity;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.project.john.bef.Main;
import com.project.john.bef.R;
import com.project.john.bef.component.Constant;
import com.project.john.bef.manager.DbAdapter;
import com.project.john.bef.component.MembershipItem;

import java.util.ArrayList;

public class Db extends AppCompatActivity {
    private Cursor mCursor;
    private MembershipItem mMbItem;
    private ArrayList<MembershipItem> mMbItems;
    private DbAdapter mDbAdapter;
    private ListView mLvList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atvt_db);

        mMbItems = new ArrayList<MembershipItem>( );
        doWhileCursorToArray( );
        mDbAdapter = new DbAdapter(this, mMbItems);

        mLvList = (ListView) findViewById(R.id.lv_list);
        mLvList.setAdapter(mDbAdapter);
        mLvList.setOnItemLongClickListener(longClickListener);
    }

    private void doWhileCursorToArray( ) {
        mCursor = null;
        mCursor = Main.sDbHelper.getAllColumns( );

        while (mCursor.moveToNext( )) {
            mMbItem = new MembershipItem(mCursor.getInt(mCursor.getColumnIndex("_id")),
                                         mCursor.getString(mCursor.getColumnIndex("email")),
                                         mCursor.getString(mCursor.getColumnIndex("password")),
                                         mCursor.getString(mCursor.getColumnIndex("name")),
                                         mCursor.getString(mCursor.getColumnIndex("birth")),
                                         mCursor.getString(mCursor.getColumnIndex("city")),
                                         mCursor.getString(mCursor.getColumnIndex("job")),
                                         mCursor.getString(mCursor.getColumnIndex("sex")));
            mMbItems.add(mMbItem);
        }
        mCursor.close( );
    }

    private AdapterView.OnItemLongClickListener longClickListener =
            new AdapterView.OnItemLongClickListener( ) {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
                                               long arg3) {
                    boolean result = Main.sDbHelper.deleteColumn(mMbItems.get(position).mEmail);

                    if (result) {
                        mMbItems.remove(position);
                        mDbAdapter.setArrayList(mMbItems);
                        mDbAdapter.notifyDataSetChanged( );
                    } else {
                        Toast.makeText(getApplicationContext( ), Constant.DELETE_ERROR_MSG,
                                       Toast.LENGTH_LONG).show( );
                    }
                    return false;
                }
            };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
