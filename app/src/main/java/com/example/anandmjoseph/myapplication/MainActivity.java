package com.example.anandmjoseph.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.anandmjoseph.myapplication.adapter.IieamAdapater;
import com.example.anandmjoseph.myapplication.app.AppFeed;
import com.example.anandmjoseph.myapplication.appinterphase.CustomListener;
import com.example.anandmjoseph.myapplication.connectioninterphase.NetworkManager;
import com.example.anandmjoseph.myapplication.models.LandingModel;
import com.example.anandmjoseph.myapplication.parser.LandingParser;
import com.example.anandmjoseph.myapplication.utils.Utils;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private LandingModel mLandingModel;
    private IieamAdapater mIieamAdapater;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){

        mListView=findViewById(R.id.aqd_list);
        callSignInApi();
    }


    private void updateview(){
        mIieamAdapater = new IieamAdapater(this);
        mIieamAdapater.setIieamAdapater(mLandingModel.mLandingIteamData);
        //mCurrentAirQualityListAdapter.setmCurrentAirQualityDataList(list);
        mListView.setAdapter(mIieamAdapater);

    }


    /**
     * API call for sign in
     */
    private void callSignInApi() {
        //showprogressbar();
        HashMap<String, String> mHeaderParams = setHeader();

        NetworkManager.getInstance().CommonStringGetRequest(mHeaderParams, AppFeed.BASE_URL, LandingParser.class, new CustomListener<Object>() {
            @Override
            public void getResult(Object result) {

                if (result instanceof LandingModel) {
                    mLandingModel = (LandingModel) result;
                    if (mLandingModel.code == 1001 || mLandingModel.code > 0) {

                        System.out.println("The value is ==============>>"+mLandingModel.mLandingIteamData.size());
                        updateview();
                        //Need to update
                    } else {

                       // Need to update ERROR UI
                    }
                } else {
                    // Need to update ERROR UI
                }
            }
        });
    }


    /**
     * Show ProgressBar
     */
    private void showprogressbar() {
        Utils.showProgressBar(this, getResources().getString(R.string.please));
    }

    /**
     * header for authorized API calls
     *
     * @return
     */
    public HashMap<String, String> setHeader() {
        HashMap<String, String> mHeaderParams = new HashMap<>();
        mHeaderParams.put("Content-Type", "application/x-www-form-urlencoded");
        return mHeaderParams;
    }
}
