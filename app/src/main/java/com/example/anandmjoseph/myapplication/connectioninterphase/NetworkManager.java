/**
 * Copyright (C) 2015-2016 Neva ventures .
 */
package com.example.anandmjoseph.myapplication.connectioninterphase;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.example.anandmjoseph.myapplication.appinterphase.AnandParser;
import com.example.anandmjoseph.myapplication.appinterphase.CustomListener;

import com.example.anandmjoseph.myapplication.utils.AppController;
import com.example.anandmjoseph.myapplication.utils.MultiPartReq;

/**
 * Network connection interface class Created by anand Created on 25/9/15.
 */
public class NetworkManager {
	private static final String TAG			= "NetworkManager";
	private static NetworkManager	instance	= null;
	protected AnandParser npParser	= null;
	protected Class<?> mParser;
	// for Volley API
	public RequestQueue				requestQueue;

	private NetworkManager(Context context) {

		requestQueue = AppController.getInstance().getRequestQueue();
	}

	private NetworkManager() {

	}

	public static synchronized NetworkManager getInstance(Context context) {
		if (null == instance)
			instance = new NetworkManager(context);
		return instance;
	}

	// this is so you don't need to pass context each time
	public static synchronized NetworkManager getInstance() {
		if (null == instance) {
			throw new IllegalStateException(NetworkManager.class.getSimpleName() + " is not initialized, call getInstance(...) first");
		}
		return instance;
	}

	/**
	 * COMMON POST REQUEST
	 *
	 * @param Headdersparams
	 * @param url
	 * @param listener
	 */

	public void CommonStringPostRequestToken(final HashMap<String, String> params, final HashMap<String, String> Headdersparams, String url, final Class<?> parser, final CustomListener<Object> listener) {
		StringRequest myReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {

				mParser = parser;

				try {
					npParser = (AnandParser) mParser.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
					npParser = null;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					npParser = null;
				}

				if (null != response.toString()) {
					npParser.init(response.toString());
					if (npParser.isFeedParsable()) {
						listener.getResult(npParser.parse());
					}
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				String json = null;
				if(error != null &&  error.networkResponse != null && error.networkResponse.data != null){
							 json = new String(error.networkResponse.data);
							listener.getResult(json);
				}else{
					listener.getResult("{\n" +
							"\t\t\t\t\t\t\"msg\": \"Network Error OR Data connection Error.\",\n" +
							"\t\t\t\t\t\t\t\"code\": -1001\n" +
							"\t\t\t\t\t}");
				}
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				if (Headdersparams == null || Headdersparams.isEmpty()) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("Content-Type", "application/x-www-form-urlencoded");
					return params;
				} else {
					return Headdersparams;
				}
			}
		};
		requestQueue.add(myReq);

