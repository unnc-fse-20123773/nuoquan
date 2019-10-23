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
	 * @Description: 文章评论数量累加
	 * @param articleId
	 */
	public void addArticleCommentCount(String articleId);
	
	/**
	 * 搜索文章内容
	 * @param articleContent
	 * @return
	 */
	public List<ArticleVO> searchArticleContentYang(@Param("searchKeywords") String searchKeywords);
	
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
	
	/**
	 * 获取热度值前三的文章
	 * @return
	 */
	public List<ArticleVO> getTop3ByPopularity();
	
	/**
	 * 按 articleId 获取文章
	 * @param articleId
	 * @return
	 */
	public ArticleVO getArticleById(String articleId);

	/**
	 * 按userId获取本人发布的所有文章
	 * @return
	 */
	public List<ArticleVO> queryAllMyHisArticle(String userId);

	/**
	 * 按targetId获取他人的, status为1的所有文章
	 * @param targetId
	 * @return
	 */
	public List<ArticleVO> queryOthersLegalHisArticle(String targetId);
	
}