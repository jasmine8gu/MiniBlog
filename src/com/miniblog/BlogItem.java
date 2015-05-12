package com.miniblog;

import java.io.Serializable;

public class BlogItem implements Serializable {  	
	private static final long serialVersionUID = 1L;
	
	public int blogId;
	public String title;
	public String updatedatetime;
	
	public BlogItem() {
    }
	
}  

