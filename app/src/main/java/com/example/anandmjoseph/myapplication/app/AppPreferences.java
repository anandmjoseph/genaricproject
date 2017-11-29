package com.example.anandmjoseph.myapplication.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Anand on 29/11/17.
 */

public class AppPreferences {

    private boolean mFirstTimeLoading = true;
    private String APP_SHARED_PREFS = AppPreferences.class.getSimpleName();
    private SharedPreferences _sharedPrefs;
    private SharedPreferences.Editor _prefsEditor;
    public String ACCESS_TOKEN = "access_token";
    public String FIRST_TIME_LOADING = "first_time_loading";
    /**
     * The constructor
     *
     * @param context
     */
    public AppPreferences(Context context) {
        this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();
    }


    /**
     * Clear all Shared preference
     */
    public void clear() {

        mFirstTimeLoading = getFirstTimeLoading();

        _prefsEditor.clear();
        _prefsEditor.commit();

        saveFirstTimeLoading(mFirstTimeLoading);


    }


    /**
     * Method to save first time loading
     *
     * @param first_time_loading
     */
    public void saveFirstTimeLoading(boolean first_time_loading) {
        _prefsEditor.putBoolean(FIRST_TIME_LOADING, first_time_loading).commit();
    }

    /**
     * Method to get first time loading
     *
     * @return first_time_loading
     */
    public boolean getFirstTimeLoading() {
        return _sharedPrefs.getBoolean(FIRST_TIME_LOADING, true);
    }


}
