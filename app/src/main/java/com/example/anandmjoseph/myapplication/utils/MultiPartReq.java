package com.example.anandmjoseph.myapplication.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * Created by Anand on 19/10/15
 *
 * Multipart file upload for audio,video,docs and pdf
 */
public class MultiPartReq extends Request<String> {
    private Response.Listener<String> mListener = null;
    private Response.ErrorListener mEListener;
    private String charset = null;
    private final File mFilePart;
    private final String mStringPart;
    private Map<String, String> parameters;
    private Map<String, String> header;
    private MultipartEntity entity = new MultipartEntity();

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return header;
    }

    public MultiPartReq(String url, Response.ErrorListener eListener,
                        Response.Listener<String> rListener, File file, String stringPart,
                        Map<String, String> param, Map<String, String> head) {
        super(Method.POST, url, eListener);
        mListener = rListener;
        mEListener = eListener;
        mFilePart = file;
        mStringPart = stringPart;
        parameters = param;
        header = head;
        buildMultipartEntity();
    }

    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
            String entityContentAsString = new String(bos.toByteArray());
            Log.e("volley", entityContentAsString);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response)
    {
        String parsed;
        try {
            if(charset != null)
            {
                parsed = new String(response.data, charset);
            }
            else
            {
                parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            }
        } catch (UnsupportedEncodingException e)
        {
            parsed = new String(response.data);
        }
       return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String arg0) {
        mListener.onResponse(arg0);
    }

    /**
     * Build MultiPart entity
     */
    private void buildMultipartEntity() {

        if(mFilePart!=null) {
            FileBody file = new FileBody(mFilePart, mStringPart, "image/jpeg", "utf-8");
            entity.addPart(mStringPart, file);
        }
        try {
            for (String key : parameters.keySet())
                entity.addPart(key, new StringBody(parameters.get(key)));
        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }
}
