package com.miniblog;

import java.io.InputStream;

import android.net.Uri;
import android.os.Bundle;
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
import android.provider.MediaStore;

public class BlogEditActivity extends SuperActivity {
	static final String TAG = "jasmine BlogEditActivity";
	
	private int blogId = -1;
	private int userId = -1;
	private String nickName = "";
	
	private Bitmap bmpBlog = null;
	Bitmap bmpPicture = null;
	
	private final int SELECT_PICTURE = 1;
	
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

	public void onSave(View v) {
		EditText etTitle = (EditText)findViewById(R.id.etTitle);
		String strTitle = etTitle.getText().toString();
		
		if (strTitle == null || strTitle.length() == 0) {
			XUtils.showAlert(this, getString(R.string.titleempty));
			return;				
		}
		else if (strTitle.length() > 64) {
			XUtils.showAlert(this, getString(R.string.titletoolong));
			return;							
		}
		
		EditText etContent = (EditText)findViewById(R.id.etContent);
		String strContent = etContent.getText().toString();
		
		if (strContent == null || strContent.length() == 0) {
			XUtils.showAlert(this, getString(R.string.contentempty));
			return;				
		}
		else if (strContent.length() > 512) {
			XUtils.showAlert(this, getString(R.string.contenttoolong));
			return;							
		}
		
		if (blogId == -1) {
			DbAsyncTask dbAsyncTask = new DbAsyncTask();
			dbAsyncTask.setListener(BlogEditActivity.this);
			dbAsyncTask.execute("BlogNew", Integer.valueOf(blogId).toString(), Integer.valueOf(MainActivity.signedUserId).toString(), strTitle, strContent);
		}
		else {
			DbAsyncTask dbAsyncTask = new DbAsyncTask();
			dbAsyncTask.setListener(BlogEditActivity.this);
			dbAsyncTask.execute("BlogUpdate", Integer.valueOf(blogId).toString(), strTitle, strContent);
		}
    }
		
	public void callBack(String strFunction, String js) {
		if (strFunction.equals("BlogDetail")) {
			String[] result = XUtils.JASONtoString(js);
			if (result == null || result.length < 1) {
				return;
			}
			
			EditText etTitle = (EditText)findViewById(R.id.etTitle);
			etTitle.setText(XUtils.getColValue(result[0], "title"));
			
			EditText etContent = (EditText)findViewById(R.id.etContent);
			etContent.setText(XUtils.getColValue(result[0], "content"));

    		TextView lbInfo = (TextView)findViewById(R.id.lbInfo);
    		lbInfo.setText(nickName + " " + XUtils.getColValue(result[0], "updatedatetime"));
	    				
			String url = new String(XUtils.getColValue(result[0], "url"));
			if (url.length() == 0) {
				ImageView ivPicture = (ImageView)this.findViewById(R.id.ivPicture);
				ivPicture.setImageBitmap(bmpBlog);
			}
			else {
				DbAsyncTask imageAsyncTask = new DbAsyncTask();
				imageAsyncTask.setListener(this);
				imageAsyncTask.execute("Image", "", url);
			}
		}
		else if (strFunction.equals("BlogNew")) {
			Integer ret = XUtils.JASONtoInt(js);
			if (ret != null && ret != -1) {
				blogId = ret;
								
				if (bmpPicture != null) {
					UploadAsyncTask uploadAsyncTask = new UploadAsyncTask();
					uploadAsyncTask.setListener(BlogEditActivity.this);
					uploadAsyncTask.setPicture(bmpPicture);
					uploadAsyncTask.execute("BlogPicture", Integer.valueOf(blogId).toString(), Integer.valueOf(MainActivity.signedUserId).toString());
				}
				else {
					Toast.makeText(this, R.string.blogsavesuccess, Toast.LENGTH_SHORT).show();
				}
			}
			else {
				XUtils.showAlert(this, getString(R.string.blogsavefail));
			}
		}
		else if (strFunction.equals("BlogUpdate")) {
			Integer ret = XUtils.JASONtoInt(js);
			if (ret != null && ret == 1) {
				if (bmpPicture != null) {
					UploadAsyncTask uploadAsyncTask = new UploadAsyncTask();
					uploadAsyncTask.setListener(BlogEditActivity.this);
					uploadAsyncTask.setPicture(bmpPicture);
					uploadAsyncTask.execute("BlogPicture", Integer.valueOf(blogId).toString(), Integer.valueOf(MainActivity.signedUserId).toString());
				}
				else {
					Toast.makeText(this, R.string.blogsavesuccess, Toast.LENGTH_SHORT).show();
				}
			}
			else {
				Toast.makeText(this, R.string.blogsavefail, Toast.LENGTH_SHORT).show();
			}
		}
		else if (strFunction.equals("BlogPicture")) {
			Integer ret = XUtils.JASONtoInt(js);
			if (ret != null && ret == 1) {
				Toast.makeText(this, R.string.blogsavesuccess, Toast.LENGTH_SHORT).show();
	        }			
			else {
				XUtils.showAlert(this, getString(R.string.blogsavefail));
			}
		}
	}
	
	public void callBack(String strFunction, String strItemId, Bitmap image) {
		ImageView ivPicture = (ImageView)this.findViewById(R.id.ivPicture);
		ivPicture.setImageBitmap(image);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blog_edit);
		
    	SignBar signBar = (SignBar)this.findViewById(R.id.signbar);
    	signBar.onCreate(this);
    	
		Bundle b = getIntent().getExtras();
		blogId = b.getInt("blogId");
		userId = b.getInt("userId");
		nickName = b.getString("nickName");
		
		bmpBlog = BitmapFactory.decodeResource(getResources(), R.drawable.blog);
		
		if (blogId > 0) {
	    	DbAsyncTask dbAsyncTask = new DbAsyncTask();
			dbAsyncTask.setListener(this);
			dbAsyncTask.execute("BlogDetail", Integer.valueOf(blogId).toString());
		}
		else {
			ImageView ivPicture = (ImageView)this.findViewById(R.id.ivPicture);
			ivPicture.setImageBitmap(bmpBlog);
		}		
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
			
			EditText etTitle = (EditText)findViewById(R.id.etTitle);
			etTitle.setEnabled(true);
			
			EditText etContent = (EditText)findViewById(R.id.etContent);
			etContent.setEnabled(true);
    	}
    	else {
			Button btPicture = (Button)findViewById(R.id.btPicture);
			btPicture.setVisibility(View.INVISIBLE);
			
			Button btSave = (Button)findViewById(R.id.btSave);
			btSave.setVisibility(View.INVISIBLE);
			    		
			EditText etTitle = (EditText)findViewById(R.id.etTitle);
			etTitle.setEnabled(false);
			
			EditText etContent = (EditText)findViewById(R.id.etContent);
			etContent.setEnabled(false);
    	}    	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.blog_edit, menu);
		return true;
	}
}
