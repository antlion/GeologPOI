package com.geolog.dominio.web;

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
	/**
	 * @uml.property  name="count"
	 */
	private int count;
	// ??
	/**
	 * @uml.property  name="icon"
	 */
	private String icon;
	// Lista delle categorie
	/**
	 * @uml.property  name="categories"
	 */
	private List<Category> categories;

	/**
	 * @return
	 * @uml.property  name="icon"
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 * @uml.property  name="icon"
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return
	 * @uml.property  name="count"
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 * @uml.property  name="count"
	 */
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
