package com.miniblog;

import android.os.Bundle;
import android.view.Menu;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends SuperActivity {
	public static String hostAddress="http://xxx.xxx.xxx.xxx/miniblog";
	
	public static boolean signedIn = false;
	public static int signedUserId = -1;
	public static String signedUserNickName = null;

	private int COL_NUM = 3;
	private int ROW_NUM = 4;
	
	private UserItem[] userItem = null;
	
	public static final int REFRESH = 1;

	private OnClickListener onProfile = new OnClickListener() {
	    public void onClick(View v) {
	        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
	        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        intent.putExtra("userId", ((UserItem)v).getUserId());
	        intent.putExtra("nickName", ((UserItem)v).getNickName());
	        startActivity(intent);
	    }
	};
	
	public void callBack(String strFunction, String js) {
		if (!strFunction.equals("UserList")) {
			return;
		}
		
		String[] result = XUtils.JASONtoString(js);
		if (result == null || result.length == 0) {
			return;
		}
		
		for (int i = 0; i < ROW_NUM * COL_NUM; i++) {
			if (i >= result.length) {
				break;
			}
			
			userItem[i].setUserId(Integer.parseInt(XUtils.getColValue(result[i], "id")));
			userItem[i].setNickName(XUtils.getColValue(result[i], "nickname"));
			
			String url = new String(XUtils.getColValue(result[i], "url"));
			DbAsyncTask imageAsyncTask = new DbAsyncTask();
			imageAsyncTask.setListener(this);
			imageAsyncTask.execute("Image", Integer.valueOf(i).toString(), url);
		}
	}

	public void callBack(String strFunction, String strItemId, Bitmap image) {
		if (!strFunction.equals("Image")) {
			return;
		}
		
		int itemId = Integer.parseInt(strItemId);
		userItem[itemId].setPicture(image);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

    	SignBar signBar = (SignBar)this.findViewById(R.id.signbar);
    	signBar.onCreate(this);
    	
    	userItem = new UserItem[ROW_NUM * COL_NUM];
    	userItem[0] = (UserItem) findViewById(R.id.useritem0);
    	userItem[1] = (UserItem) findViewById(R.id.useritem1);
    	userItem[2] = (UserItem) findViewById(R.id.useritem2);
    	userItem[3] = (UserItem) findViewById(R.id.useritem3);
    	userItem[4] = (UserItem) findViewById(R.id.useritem4);
    	userItem[5] = (UserItem) findViewById(R.id.useritem5);
    	userItem[6] = (UserItem) findViewById(R.id.useritem6);
    	userItem[7] = (UserItem) findViewById(R.id.useritem7);
    	userItem[8] = (UserItem) findViewById(R.id.useritem8);
    	userItem[9] = (UserItem) findViewById(R.id.useritem9);
    	userItem[10] = (UserItem) findViewById(R.id.useritem10);
    	userItem[11] = (UserItem) findViewById(R.id.useritem11);

		for (int j = 0; j < ROW_NUM; j++) {
		    for (int i = 0; i < COL_NUM; i++) {
		    	int idx = COL_NUM * j + i;
		    	userItem[idx].onCreate();
		    	userItem[idx].setOnClickListener(onProfile);
		    }
		}		
	}

	public void onResume() {
		super.onResume();
		
    	SignBar signBar = (SignBar)this.findViewById(R.id.signbar);
    	signBar.onResume();
    	
		DbAsyncTask dbAsyncTask = new DbAsyncTask();
		dbAsyncTask.setListener(this);
		dbAsyncTask.execute("UserList", Integer.valueOf(0).toString(), Integer.valueOf(COL_NUM * ROW_NUM).toString());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}