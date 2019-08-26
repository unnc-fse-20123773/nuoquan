package com.nuoquan.service;

import java.util.List;

import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.UserArticleComment;
import com.nuoquan.utils.PagedResult;

public interface ArticleService {
	
	/**
	 *  分页查询全部文章
	 */
	public PagedResult getAllArticles(Integer page, Integer pageSize);
	
	/**
	 * @des:用户喜欢文章
	 * @param userId
	 * @param articleId
	 * @param articleCreaterId
	 */
	public void userLikeArticle(String userId, String articleId, String articleCreaterId);
	
	/**
	 * @des: 用户不喜欢文章/取消点赞
	 * @param userId
	 * @param articleId
	 * @param articleCreaterId
	 */
	public void userUnLikeArticle(String userId, String articleId, String articleCreaterId);
	
	/**
	 *  查询阳面文章
	 */
	public PagedResult searchYangArticles(Article article, Integer isSaveRecord, Integer page, Integer pageSize);
	
	/**
	 * 获取热搜词
	 * @return
	 */
	public List<String> getHotWords();
	
	/**
	 * 保存文章
	 */
	public String saveArticle(Article article);
	
	/**
	 * 上传留言到数据库
	 * @param comment
	 */
	public void saveComment(UserArticleComment comment);
	
	/**
	 * 留言分页
	 * @param articleId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public PagedResult getAllComments(String articleId, Integer page, Integer pageSize);
	
	/**
	 * 
	 */
	public void saveArticleImages(ArticleImage articleImage);
	
	/**
	 * 根据公式更新文章热度
	 */
	public void upadtePopByFunction();
}
