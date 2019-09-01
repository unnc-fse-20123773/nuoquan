package com.nuoquan.service;

import java.util.List;

import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.UserArticleComment;
import com.nuoquan.pojo.UserLikeArticle;
import com.nuoquan.pojo.UserLikeComment;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.pojo.vo.UserArticleCommentVO;
import com.nuoquan.utils.PagedResult;

public interface ArticleService {
	
	/**
	 * 分页查询全部文章
	 */
	public PagedResult getAllArticles(Integer page, Integer pageSize);
	
	/**
	 * 按 articleId 获取文章
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public ArticleVO getArticleById(String articleId);
	
	/**
	 * @des:用户点赞文章
	 * @param userId
	 * @param articleId
	 * @param articleCreaterId
	 */
	public void userLikeArticle(String userId, String articleId, String articleCreaterId);
	
	/**
	 * @des: 用户取消点赞文章
	 * @param userId
	 * @param articleId
	 * @param articleCreaterId
	 */
	public void userUnLikeArticle(String userId, String articleId, String articleCreaterId);
	
	/**
	 * 查看用户是否点赞了文章
	 * @param userId
	 * @param articleId
	 * @return
	 */
	public boolean isUserLikeArticle(String userId, String articleId);
	
	/**
	 *  查询阳面文章
	 */
	public PagedResult searchYangArticlesContent(Article article, Integer isSaveRecord, Integer page, Integer pageSize);
	
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
	 * 上传评论到数据库
	 * @param comment
	 * @return commentId
	 */
	public String saveComment(UserArticleComment comment);
	
	/**
	 * @des:用户点赞评论
	 * @param userId
	 * @param commentId
	 * @param createrId
	 */
	public void userLikeComment(String userId, String commentId, String createrId);
	
	/**
	 * @des: 用户取消点赞评论
	 * @param userId
	 * @param commentId
	 * @param createrId
	 */
	public void userUnLikeComment(String userId, String commentId, String createrId);
	
	/**
	 * 查看用户是否点赞了评论
	 * @param userId
	 * @param commentId
	 * @return
	 */
	public boolean isUserLikeComment(String userId, String commentId);
	
	/**
	 * 留言分页
	 * @param articleId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public PagedResult getAllComments(String articleId, Integer page, Integer pageSize);
	
	/**
	 * 根据 commentId 获取评论
	 * @param commentId
	 * @return
	 */
	public UserArticleCommentVO getCommentById(String commentId);

	/**
	 * 保存文章图片
	 */
	public void saveArticleImages(ArticleImage articleImage);
	
	/**
	 * 根据公式更新文章热度
	 */
	public void upadtePopByFunction();
	
	/**
	 * 获取热度值前三的文章
	 * @return
	 */
	public List<ArticleVO> getTop3ByPopularity();
}
