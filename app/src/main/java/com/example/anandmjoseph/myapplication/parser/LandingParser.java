package com.example.anandmjoseph.myapplication.parser;

import com.example.anandmjoseph.myapplication.appinterphase.AnandParser;
import com.example.anandmjoseph.myapplication.models.LandingIteamData;
import com.example.anandmjoseph.myapplication.models.LandingModel;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Anand M Joseph on 30-11-2017.
 */

public class LandingParser implements AnandParser {

    private String mDataToParse;
    private LandingModel mLandingModel;

    @Override
    public void init() {

    }

    @Override
    public void init(Object obj) {
        mDataToParse = (String) obj;
    }

    @Override
    public LandingModel parse() {
        mLandingModel= new LandingModel();

        JSONObject jsonobj = null;
        JSONArray jsonIteamarray = null;

        try {
            jsonobj = new JSONObject(mDataToParse);
            mLandingModel.code= jsonobj.optInt("code");
            mLandingModel.message=jsonobj.optString("message");
            jsonIteamarray=jsonobj.optJSONArray("data");

            for(int i=0;i<jsonIteamarray.length();i++){
                LandingIteamData mLandingIteamData=new LandingIteamData();
                mLandingIteamData.id=jsonIteamarray.optJSONObject(i).optInt("id");
                mLandingIteamData.skills=jsonIteamarray.optJSONObject(i).optString("skills");
                mLandingIteamData.image=jsonIteamarray.optJSONObject(i).optString("image");
                mLandingModel.mLandingIteamData.add(mLandingIteamData);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return mLandingModel;
    }

    @Override
    public boolean isFeedParsable() {
        boolean retVal = false;
        if (null != mDataToParse && mDataToParse.length() > 0) {
            retVal = true;
        }
        return retVal;
    }

}
