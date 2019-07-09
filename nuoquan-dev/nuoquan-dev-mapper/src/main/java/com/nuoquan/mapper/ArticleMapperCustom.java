package com.nuoquan.mapper;

import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.utils.MyMapper;
import java.util.List;

public interface ArticleMapperCustom extends MyMapper<Article> {
	
	public List<ArticleVO> queryAllArticles();
	
}