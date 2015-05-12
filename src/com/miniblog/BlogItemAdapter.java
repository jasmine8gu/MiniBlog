package com.miniblog;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BlogItemAdapter extends ArrayAdapter<BlogItem> implements OnClickListener {
    private Context context;
    ArrayList<BlogItem> blogList;
    
    public BlogItemAdapter(Context c, int resource, int textViewResourceId, ArrayList<BlogItem> bList) {
    	super(c, textViewResourceId);
    	context = c;
    	
    	blogList = bList;
    }
    
	@Override
	public int getCount() {
    	if (blogList == null) {
    		return 0;
    	}

		return blogList.size();
	}

	@Override
	public BlogItem getItem(int index) {
    	if (blogList == null || index >= blogList.size()) {
    		return null;
    	}
    	
    	return blogList.get(index);
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (rowView == null) {
	        rowView = inflater.inflate(R.layout.blogitem, parent, false);
        }
        
        BlogItem blogItem = getItem(position);
        if (blogItem != null) {
	    	TextView tvTitle = (TextView)rowView.findViewById(R.id.tvTitle);
	    	tvTitle.setText(blogItem.title);
	    	
	    	TextView tvDatetime = (TextView)rowView.findViewById(R.id.tvDatetime);
	    	tvDatetime.setText(blogItem.updatedatetime);
        }
        
        return rowView;
    }
    
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
