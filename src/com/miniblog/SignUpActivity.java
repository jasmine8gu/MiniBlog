package com.miniblog;

import java.util.Locale;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends SuperActivity {
	
    public void onSignup(View v) {
		EditText etNickName = (EditText)findViewById(R.id.etNickName);
		String strNickName = etNickName.getText().toString();
		if (strNickName == null || strNickName.length() < 1) {
			XUtils.showAlert(this, getString(R.string.nicknameempty));
			return;
		}
		else if (strNickName.length() > 32) {
			XUtils.showAlert(this, getString(R.string.nicknametoolong));
			return;				
		}
		
		EditText etEmail = (EditText)findViewById(R.id.etEmail);
		String strEmail = etEmail.getText().toString();			
		if (strEmail == null || strEmail.length() < 1) {
			XUtils.showAlert(this, getString(R.string.emailempty));
			return;
		}
		else if (strEmail.length() > 32) {
			XUtils.showAlert(this, getString(R.string.emailtoolong));
			return;				
		}
		
		EditText etPassword = (EditText)findViewById(R.id.etPassword);
		String strPassword = etPassword.getText().toString();
		if (strPassword == null || strPassword.length() < 1) {
			XUtils.showAlert(this, getString(R.string.passwordempty));
			return;
		}
		else if (strPassword.length() > 32) {
			XUtils.showAlert(this, getString(R.string.passwordtoolong));
			return;				
		}
		
		EditText etConfirmPassword = (EditText)findViewById(R.id.etConfirmPassword);
		String strConfirmPassword = etConfirmPassword.getText().toString();
		if (strConfirmPassword == null || strConfirmPassword.length() < 1) {
			XUtils.showAlert(this, getString(R.string.confirmpasswordempty));
			return;
		}
		
		if (strPassword.equals(strConfirmPassword) == false) {
			XUtils.showAlert(this, getString(R.string.passwordnotmatch));
			return;			
		}

		strEmail = strEmail.toLowerCase(Locale.getDefault());
		DbAsyncTask dbAsyncTask = new DbAsyncTask();
		dbAsyncTask.setListener(SignUpActivity.this);
		dbAsyncTask.execute("SignUpCheck", strEmail);
	}
	
	public void callBack(String strFunction, String js) {
		String[] result = XUtils.JASONtoString(js);
		
		if (strFunction.equals("SignUpCheck")) {
			if (result != null && result.length > 0) {
				XUtils.showAlert(this, getString(R.string.emailduplicate));
				return;
			}
			else {
				EditText etNickName = (EditText)findViewById(R.id.etNickName);
				String strNickName = etNickName.getText().toString();
				EditText etEmail = (EditText)findViewById(R.id.etEmail);
				String strEmail = etEmail.getText().toString();
				EditText etPassword = (EditText)findViewById(R.id.etPassword);
				String strPassword = etPassword.getText().toString();
				
				DbAsyncTask dbAsyncTask = new DbAsyncTask();
				dbAsyncTask.setListener(SignUpActivity.this);
				dbAsyncTask.execute("SignUp", strNickName, strEmail, strPassword);
			}
		}
		else if (strFunction.equals("SignUp")) {
			Integer ret = XUtils.JASONtoInt(js);
			if (ret != null && ret != -1) {
				MainActivity.signedIn = true;
				MainActivity.signedUserId = ret;
				
				EditText etNickName = (EditText)findViewById(R.id.etNickName);
				String strNickName = etNickName.getText().toString();
				MainActivity.signedUserNickName = strNickName;
				this.finish();
			}
			else {
				XUtils.showAlert(this, getString(R.string.signupfail));
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

}
