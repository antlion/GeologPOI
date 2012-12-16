package com.geolog.web.domain;

import java.util.List;





import com.geolog.dominio.Category;

public class CategoryListResponse extends BaseResponse {


	private int count;
	private String icon;
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}


	private List<Category> categories;
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	
	@Override
	public String serialize() {
		return null;

	}


}

