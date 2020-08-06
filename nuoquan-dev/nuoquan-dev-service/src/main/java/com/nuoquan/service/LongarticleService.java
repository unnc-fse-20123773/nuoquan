package com.nuoquan.service;

import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.Longarticle;
import com.nuoquan.pojo.LongarticleComment;
import com.nuoquan.pojo.UserArticleComment;
import com.nuoquan.pojo.UserLikeCommentLongarticle;
import com.nuoquan.pojo.UserLikeLongarticle;
import com.nuoquan.pojo.vo.LongarticleVO;
import com.nuoquan.pojo.vo.UserArticleCommentVO;
import com.nuoquan.utils.PagedResult;

public interface LongarticleService {
	
	/**
	 * 分页查询全部长文章
	 */
	public PagedResult list(Integer page, Integer pageSize);
	
	/**
	 * 列出所有等待审核的文章
	 * @return
	 */
	public PagedResult listCheckOnly(Integer page, Integer pageSize);
	
	/**
	 * 按 articleId 获取文章
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public LongarticleVO getById(String articleId, String userId);
	
	/**
	 * @des:用户点赞长文章
	 * @param userId
	 * @param articleId
	 * @param articleCreaterId
	 * @return id in DB
	 */
	public UserLikeLongarticle userLike(String userId, String articleId, String articleCreaterId, Integer signFlag);
	
	/**
	 * @des: 用户取消点赞文章
	 * @param userId
	 * @param articleId
	 * @param articleCreaterId
	 */
	public void userUnLike(String userId, String articleId, String articleCreaterId);
	
	/**
	 * 查看用户是否点赞了文章
	 * @param userId
	 * @param articleId
	 * @return
	 */
	public boolean isUserLike(String userId, String articleId);
	
	/**
	 * @des:用户收藏文章
	 * @param userId
	 * @param articleId
	 * @return id in DB
	 */
//	UserCollectLongarticle 待添加 @Jerrio 8/6/2020
//	public UserCollectLongarticle userCollect(String userId, String articleId, Integer signFlag);
	
	/**
	 * @des: 用户取消收藏文章
	 * @param userId
	 * @param articleId
	 * @param articleCreaterId
	 */
	public void userUncollect(String userId, String articleId);
	
	/**
	 * 查看用户是否收藏了文章
	 * @param userId
	 * @param articleId
	 * @return
	 */
	public boolean isUserCollect(String userId, String articleId);
	
	/**
	 * 查询目标用户收藏的文章
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @param targetId
	 * @return
	 */
	public PagedResult queryCollect(Integer page, Integer pageSize, String userId, String targetId);
	
	/**
	 * 保存文章
	 */
	public String saveArticle(Longarticle longarticle);
	
	/**
	 * 上传评论到数据库
	 * @param comment
	 * @return commentId
	 */
	public String saveComment(LongarticleComment comment);
	
	/**
	 * @des:用户点赞评论
	 * @param userId
	 * @param commentId
	 * @param createrId
	 * @return id in DB
	 */
	public UserLikeCommentLongarticle userLikeComment(String userId, String commentId, String createrId, Integer signFlag);
	
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
	 * 根据公式更新文章热度
	 */
	//暂不需要实现 @Jerrio 8/6/2020
	public void upadtePopByFunction();
	
	/**
	 * 操作者本人查看自己发布过的所有文章
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public PagedResult getAllMyHis(Integer page, Integer pageSize, String userId);

	/**
	 * 操作者查看他人发布过的文章(状态为1的文章)
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @param targetId
	 * @return
	 */
	public PagedResult gerOthersLegalHis(Integer page, Integer pageSize, String userId, String targetId);
	
	/**
	 * 浏览量+1
	 * @param articleId
	 */
	public void addViewCount(String articleId);

	/**
	 * 修改文章状态(可批量)
	 * 0 = unreadable, 1 = readable, 2 = checking
	 */
	public int updateStatus(String articleIds, int status);

	/**
	 * QueryAritcle接口的方法
	 * @param page
	 * @param pageSize
	 * @param queryType
	 * @param userId
	 * @param selectedTag
	 * @return
	 */
	public PagedResult queryArticle(Integer page, Integer pageSize, Integer queryType, String userId, String selectedTag);
}
