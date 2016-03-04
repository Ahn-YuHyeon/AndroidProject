package com.project.john.bef.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.john.bef.R;
import com.project.john.bef.component.MembershipItem;

import java.util.ArrayList;

public class DbAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<MembershipItem> mMbItems;
    private ViewHolder mViewHolder;

    public DbAdapter(Context context, ArrayList<MembershipItem> arrMembership) {
        mInflater = LayoutInflater.from(context);
        mMbItems = arrMembership;
    }

    @Override
    public int getCount( ) {
        return mMbItems.size( );
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
            view = mInflater.inflate(R.layout.list_membership, null);
            mViewHolder.mTvId = (TextView) view.findViewById(R.id.tv_id);
            mViewHolder.mTvEmail = (TextView) view.findViewById(R.id.tv_email);
            mViewHolder.mTvPw = (TextView) view.findViewById(R.id.tv_password);
            //mViewHolder.mName = (TextView) view.findViewById(R.id.tv_name);
            //mViewHolder.mBirth = (TextView) view.findViewById(R.id.tv_birth);
            //mViewHolder.mCity = (TextView) view.findViewById(R.id.tv_city);
            //mViewHolder.mJob = (TextView) view.findViewById(R.id.tv_job);
            //mViewHolder.mSex = (TextView) view.findViewById(R.id.tv_sex);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag( );
        }
        mViewHolder.mTvId.setText(String.valueOf(mMbItems.get(position).mId));
        mViewHolder.mTvEmail.setText(mMbItems.get(position).mEmail);
        mViewHolder.mTvPw.setText(mMbItems.get(position).mPw);
        //mViewHolder.mName.setText(mArrMembership.get(position).mName);
        //mViewHolder.mBirth.setText(mArrMembership.get(position).mBirth);
        //mViewHolder.mCity.setText(mArrMembership.get(position).mCity);
        //mViewHolder.mJob.setText(mArrMembership.get(position).mJob);
        //mViewHolder.mSex.setText(mArrMembership.get(position).mSex);
        return view;
    }

    public void setArrayList(ArrayList<MembershipItem> arrMembership) {
        this.mMbItems = arrMembership;
    }

    class ViewHolder {
        TextView mTvId;
        TextView mTvEmail;
        TextView mTvPw;
        //TextView mName;
        //TextView mBirth;
        //TextView mCity;
        //TextView mJob;
        //TextView mSex;
    }
}
