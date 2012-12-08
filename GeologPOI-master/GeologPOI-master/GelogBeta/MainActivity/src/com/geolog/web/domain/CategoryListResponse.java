package com.geolog.web.domain;

import java.util.List;

import com.geolog.dominio.Category;

public class CategoryListResponse extends BaseResponse {

	
	
	private List<Category> category;
	private int count;
	
	
	


	public List<Category> getCategory() {
		return category;
	}


	public void setCategory(List<Category> category) {
		this.category = category;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}

}
