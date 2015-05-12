package com.miniblog;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends SuperActivity {
	private OnClickListener onForgetPassword = new OnClickListener() {
	    public void onClick(View v) {
	        Intent intent = new Intent(SignInActivity.this, ForgetPwdActivity.class);
	        startActivity(intent);
	    }
	};
	
	private OnClickListener onSignup = new OnClickListener() {
	    public void onClick(View v) {
	        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
	        startActivity(intent);
	    }
	};
	
	private OnClickListener onSignin = new OnClickListener() {
	    public void onClick(View v) {
			EditText etEmail = (EditText)findViewById(R.id.etEmail);
			String strEmail = etEmail.getText().toString();
			EditText etPassword = (EditText)findViewById(R.id.etPassword);
			String strPassword = etPassword.getText().toString();
			
			if (strEmail == null || strPassword == null || strEmail.length() < 1 || strPassword.length() < 1) {
				AlertDialog alertDialog;
				alertDialog = new AlertDialog.Builder(SignInActivity.this).create();
				alertDialog.setTitle(getString(R.string.info));
				alertDialog.setMessage(getString(R.string.emailpassordempty));
				alertDialog.show();					
				return;
			}
			else if (strEmail.length() > 32 || strPassword.length() > 32) {
				AlertDialog alertDialog;
				alertDialog = new AlertDialog.Builder(SignInActivity.this).create();
				alertDialog.setTitle(getString(R.string.info));
				alertDialog.setMessage(getString(R.string.emailpassordinvalid));
				alertDialog.show();					
				return;
			}
			else {
				strEmail = strEmail.toLowerCase(Locale.getDefault());
				DbAsyncTask dbAsyncTask = new DbAsyncTask();
				dbAsyncTask.setListener(SignInActivity.this);
				dbAsyncTask.execute("SignIn", strEmail, strPassword);
				
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		
		Button btSignIn = (Button)findViewById(R.id.btSignIn);
		btSignIn.setOnClickListener(onSignin);
		
		TextView tvForgetPassword = (TextView)findViewById(R.id.tvForgetPassword);
		tvForgetPassword.setOnClickListener(onForgetPassword);
		
		Button btSignup = (Button)findViewById(R.id.btSignup);
		btSignup.setOnClickListener(onSignup);
	}

	public void callBack(String strFunction, String js) {
		String[] result = XUtils.JASONtoString(js);
		if (result == null) {
			Toast.makeText(this, R.string.emailpassordinvalid, Toast.LENGTH_SHORT).show();
		}
		
		int len = result.length;
		
		if (len >= 1) {
			MainActivity.signedIn = true;
			MainActivity.signedUserId = Integer.parseInt(XUtils.getColValue(result[0], "id"));
			MainActivity.signedUserNickName = XUtils.getColValue(result[0], "nickname");
			this.finish();
			
	        Intent intent = new Intent(this, ProfileActivity.class);
	        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        intent.putExtra("userId", MainActivity.signedUserId);
	        intent.putExtra("nickName", MainActivity.signedUserNickName);
	        startActivity(intent);
		}
		else {
			Toast.makeText(this, R.string.emailpassordinvalid, Toast.LENGTH_SHORT).show();
		}
	}

	public void onResume() {
		super.onResume();
		
		if (MainActivity.signedIn == true) {
			this.finish();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in, menu);
		return true;
	}

}
