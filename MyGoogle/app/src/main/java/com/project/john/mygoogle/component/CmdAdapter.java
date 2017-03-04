package com.project.john.mygoogle.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.john.mygoogle.R;

import java.util.ArrayList;

public class CmdAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Cmd> mCmds;
    private ViewHolder mViewHolder;

    public CmdAdapter(Context context, ArrayList<Cmd> cmds) {
        mInflater = LayoutInflater.from(context);
        mCmds = cmds;
    }

    @Override
    public int getCount( ) {
        return mCmds.size( );
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            mViewHolder = new ViewHolder( );
            view = mInflater.inflate(R.layout.list_cmd, null);
            mViewHolder.mTvCmd = (TextView) view.findViewById(R.id.tv_cmd);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag( );
        }
        mViewHolder.mTvCmd.setText(mCmds.get(position).getCmd( ));
        return view;
    }

    public void setCmds(ArrayList<Cmd> cmds) {
        mCmds = cmds;
    }

    class ViewHolder {
        TextView mTvCmd;
    }
}
