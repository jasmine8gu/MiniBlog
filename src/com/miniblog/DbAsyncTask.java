package com.miniblog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class DbAsyncTask extends AsyncTask<String, Void, Object> {
	static final String TAG = "jasmine DbAsyncTask";

	private SuperActivity listener;
	private String strFunction = null;
	private String strItemId = null;
	
	public void setListener(SuperActivity view) {
		listener = view;
	}

	public static Bitmap getHttpBitmap(String url) {
		url = MainActivity.hostAddress + "/" + url;
		Bitmap bitmap = null;
		URL myFileUrl = null;
		
		try {
			myFileUrl = new URL(url);
		} 
		catch (MalformedURLException e) {
			Log.i(TAG, e.getMessage());
		}
		
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setConnectTimeout(0);
			conn.setDoInput(true);
			conn.connect();
			
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
			conn.disconnect();
		} 
		catch (IOException e) {
			Log.i(TAG, e.getMessage());
		}
		
		return bitmap;
	}	
	
	@SuppressLint("UseValueOf")
	@Override
	protected Object doInBackground(String... arg) {
		strFunction = arg[0];
		
		if (strFunction.equals("Image")) {
			strItemId = arg[1];
			Bitmap bitmap = getHttpBitmap(arg[2]);
	    	return bitmap;
		}
				
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        try {
			if (strFunction.equals("UserList")) {
		    	nameValuePairs.add(new BasicNameValuePair("strFunction", arg[0]));
		        nameValuePairs.add(new BasicNameValuePair("startIdx", arg[1]));	
		        nameValuePairs.add(new BasicNameValuePair("count", arg[2]));
			}
			else if (strFunction.equals("Profile")) {
		    	nameValuePairs.add(new BasicNameValuePair("strFunction", arg[0]));
		        nameValuePairs.add(new BasicNameValuePair("userid", arg[1]));
			}
			else if (strFunction.equals("SignIn")) {
		    	nameValuePairs.add(new BasicNameValuePair("strFunction", arg[0]));
		        nameValuePairs.add(new BasicNameValuePair("email", arg[1]));
		        nameValuePairs.add(new BasicNameValuePair("password", arg[2]));
			}
			else if (strFunction.equals("SignUpCheck")) {
		    	nameValuePairs.add(new BasicNameValuePair("strFunction", arg[0]));
		        nameValuePairs.add(new BasicNameValuePair("email", arg[1]));
			}
			else if (strFunction.equals("SignUp")) {
		    	nameValuePairs.add(new BasicNameValuePair("strFunction", arg[0]));
		        nameValuePairs.add(new BasicNameValuePair("nickname", URLEncoder.encode(arg[1], "UTF-8")));
		        nameValuePairs.add(new BasicNameValuePair("email", arg[2]));
		        nameValuePairs.add(new BasicNameValuePair("password", arg[3]));
			}
			else if (strFunction.equals("ProfileUpdate")) {
		    	nameValuePairs.add(new BasicNameValuePair("strFunction", arg[0]));
		        nameValuePairs.add(new BasicNameValuePair("userid", arg[1]));
		        nameValuePairs.add(new BasicNameValuePair("nickname", URLEncoder.encode(arg[2], "UTF-8")));
		        nameValuePairs.add(new BasicNameValuePair("age", arg[3]));
		        nameValuePairs.add(new BasicNameValuePair("gender", URLEncoder.encode(arg[4], "UTF-8")));
		        nameValuePairs.add(new BasicNameValuePair("breed", URLEncoder.encode(arg[5], "UTF-8")));
			}
			else if (strFunction.equals("ForgetPassword")) {
		    	nameValuePairs.add(new BasicNameValuePair("strFunction", arg[0]));
		        nameValuePairs.add(new BasicNameValuePair("email", arg[1]));
			}
			else if (strFunction.equals("BlogList")) {
		    	nameValuePairs.add(new BasicNameValuePair("strFunction", arg[0]));
		        nameValuePairs.add(new BasicNameValuePair("userid", arg[1]));
			}
			else if (strFunction.equals("BlogDetail")) {
		    	nameValuePairs.add(new BasicNameValuePair("strFunction", arg[0]));
		        nameValuePairs.add(new BasicNameValuePair("blogid", arg[1]));
			}
			else if (strFunction.equals("BlogNew")) {
		    	nameValuePairs.add(new BasicNameValuePair("strFunction", arg[0]));
		        nameValuePairs.add(new BasicNameValuePair("userid", arg[2]));
				nameValuePairs.add(new BasicNameValuePair("title", URLEncoder.encode(arg[3], "UTF-8")));
		        nameValuePairs.add(new BasicNameValuePair("content", URLEncoder.encode(arg[4], "UTF-8")));
			}				
			else if (strFunction.equals("BlogUpdate")) {
		    	nameValuePairs.add(new BasicNameValuePair("strFunction", arg[0]));
		        nameValuePairs.add(new BasicNameValuePair("blogid", arg[1]));
				nameValuePairs.add(new BasicNameValuePair("title", URLEncoder.encode(arg[2], "UTF-8")));
		        nameValuePairs.add(new BasicNameValuePair("content", URLEncoder.encode(arg[3], "UTF-8")));
			}				
		} 
        catch (Exception e) {
        	Log.i(TAG, e.getMessage());
		}
		
        InputStream is = null;

        try {
        	HttpClient httpclient = new DefaultHttpClient();
        	httpclient.getParams().setParameter("http.socket.timeout", new Integer(10000));
            HttpPost httppost = new HttpPost(MainActivity.hostAddress + "/miniblog.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }
        catch(Exception e) {
        	
        	if (e.getMessage() == null) {
        		Log.i(TAG, "Get a null exception!" + e.toString());
        	}
        	else {
        		Log.i(TAG, e.getMessage());
        	}
        }
        
    	String js = "";
    	try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }
            
            is.close();

            js = sb.toString();
            //Log.i(TAG, js);
        }
        catch(Exception e) {
        	Log.i(TAG, e.getMessage());
        }
    	return js;    	
	}
	
	protected void onPostExecute(Object result) {
		if (listener == null) {
			return;
		}
		
		if (strFunction.equals("Image")) {
			listener.callBack(strFunction, strItemId, (Bitmap)result);
		}
		else {
			listener.callBack(strFunction, (String)result);
		}
    }
}
