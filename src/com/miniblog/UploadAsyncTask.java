package com.miniblog;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public class UploadAsyncTask extends AsyncTask<String, Void, String> {
	static final String TAG = "jasmine UploadAsyncTask";
	
	private SuperActivity listener;
	private String strFunction = null;
	private Bitmap bmpPicture = null;
	
	public void setListener(SuperActivity view) {
		listener = view;
	}

	public void setPicture(Bitmap bmp) {
		bmpPicture = bmp;
	}

	@Override
	protected String doInBackground(String... arg) {
		strFunction = arg[0];
	
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		bmpPicture.compress(Bitmap.CompressFormat.JPEG, 90, bao);

		byte [] ba = bao.toByteArray();
		String ba1 = Base64.encodeBytes(ba);

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	nameValuePairs.add(new BasicNameValuePair("strFunction", arg[0]));
		if (strFunction.equals("UserPicture")) {
			nameValuePairs.add(new BasicNameValuePair("userid",Integer.valueOf(arg[1]).toString()));
		}    	
		else if (strFunction.equals("BlogPicture")) {
			nameValuePairs.add(new BasicNameValuePair("blogid",Integer.valueOf(arg[1]).toString()));
			nameValuePairs.add(new BasicNameValuePair("userid",Integer.valueOf(arg[2]).toString()));
		}
		nameValuePairs.add(new BasicNameValuePair("image",ba1));

		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(MainActivity.hostAddress + "/upload.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
            int ch;
            StringBuffer b =new StringBuffer();
            while ((ch = is.read()) != -1) {
            	b.append( (char)ch );
            }
            return b.toString();
		}
		catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}	
		
        return null;
	}
	
	protected void onPostExecute(String result) {
		if (listener == null) {
			return;
		}
		
		listener.callBack(strFunction, result);
    }
}
