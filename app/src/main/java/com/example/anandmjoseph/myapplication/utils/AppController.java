/**
 * Copyright (C) 2015-2016 Neva ventures .
 */
package com.example.anandmjoseph.myapplication.utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.anandmjoseph.myapplication.app.AppConstants;
import com.example.anandmjoseph.myapplication.app.AppPreferences;
import com.example.anandmjoseph.myapplication.DatabaseHelper.DatabaseHelper;

/**
 * Parent application class for Sense app Created by anand Created on 25/9/15.
 */
public class AppController extends Application {

	public static final String TAG	= AppController.class.getSimpleName();
	private static AppController	mInstance;
	private RequestQueue			mRequestQueue;
	private static DatabaseHelper helper;

	private AppPreferences mAppPreferences;

	public static synchronized AppController getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		mAppPreferences = new AppPreferences(this);
		MultiDex.install(this);
		setDatabase();
		com.example.anandmjoseph.myapplication.connectioninterphase.NetworkManager.getInstance(this);
	}


	/**
	 * Method to attach base context
	 * 
	 * @param base
	 */
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	/**
	 * Method to get request queue
	 * 
	 * @return
	 */
	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return mRequestQueue;
	}

	/**
	 * Set database
	 */
	private void setDatabase() {
		DatabaseHelper helper = (DatabaseHelper) OpenHelperManager.getHelper(this.getApplicationContext(), DatabaseHelper.class);
		AppController.helper = helper;
		if (helper != null) {
			helper.getWritableDatabase();
		}
	}

	/**
	 * Method to get Database Helper
	 *
	 * @return DatabaseHelper
	 */
	public static DatabaseHelper getHelper() {
		return helper;
	}



}