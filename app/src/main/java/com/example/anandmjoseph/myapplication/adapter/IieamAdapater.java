package com.example.anandmjoseph.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.anandmjoseph.myapplication.R;
import com.example.anandmjoseph.myapplication.models.LandingIteamData;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anand M Joseph on 30-11-2017.
 */

public class IieamAdapater  extends BaseAdapter {

    private Context mContext;
    private ArrayList<LandingIteamData>  mCurrentAirQualityDataList;
    private LayoutInflater mLayoutInflater;

    public IieamAdapater(Context context) {
        this.mContext = context;
        mLayoutInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(mCurrentAirQualityDataList!=null) {
            return mCurrentAirQualityDataList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mCurrentAirQualityDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        CurrentAirQualityViewHolder mViewHolder;

        if (view == null) {
            mViewHolder = new CurrentAirQualityViewHolder();
            view = mLayoutInflater.inflate(R.layout.profile_list_item, parent, false);
            view.setId(position);
            mViewHolder.avatar_iv = (CircleImageView) view.findViewById(R.id.avatar_iv);
            mViewHolder.mProfile_name = (TextView) view.findViewById(R.id.profile_name);
            mViewHolder.mProfile_skills = (TextView) view.findViewById(R.id.profile_skills);

            view.setTag(mViewHolder);
        } else {
            mViewHolder = (CurrentAirQualityViewHolder) view.getTag();
        }
        populateForecastData(mViewHolder, position);


        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    /**
     * Update view with data
     * @param mViewHolder
     * @param position
     */
    private void populateForecastData(CurrentAirQualityViewHolder mViewHolder, int position) {

        LandingIteamData airQuality = mCurrentAirQualityDataList.get(position);

        //load profile avatar asynchronously
        if (!airQuality.image.trim().isEmpty()) {

            Picasso.with(mContext).load(airQuality.image)
                    .error(R.drawable.avatar)
                    .resize(100, 100)
                    .into(mViewHolder.avatar_iv);
        } else {

            Picasso.with(mContext).load(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .resize(100, 100)
                    .into(mViewHolder.avatar_iv);
        }


            mViewHolder.mProfile_skills.setText(airQuality.skills);
        mViewHolder.mProfile_name.setText("dadsadsa");

    }

    /**
     * ViewHolder class for Current Air Quality Data items
     */
    public static class CurrentAirQualityViewHolder {
        private CircleImageView avatar_iv;
        private TextView mProfile_name;
        private TextView mProfile_skills;
    }

    public void setIieamAdapater(ArrayList<LandingIteamData> mCurrentAirQualityDataList) {
        this.mCurrentAirQualityDataList = mCurrentAirQualityDataList;
    }

}