		myReq.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

	}


	/**
	 * COMMON MultipartPOST REQUEST
	 *
	 * @param Headdersparams
	 * @param url
	 * @param listener
	 */

	public void CommonMultipartPostRequestToken(final HashMap<String, String> params, final HashMap<String, String> Headdersparams, String url, final Class<?> parser, final CustomListener<Object> listener, final Uri image) {

		File file = null;

		if (image!=null) {
			 file = new File(image.getPath());
		}

		MultiPartReq myReqMultipart= new MultiPartReq(url,new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				String json = null;
				if(error != null &&  error.networkResponse != null && error.networkResponse.data != null){
					json = new String(error.networkResponse.data);
					listener.getResult(json);
				}else{
					listener.getResult("{\n" +
							"\t\t\t\t\t\t\"msg\": \"Network Error OR Data connection Error.\",\n" +
							"\t\t\t\t\t\t\t\"code\": -1001\n" +
							"\t\t\t\t\t}");
				}
			}
		},new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				mParser = parser;

				try {
					npParser = (AnandParser) mParser.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
					npParser = null;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					npParser = null;
				}

				if (null != response.toString()) {
					npParser.init(response.toString());
					if (npParser.isFeedParsable()) {
						listener.getResult(npParser.parse());
					}
				}
			}
		},file, "fileUpload", params, Headdersparams);



		requestQueue.add(myReqMultipart);

		myReqMultipart.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

	}


	/**
	 * COMMON GET REQUEST
	 *
	 * @param Headdersparams
	 * @param url
	 * @param listener
	 */
	public void CommonStringGetRequest(final HashMap<String, String> Headdersparams, String url, final Class<?> parser, final CustomListener<Object> listener) {

		StringRequest myReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				mParser = parser;

				try {
					npParser = (AnandParser) mParser.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
					npParser = null;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					npParser = null;
				}

				if (null != response.toString()) {
					npParser.init(response.toString());
					if (npParser.isFeedParsable()) {
						listener.getResult(npParser.parse());
					}
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				String json = null;
				if(error != null &&  error.networkResponse != null && error.networkResponse.data != null){
					json = new String(error.networkResponse.data);
					listener.getResult(json);
				}else{
					listener.getResult("{\n" +
							"\t\t\t\t\t\t\"msg\": \"Network Error OR Data connection Error.\",\n" +
							"\t\t\t\t\t\t\t\"code\": -1001\n" +
							"\t\t\t\t\t}");
				}
			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				if (Headdersparams == null || Headdersparams.isEmpty()) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("Content-Type", "application/x-www-form-urlencoded");
					return params;
				} else {
					return Headdersparams;
				}
			}
		};

		int socketTimeout = 30000;// 30 seconds
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		myReq.setRetryPolicy(policy);

		requestQueue.add(myReq);

	}

	public void CommonJsonObjectRequest(final HashMap<String, Object> params, final HashMap<String, String> Headdersparams, String url, final Class<?> parser, final CustomListener<Object> listener) {
		final JSONObject json_params = new JSONObject(params);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, json_params, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				mParser = parser;

				try {
					npParser = (AnandParser) mParser.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
					npParser = null;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					npParser = null;
				}

				if (null != response.toString()) {
					npParser.init(response.toString());
					if (npParser.isFeedParsable()) {
						listener.getResult(npParser.parse());
					}

				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

				String json = null;
				if(error != null &&  error.networkResponse != null && error.networkResponse.data != null){
					json = new String(error.networkResponse.data);
					listener.getResult(json);
				}else{
					listener.getResult("{\n" +
							"\t\t\t\t\t\t\"msg\": \"Network Error OR Data connection Error.\",\n" +
							"\t\t\t\t\t\t\t\"code\": -1001\n" +
							"\t\t\t\t\t}");
				}
			}
		}) {

			/**
			 * Passing some request headers
			 */
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				if (Headdersparams == null || Headdersparams.isEmpty()) {
					HashMap<String, String> headers = new HashMap<String, String>();
					headers.put("Content-Type", "application/json");
					headers.put("charset", "utf-8");
					return headers;
				} else {
					return Headdersparams;
				}
			}

		};
		requestQueue.add(jsonObjReq);
	}

	public void CommonJsonObjectRequestwithJson(final JSONObject params, final HashMap<String, String> Headdersparams, String url, final Class<?> parser, final CustomListener<Object> listener) {

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				mParser = parser;

				try {
					npParser = (AnandParser) mParser.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
					npParser = null;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					npParser = null;
				}

				if (null != response.toString()) {
					npParser.init(response.toString());
					if (npParser.isFeedParsable()) {
						listener.getResult(npParser.parse());
					}

				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

				String json = null;
				if(error != null &&  error.networkResponse != null && error.networkResponse.data != null){
					json = new String(error.networkResponse.data);
					listener.getResult(json);
				}else{
					listener.getResult("{\n" +
							"\t\t\t\t\t\t\"msg\": \"Network Error OR Data connection Error.\",\n" +
							"\t\t\t\t\t\t\t\"code\": -1001\n" +
							"\t\t\t\t\t}");
				}
			}
		}) {

			/**
			 * Passing some request headers
			 */
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				if (Headdersparams == null || Headdersparams.isEmpty()) {
					HashMap<String, String> headers = new HashMap<String, String>();
					headers.put("Content-Type", "application/json");
					headers.put("charset", "utf-8");
					return headers;
				} else {
					return Headdersparams;
				}
			}

		};
		requestQueue.add(jsonObjReq);

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

	}



}