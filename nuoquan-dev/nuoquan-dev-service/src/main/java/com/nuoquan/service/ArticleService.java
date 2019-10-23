package com.nuoquan.service;

import java.util.List;

import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.UserArticleComment;
import com.nuoquan.pojo.UserLikeArticle;
import com.nuoquan.pojo.UserLikeComment;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.pojo.vo.UserArticleCommentVO;
import com.nuoquan.pojo.vo.UserLikeVO;
import com.nuoquan.utils.PagedResult;

public interface ArticleService {
	
	/**
	 * 分页查询全部文章
	 */
	public PagedResult getAllArticles(Integer page, Integer pageSize, String userId);
	
	/**
	 * 按 articleId 获取文章
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public ArticleVO getArticleById(String articleId, String userId);
	
	/**
	 * @des:用户点赞文章
	 * @param userId
	 * @param articleId
	 * @param articleCreaterId
	 * @return id in DB
	 */
	public UserLikeArticle userLikeArticle(String userId, String articleId, String articleCreaterId, Integer signFlag);
	
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
	public PagedResult searchYangArticlesContent(Integer isSaveRecord, Integer page, Integer pageSize, Article article, String userId);
	
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
	 * 删除文章
	 * @param articleId
	 */
	public void deleteArticle(String articleId);
	
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
	 * @return id in DB
	 */
	public UserLikeComment userLikeComment(String userId, String commentId, String createrId, Integer signFlag);
	
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
	 * 留言分页  父评论
	 * @param page
	 * @param pageSize
	 * @param articleId
	 * @param userId
	 * @return
	 */
	public PagedResult getMainComments(Integer page, Integer pageSize, String articleId, String userId);
	
	/**
	 * 留言分页 子评论
	 */
	public PagedResult getSonComments(Integer page, Integer pageSize, String underCommentId, String userId);
	
	/**
	 * 根据 commentId 获取评论
	 * @param commentId
	 * @return
	 */
	public UserArticleCommentVO getCommentById(String commentId, String userId);

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
	public List<ArticleVO> getTop3ByPopularity(String userId);
	
	/**
	 * 批量签收点赞文章消息
	 * @param msgIdList
	 */
	public void updateLikeArticleSigned(List<String> msgIdList);
	
	/**
	 * 批量签收点赞评论消息
	 * @param msgIdList
	 */
	public void updateLikeCommentSigned(List<String> msgIdList);
	
	/**
	 * 批量签收评论消息
	 * @param msgIdList
	 */
	public void updateCommentSigned(List<String> msgIdList);
	
	/**
	 * 按作者id获取未签收的点赞消息
	 * @param userId
	 * @return
	 */
	public List<UserLikeVO> getUnsignedLikeMsg(String userId);
	
	/**
	 * 按作者id获取未签收的评论消息
	 * @param userId
	 * @return
	 */
	public List<UserArticleCommentVO> getUnsignedCommentMsg(String userId);
	
	public void banArticle(String articleId);
	
	public void passArticle(String articleId);
	
	public void banComment(String commentId);
	
	public void passComment(String commentId);
	
	
	/**
	 * 操作者本人查看自己发布过的所有文章
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public PagedResult getAllMyHisArticle(Integer page, Integer pageSize, String userId);

	/**
	 * 操作者查看他人发布过的文章(状态为1的文章)
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @param targetId
	 * @return
	 */
	public PagedResult gerOtherslegalHisArticle(Integer page, Integer pageSize, String userId, String targetId);
}
