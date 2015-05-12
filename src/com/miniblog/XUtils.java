package com.miniblog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class XUtils {
	static final String TAG = "jasmine XUtils";
	
	public static Integer JASONtoInt(String js) {
		try {
			JSONArray jArray = new JSONArray(js);
			return jArray.getInt(0);
		} 
		catch (JSONException e) {
			Log.i(TAG, e.getMessage());
		}
		
		return null;
	}
	
	public static String[] JASONtoString(String js) {
    	try {
        	String[] result;
            JSONArray jArray = new JSONArray(js);
            int len = jArray.length();
            
            result = new String[len];

            for (int i = 0; i < len; i++) {
                JSONObject json_data = jArray.getJSONObject(i);
            	result[i] = json_data.toString();
            }
            
            return result;
        }        
        catch(JSONException e) {
        	Log.i(TAG, e.getMessage());
        }
    	return null;
	}
	
	public static String getColValue(String row, String colName) {
		row = row.substring(1, row.length() - 1);
		
		String[] cols = row.split(",");
		
		for (int i = 0; i < cols.length; i++) {
			String curCol = cols[i];
			
			String[] col = new String[2];
			int sep = curCol.indexOf(':');
			col[0] = curCol.substring(1, sep - 1);
			col[1] = curCol.substring(sep + 1, curCol.length());
			if (col[0].equals(colName)) {
				if (col[1].equals("null")) {
					return "";
				}
				else {
					col[1] = col[1].substring(1, col[1].length() - 1);
					return col[1];
				}
			}
		}
		return null;
	}

	public static void showAlert(Context context, String message) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(message)
	           		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           			public void onClick(DialogInterface dialog, int id) {
	           				dialog.dismiss();
	           			}
	           		});

			AlertDialog alertDialog = builder.create();
			alertDialog.setTitle("Information");
			alertDialog.show();		
	}
}
