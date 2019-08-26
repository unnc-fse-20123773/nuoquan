package com.nuoquan.mapper;

import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.utils.MyMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ArticleMapperCustom extends MyMapper<Article> {
	
	public List<ArticleVO> queryAllArticles();
	
	/**
	 * @Description: 对文章喜欢的数量进行累加
	 * @param articleId
	 */
	public void addArticleLikeCount(String articleId);
	
	
	/**
	 * @description: 对文章喜欢的数量进行累减
	 * @param articleId
	 */
	public void reduceArticleLikeCount(String articleId);
	
	/**
	 * 搜索文章内容
	 * @param articleContent
	 * @return
	 */
	public List<ArticleVO> searchArticleContentYang(@Param("articleContent") String articleContent);
	
	/**
	 * 为文章增加 num 的浏览量
	 * @param articleId
	 * @param num
	 */
	public void addViewNum(String articleId, int num);
	
	/**
	 * 按公式更新热度
	 * (24小时内的点赞数+24小时内的评论数)
	 */
	public void upadtePopByFunction();
}