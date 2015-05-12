package com.miniblog;

import java.io.InputStream;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ProfileActivity extends SuperActivity {
	static final String TAG = "jasmine ProfileActivity";
	
	private int userId = -1;
	private String nickName = "";

	private final int SELECT_PICTURE = 1;
	Bitmap bmpPicture = null;
	
    public void onPicture(View v) {
    	Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);    	
        intent.setType("image/*");
		startActivityForResult(intent, SELECT_PICTURE);        
    }
	
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
        	try {
				Uri imageUri = data.getData();
				InputStream imageStream = (InputStream) (getContentResolver().openInputStream(imageUri));
				
				bmpPicture = BitmapFactory.decodeStream(imageStream);
	        	if (bmpPicture != null) {
		            ImageView ivPicture = (ImageView) findViewById(R.id.ivPicture);
		            ivPicture.setImageBitmap(bmpPicture);		            
	        	}
			} 
        	catch (Exception e) {
        		Log.i(TAG, e.getMessage());
			}        	
        }
    }

	public void callBack(String strFunction, String js) {
		if (strFunction.equals("Profile")) {
			String[] result = XUtils.JASONtoString(js);
			if (result == null || result.length < 1) {
				return;
			}
			
			EditText etAge = (EditText)this.findViewById(R.id.etAge);
			etAge.setText(XUtils.getColValue(result[0], "age"));
			
			EditText etLocation = (EditText)this.findViewById(R.id.etGender);
			etLocation.setText(XUtils.getColValue(result[0], "gender"));
			
			EditText etJob = (EditText)this.findViewById(R.id.etBreed);
			etJob.setText(XUtils.getColValue(result[0], "breed"));
			
			String url = new String(XUtils.getColValue(result[0], "url"));
			DbAsyncTask imageAsyncTask = new DbAsyncTask();
			imageAsyncTask.setListener(this);
			imageAsyncTask.execute("Image", "", url);
		}
		else if (strFunction.equals("ProfileUpdate")) {
			Integer ret = XUtils.JASONtoInt(js);
			if (ret != null && ret == 1) {
				EditText etNickName = (EditText)findViewById(R.id.etNickName);
				nickName = etNickName.getText().toString();
				MainActivity.signedUserNickName = nickName;
				TextView tvSignedInUser = (TextView)findViewById(R.id.tvSignedInUser);
				tvSignedInUser.setText(MainActivity.signedUserNickName);
				
				if (bmpPicture != null) {
					UploadAsyncTask uploadAsyncTask = new UploadAsyncTask();
					uploadAsyncTask.setListener(ProfileActivity.this);
					uploadAsyncTask.setPicture(bmpPicture);
					uploadAsyncTask.execute("UserPicture", Integer.valueOf(MainActivity.signedUserId).toString());
				}
				else {
					Toast.makeText(this, R.string.savesuccess, Toast.LENGTH_SHORT).show();
				}
			}
			else {
				XUtils.showAlert(this, getString(R.string.savefail));
			}
		}			
		else if (strFunction.equals("UserPicture")) {
			Integer ret = XUtils.JASONtoInt(js);
			if (ret != null && ret == 1) {
				Toast.makeText(this, R.string.savesuccess, Toast.LENGTH_SHORT).show();
	        }			
			else {
				XUtils.showAlert(this, getString(R.string.savefail));
			}
		}
	}
	
	public void callBack(String strFunction, String strItemId, Bitmap image) {
		if (!strFunction.equals("Image")) {
			return;
		}
		
		ImageView ivPicture = (ImageView)this.findViewById(R.id.ivPicture);
		ivPicture.setImageBitmap(image);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

    	SignBar signBar = (SignBar)this.findViewById(R.id.signbar);
    	signBar.onCreate(this);
    	
		Bundle b = getIntent().getExtras();
		userId = b.getInt("userId");
		nickName = b.getString("nickName");
		
		DbAsyncTask dbAsyncTask = new DbAsyncTask();
		dbAsyncTask.setListener(this);
		dbAsyncTask.execute("Profile", Integer.valueOf(userId).toString());
	}
	
	public void onResume() {
		super.onResume();
		
    	SignBar signBar = (SignBar)this.findViewById(R.id.signbar);
    	signBar.onResume();
    	
		if (userId == MainActivity.signedUserId) {
			Button btPicture = (Button)findViewById(R.id.btPicture);
			btPicture.setVisibility(View.VISIBLE);
			
			Button btSave = (Button)findViewById(R.id.btSave);
			btSave.setVisibility(View.VISIBLE);
			
			TextView lbMyblog = (TextView)findViewById(R.id.lbMyblog);
			lbMyblog.setText("My Blog");
			
			EditText etNickName = (EditText)findViewById(R.id.etNickName);
			etNickName.setText(nickName);
			etNickName.setEnabled(true);
			
			EditText etAge = (EditText)findViewById(R.id.etAge);
			etAge.setEnabled(true);
			
			EditText etGender = (EditText)findViewById(R.id.etGender);
			etGender.setEnabled(true);
			
			EditText etBreed = (EditText)findViewById(R.id.etBreed);
			etBreed.setEnabled(true);
		}
		else {
			Button btPicture = (Button)findViewById(R.id.btPicture);
			btPicture.setVisibility(View.INVISIBLE);
			
			Button btSave = (Button)findViewById(R.id.btSave);
			btSave.setVisibility(View.INVISIBLE);
			
			TextView lbMyblog = (TextView)findViewById(R.id.lbMyblog);
			lbMyblog.setText(nickName + "'s Blog");

			EditText etNickName = (EditText)findViewById(R.id.etNickName);
			etNickName.setText(nickName);
			etNickName.setEnabled(false);
			
			EditText etAge = (EditText)findViewById(R.id.etAge);
			etAge.setEnabled(false);
			
			EditText etGender = (EditText)findViewById(R.id.etGender);
			etGender.setEnabled(false);
			
			EditText etBreed = (EditText)findViewById(R.id.etBreed);
			etBreed.setEnabled(false);			
		}		
	}		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}
	
	public void onSave(View v) {
		EditText etNickName = (EditText)findViewById(R.id.etNickName);
		String strNickName = etNickName.getText().toString();
		EditText etAge = (EditText)findViewById(R.id.etAge);
		String strAge = etAge.getText().toString();
		EditText etGender = (EditText)findViewById(R.id.etGender);
		String strGender = etGender.getText().toString();
		EditText etBreed = (EditText)findViewById(R.id.etBreed);
		String strBreed = etBreed.getText().toString();
		
		if (strAge == null || strAge.length() == 0) {
			strAge = "0";
		}
		
		if (strNickName.length() > 32) {
			XUtils.showAlert(this, getString(R.string.nicknametoolong));
			return;
		}
		
		if (strGender.length() > 1) {
			XUtils.showAlert(this, getString(R.string.gendertoolong));
			return;
		}
		
		if (strBreed.length() > 32) {
			XUtils.showAlert(this, getString(R.string.breedtoolong));
			return;				
		}
		
		DbAsyncTask dbAsyncTask = new DbAsyncTask();
		dbAsyncTask.setListener(ProfileActivity.this);
		dbAsyncTask.execute("ProfileUpdate", Integer.valueOf(MainActivity.signedUserId).toString(), strNickName, strAge, strGender, strBreed);
    }
	
    public void onBlog(View v) {
		Intent intent = new Intent(ProfileActivity.this, BlogListActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("nickName", nickName);
		startActivity(intent);
    }
		
}
