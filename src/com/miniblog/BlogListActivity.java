package com.miniblog;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Color;

public class BlogListActivity extends SuperActivity {
	private int userId = -1;
	private String nickName = "";
	
	private ListView lvBlog;  
	private BlogItemAdapter lvAdapter ;  
	public ArrayList<BlogItem> blogList = new ArrayList<BlogItem>();
	   	
	public void callBack(String strFunction, String js) {
		if (strFunction.equals("BlogList")) {
			String[] result = XUtils.JASONtoString(js);
			if (result == null || result.length == 0) {
				return;
			}
			
			blogList.clear();
			for (int i = 0; i < result.length; i++) {
				BlogItem blogItem = new BlogItem();
				
				blogItem.blogId = Integer.parseInt(XUtils.getColValue(result[i], "id"));
				blogItem.title = XUtils.getColValue(result[i], "title");
				blogItem.updatedatetime = XUtils.getColValue(result[i], "updatedatetime");
				
				blogList.add(blogItem);
			}
			lvAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blog_list);
		
    	SignBar signBar = (SignBar)this.findViewById(R.id.signbar);
    	signBar.onCreate(this);
    	
    	lvBlog = (ListView) findViewById( R.id.lvBlog ); 
    	lvAdapter = new BlogItemAdapter(this, R.layout.blogitem, R.id.tvTitle, blogList);
    	
    	lvBlog.setAdapter(lvAdapter);
        
    	lvBlog.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		int items = parent.getCount();
			    for (int i = 0; i < items; i++) {
			    	View v = parent.getChildAt(i);
			    	if (position == i) {
			    		v.setBackgroundColor(Color.rgb(213, 213, 213));
			    	} 
			    	else {
		                 v.setBackgroundColor(Color.TRANSPARENT);
			    	}
		        }
		           
				Intent i = new Intent(BlogListActivity.this, BlogEditActivity.class);
				BlogItem theBlogItem = lvAdapter.getItem(position);

		        i.putExtra("blogId", theBlogItem.blogId);
		        i.putExtra("userId", userId);
		        i.putExtra("nickName", nickName);

				startActivity(i);
			}
		});	           	
    	
		Bundle b = getIntent().getExtras();
		userId = b.getInt("userId");
		nickName = b.getString("nickName");		
	}

	public void onResume() {
		super.onResume();
		
    	SignBar signBar = (SignBar)this.findViewById(R.id.signbar);
    	signBar.onResume();
    	
		TextView tvNickName = (TextView)findViewById(R.id.tvNickName);
		Button btWrite = (Button)findViewById(R.id.btWrite);
		if (userId == MainActivity.signedUserId) {
			tvNickName.setText("My blog");
			btWrite.setVisibility(View.VISIBLE);
		}
		else {
			tvNickName.setText(nickName + "'s blog");
			btWrite.setVisibility(View.INVISIBLE);
		}
		
		DbAsyncTask dbAsyncTask = new DbAsyncTask();
		dbAsyncTask.setListener(this);
		dbAsyncTask.execute("BlogList", Integer.valueOf(userId).toString());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.blog_list, menu);
		return true;
	}
	
    public void onNew(View v) {
		Intent intent = new Intent(BlogListActivity.this, BlogEditActivity.class);
        intent.putExtra("blogId", -1);
        intent.putExtra("userId", userId);
        intent.putExtra("nickName", nickName);
		startActivity(intent);
    }
}
