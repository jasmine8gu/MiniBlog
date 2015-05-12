package com.miniblog;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserItem extends RelativeLayout {  	
	private int userId;
	private String nickName;
	private Bitmap bmpPicture;
	
	private ImageView ivPicture = null;
	private TextView tvNickName = null;
	
	public UserItem(Context context) {
        this(context, null); 
    }  
      
    public UserItem(Context context, AttributeSet attrs) {
        super(context, attrs);  
        LayoutInflater.from(context).inflate(R.layout.useritem, this, true);
    }  
    
    public void onCreate() {
    	ivPicture = (ImageView)findViewById(R.id.ivUserItemPicture);
		tvNickName = (TextView)findViewById(R.id.tvUserItemNickName);
    }
    
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String sNickName) {
		nickName = sNickName;
		tvNickName.setText(sNickName);
	}
	
	public Bitmap getPicture() {
		return bmpPicture;
	}

	public void setPicture(Bitmap bPicture) {
		bmpPicture = bPicture;
		ivPicture.setImageBitmap((Bitmap)bPicture);
	}
	
}  

