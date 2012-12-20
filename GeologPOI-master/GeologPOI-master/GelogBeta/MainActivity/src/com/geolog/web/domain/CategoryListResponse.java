package com.geolog.web.domain;

import java.util.List;

import com.geolog.dominio.Category;

/**
 * Riposta alla richesta delle categorie di ricerca poi
 * 
 * @author Lorenzo
 * 
 */
public class CategoryListResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	// Numero della categorie nella riposta
	private int count;
	// ??
	private String icon;
	// Lista delle categorie
	private List<Category> categories;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

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
