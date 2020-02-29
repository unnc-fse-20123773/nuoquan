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
	public PagedResult list(Integer page, Integer pageSize);
	

	/**
	 * 列出所有等待审核的文章
	 * @return
	 */
	public PagedResult listCheckOnly(Integer page, Integer pageSize);
	
	/**
	 * 分页查询全部发布的文章
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
	 *  按关键词搜索阳面文章，支持多关键词以空格分割
	 *  TODO: 可以增加多种排序模式 @author jerrio
	 */
	public PagedResult searchArticleYang(Integer isSaveRecord, Integer page, Integer pageSize, String searchText, String userId);
	
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
	public void deleteArticle(String articleId, String userId);
	
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
	 * @param type   0 -- 按时间查询, 1 -- 按热度查询
	 * @param articleId
	 * @param userId
	 * @return
	 */
	public PagedResult getMainComments(Integer page, Integer pageSize, Integer type, String articleId, String userId);
	
	/**
	 * 留言分页 子评论
	 * @param type  0 -- 按时间查询, 1 -- 按热度查询
	 */
	public PagedResult getSonComments(Integer page, Integer pageSize, Integer type, String underCommentId, String userId);
	
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
	 * @param pageSize 
	 * @param page 
	 * @return
	 */
	public PagedResult getArticleByPopurity(Integer page, Integer pageSize, String userId);
	
	/**
	 * Set the status to unreadable
	 * @param articleId
	 */
	public void fDeleteArticle(String articleId);
	
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
	
	/**
	 * 浏览量+1
	 * @param articleId
	 */
	public void addViewCount(String articleId);

	/**
	 * 修改文章状态(可批量)
	 */
	public int updateArticleStatus(String articleIds, int status);


	/**
	 * 请求所有关注的人的文章
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public PagedResult getAllSubscribedAuthorArticles(Integer page, Integer pageSize, String userId);


	/**
	 * 获取关注的人的文章, 按热度排序
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public PagedResult getAllSubscribedAuthorArticlesByPopularity(Integer page, Integer pageSize, String userId);


	/**
	 * 根据传进来的text搜索文章(Text=标签)
	 * @param isSaveRecord
	 * @param page
	 * @param pageSize
	 * @param searchText
	 * @param userId
	 * @return
	 */
	public PagedResult searchArticleByTag(Integer page, Integer pageSize, String searchText,
			String userId);

//	/**
//	 * 通过热度查询评论主
//	 * @param page
//	 * @param pageSize
//	 * @param articleId
//	 * @param userId
//	 * @return
//	 */
//	public PagedResult getMainCommentsByPopilarity(Integer page, Integer pageSize, String articleId, String userId);
//
//	/**
//	 * 通过热度查询次ping
//	 * @param page
//	 * @param pageSize
//	 * @param underCommentId
//	 * @param userId
//	 * @return
//	 */
//	public PagedResult getSonCommentsByPopularity(Integer page, Integer pageSize, String underCommentId, String userId);
	
}
