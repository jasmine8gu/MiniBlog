package com.miniblog;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetPwdActivity extends SuperActivity {

	private OnClickListener onSend = new OnClickListener() {
	    public void onClick(View v) {
			EditText etEmail = (EditText)findViewById(R.id.etEmail);
			String strEmail = etEmail.getText().toString();
			if (strEmail.length() > 1) {
				DbAsyncTask dbAsyncTask = new DbAsyncTask();
				dbAsyncTask.setListener(ForgetPwdActivity.this);
				dbAsyncTask.execute("ForgetPassword", strEmail);
			}
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpwd);
		
		Button btOK = (Button)findViewById(R.id.btOK);
		btOK.setOnClickListener(onSend);
	}

	public void callBack(String strFunction, String js) {
		if (strFunction.equals("ForgetPassword")) {
			Integer ret = XUtils.JASONtoInt(js);
			if (ret != null && ret == 1) {
				Toast.makeText(this, R.string.sendemailsuccess, Toast.LENGTH_SHORT).show();
			}
			else {
				XUtils.showAlert(this, getString(R.string.sendemailfail));
			}
		}	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgetpwd, menu);
		return true;
	}

}
