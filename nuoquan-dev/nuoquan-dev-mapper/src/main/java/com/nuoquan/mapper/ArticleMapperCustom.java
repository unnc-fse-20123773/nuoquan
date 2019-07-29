package com.nuoquan.mapper;

import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.utils.MyMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ArticleMapperCustom extends MyMapper<Article> {
	
	public List<ArticleVO> queryAllArticles();
	
	/**
	 * @Description: 对视频喜欢的数量进行累加
	 * @param articleId
	 */
	public void addArticleLikeCount(String articleId);
	
	
	/**
	 * @description: 对视频喜欢的数量进行累减
	 * 
	 */
	public void reduceArticleLikeCount(String articleId);
	
	public List<ArticleVO> searchArticleContentYang(@Param("articleContent") String articleContent);
}