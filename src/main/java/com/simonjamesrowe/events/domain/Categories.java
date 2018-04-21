package com.simonjamesrowe.events.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Categories {

	@JsonProperty("category")
	private List<Category> categoryList;

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}
}
