package com.miniblog;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SignBar extends RelativeLayout {  	
	SuperActivity parent;
	
	private OnClickListener onProfile = new OnClickListener() {
	    public void onClick(View v) {
	        Intent intent = new Intent(parent, ProfileActivity.class);
	        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        intent.putExtra("userId", MainActivity.signedUserId);
	        intent.putExtra("nickName", MainActivity.signedUserNickName);
	        parent.startActivity(intent);
	    }
	};
	
	private OnClickListener onSignout = new OnClickListener() {
	    public void onClick(View v) {
	    	MainActivity.signedIn = false;
	    	MainActivity.signedUserId = -1;
	    	MainActivity.signedUserNickName = "";
	    	
	        Intent intent = new Intent(parent, MainActivity.class);
	        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        parent.startActivity(intent);
	    }
	};
	
	private OnClickListener onSignIn = new OnClickListener() {
	    public void onClick(View v) {
	        Intent intent = new Intent(parent, SignInActivity.class);
	        parent.startActivity(intent);
	    }
	};
	
	public SignBar(Context context) {
        this(context, null); 
    }  
      
    public SignBar(Context context, AttributeSet attrs) {
        super(context, attrs);  
        LayoutInflater.from(context).inflate(R.layout.signbar, this, true);
    }  
    
    public void onCreate(SuperActivity act) {
    	parent = act;
    	
		Button btProfile = (Button)findViewById(R.id.btProfile);
		btProfile.setOnClickListener(onProfile);
		
    	Button btSignout = (Button)findViewById(R.id.btSignout);
    	btSignout.setOnClickListener(onSignout);
		
		TextView tvSignin = (TextView)findViewById(R.id.tvSignin);
		tvSignin.setOnClickListener(onSignIn);
    }
    
    public void onResume() {
		if (MainActivity.signedIn != true) {
			TextView tvAppName = (TextView)findViewById(R.id.tvAppName);
			tvAppName.setVisibility(View.VISIBLE);
			
			TextView tvSignedInUser = (TextView)findViewById(R.id.tvSignedInUser);
			tvSignedInUser.setVisibility(View.INVISIBLE);
			
			Button btProfile = (Button)findViewById(R.id.btProfile);
			btProfile.setVisibility(View.INVISIBLE);

			Button btSignout = (Button)findViewById(R.id.btSignout);
			btSignout.setVisibility(View.INVISIBLE);
			
			TextView tvSignin = (TextView)findViewById(R.id.tvSignin);
			tvSignin.setVisibility(View.VISIBLE);			
		}
		else {						
			TextView tvAppName = (TextView)findViewById(R.id.tvAppName);
			tvAppName.setVisibility(View.INVISIBLE);
			
			TextView tvSignedInUser = (TextView)findViewById(R.id.tvSignedInUser);
			tvSignedInUser.setText(MainActivity.signedUserNickName);
			tvSignedInUser.setVisibility(View.VISIBLE);

			Button btProfile = (Button)findViewById(R.id.btProfile);
			btProfile.setVisibility(View.VISIBLE);
			
			Button btSignout = (Button)findViewById(R.id.btSignout);
			btSignout.setVisibility(View.VISIBLE);
			
			TextView tvSignin = (TextView)findViewById(R.id.tvSignin);
			tvSignin.setVisibility(View.INVISIBLE);
		}		    	
    }
}  

