package com.nuoquan.service;

import com.nuoquan.utils.PagedResult;

public interface ArticleService {
	
	/**
	 *  分页查询全部文章
	 */
	public PagedResult getAllArticles(Integer page, Integer pageSize);
}
