package com.nuoquan.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.utils.TimeAgoUtils;
import com.nuoquan.mapper.ArticleImageMapper;
import com.nuoquan.mapper.ArticleMapper;
import com.nuoquan.mapper.ArticleMapperCustom;
import com.nuoquan.mapper.SearchRecordMapper;
import com.nuoquan.mapper.UserArticleCommentMapper;
import com.nuoquan.mapper.UserArticleCommentMapperCustom;
import com.nuoquan.mapper.UserLikeArticleMapper;
import com.nuoquan.mapper.UserLikeCommentMapper;
import com.nuoquan.mapper.UserLikeMapperCustom;
import com.nuoquan.mapper.UserMapper;
import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.SearchRecord;
import com.nuoquan.pojo.UserArticleComment;
import com.nuoquan.pojo.UserLikeArticle;
import com.nuoquan.pojo.UserLikeComment;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.pojo.vo.UserArticleCommentVO;
import com.nuoquan.pojo.vo.UserLikeVO;
import com.nuoquan.utils.PagedResult;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private Sid sid;

	@Autowired
	private ArticleMapper articleMapper;

	@Autowired
	private ArticleMapperCustom articleMapperCustom;

	@Autowired
	private UserLikeArticleMapper userLikeArticleMapper;

	@Autowired
	private UserLikeCommentMapper userLikeCommentMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private SearchRecordMapper searchRecordMapper;

	@Autowired
	private UserArticleCommentMapper userArticleCommentMapper;

	@Autowired
	private UserArticleCommentMapperCustom userArticleCommentMapperCustom;

	@Autowired
	private ArticleImageMapper articleImageMapper;
	
	@Autowired
	private UserLikeMapperCustom userLikeMapperCustom;

	@Autowired
	private UserService userService;
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllArticles(Integer page, Integer pageSize, String userId) {

		// 从controller中获取page和pageSize (经实验，PageHelper 只拦截下一次查询)
		PageHelper.startPage(page, pageSize);

		List<ArticleVO> list = articleMapperCustom.queryAllArticles();
		// 为每个文章添加图片列表，和关于用户的点赞关系
		for (ArticleVO a : list) {
			a.setImgList(articleImageMapper.getArticleImgs(a.getId()));
			a.setIsLike(isUserLikeArticle(userId, a.getId()));
		}
		
		PageInfo<ArticleVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public ArticleVO getArticleById(String articleId) {
		ArticleVO articleVO = articleMapperCustom.getArticleById(articleId);
		List<ArticleImage> images = articleImageMapper.getArticleImgs(articleId);
		if (!images.isEmpty()) {
			articleVO.setImgList(images);
		}
		return articleVO;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public UserLikeArticle userLikeArticle(String userId, String articleId, String articleCreaterId) {

		// 保存用户和文章的点赞关联关系表
		String likeId = sid.nextShort();

		UserLikeArticle ula = new UserLikeArticle();
		ula.setId(likeId);
		ula.setUserId(userId);
		ula.setArticleId(articleId);
		ula.setCreateDate(new Date());

		userLikeArticleMapper.insertSelective(ula);
		// 文章喜欢数量累加
		articleMapperCustom.addArticleLikeCount(articleId);
		// 用户受喜欢数量的累加
		userMapper.addReceiveLikeCount(articleCreaterId);
		
		return ula;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userUnLikeArticle(String userId, String articleId, String articleCreaterId) {
		boolean isLike = isUserLikeArticle(userId, articleId);
		if (isLike) {
			// 1.删除用户和文章的点赞关联关系表
			Example example = new Example(UserLikeArticle.class);
			// 创造条件
			Criteria criteria = example.createCriteria();
			// 条件的判断 里面的变量无需和数据库一致，与pojo类中的变量一致。在pojo类中变量与column有映射
			criteria.andEqualTo("userId", userId);
			criteria.andEqualTo("articleId", articleId);

			userLikeArticleMapper.deleteByExample(example);

			// 2.文章喜欢数量累减
			articleMapperCustom.reduceArticleLikeCount(articleId);

			// 3.用户受喜欢数量的累减
			userMapper.reduceReceiveLikeCount(articleCreaterId);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public PagedResult searchYangArticlesContent(Article article, Integer isSaveRecord, Integer page, Integer pageSize) {
		
		// 保存热搜词
		String articleContent = article.getArticleContent();
		if (isSaveRecord != null && isSaveRecord == 1) {
			SearchRecord record = new SearchRecord();
			String recordId = sid.nextShort();
			record.setId(recordId);
			record.setContent(articleContent);
			searchRecordMapper.insert(record);
		}

		PageHelper.startPage(page, pageSize);
		List<ArticleVO> list = articleMapperCustom.searchArticleContentYang(articleContent);

		PageInfo<ArticleVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<String> getHotWords() {

		return searchRecordMapper.getHotWords();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String saveArticle(Article article) {
		String[] test = article.getArticleContent().split("\\n");
		for(String s : test) {
			System.out.println(s);
		}
		
		
		String id = sid.nextShort();
		article.setId(id);
		articleMapper.insertSelective(article);

		return id;

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String saveComment(UserArticleComment comment) {

		String id = sid.nextShort();
		comment.setId(id);
		comment.setDislikeNum(0);
		comment.setLikeNum(0);
		comment.setCreateDate(new Date());
		comment.setCommentNum(0);
		userArticleCommentMapper.insertSelective(comment);
		//文章评论数累加
		articleMapperCustom.addArticleCommentCount(comment.getArticleId());
		if (!StringUtils.isBlank(comment.getUnderCommentId())) {
			//主评论的评论数累加
			userArticleCommentMapperCustom.addCommentCount(comment.getUnderCommentId());
		}
		
		return id;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public UserLikeComment userLikeComment(String userId, String commentId, String createrId) {
		// 保存用户和文章的点赞关联关系表
		String likeId = sid.nextShort();

		UserLikeComment ulc = new UserLikeComment();
		ulc.setId(likeId);
		ulc.setUserId(userId);
		ulc.setCommentId(commentId);
		ulc.setCreateDate(new Date());
		
		userLikeCommentMapper.insertSelective(ulc);
		// 评论喜欢数量累加
		userArticleCommentMapperCustom.addCommentLikeCount(commentId);
		// 用户受喜欢数量的累加
		userMapper.addReceiveLikeCount(createrId);

		return ulc;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userUnLikeComment(String userId, String commentId, String createrId) {
		boolean isLike = isUserLikeComment(userId, commentId);
		if(isLike) {
			// 1.删除用户和文章的点赞关联关系表
			Example example = new Example(UserLikeComment.class);
			// 创造条件
			Criteria criteria = example.createCriteria();
			// 条件的判断 里面的变量无需和数据库一致，与pojo类中的变量一致。在pojo类中变量与column有映射
			criteria.andEqualTo("userId", userId);
			criteria.andEqualTo("commentId", commentId);

			userLikeCommentMapper.deleteByExample(example);

			// 2.文章喜欢数量累减
			userArticleCommentMapperCustom.reduceCommentLikeCount(commentId);

			// 3.用户受喜欢数量的累减
			userMapper.reduceReceiveLikeCount(createrId);
		}
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean isUserLikeArticle(String userId, String articleId) {
		Example example = new Example(UserLikeArticle.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("articleId", articleId);
		
		List<UserLikeArticle> list = userLikeArticleMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			return true;
		}
		return false;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean isUserLikeComment(String userId, String commentId) {
		Example example = new Example(UserLikeComment.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("commentId", commentId);
		
		List<UserLikeComment> list = userLikeCommentMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			return true;
		}
		return false;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getMainComments(Integer page, Integer pageSize, String articleId, String userId) {

		PageHelper.startPage(page, pageSize);

		List<UserArticleCommentVO> list = userArticleCommentMapperCustom.queryComments(articleId);
		for (UserArticleCommentVO c : list) {
			// 对时间格式进行处理
			String timeAgo = TimeAgoUtils.format(c.getCreateDate());
			c.setTimeAgo(timeAgo);
			// 查询并设置关于用户的点赞关系
			c.setIsLike(isUserLikeComment(userId, c.getId()));
		}

		PageInfo<UserArticleCommentVO> pageList = new PageInfo<>(list);

		PagedResult grid = new PagedResult();
		grid.setTotal(pageList.getPages());
		grid.setRows(list);
		grid.setPage(page);
		grid.setRecords(pageList.getTotal());

		return grid;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getSonComments(Integer page, Integer pageSize, String underCommentId, String userId) {
		
		PageHelper.startPage(page, pageSize);
		List<UserArticleCommentVO> list = userArticleCommentMapperCustom.querySonComments(underCommentId);
		
		for (UserArticleCommentVO c : list) {
			String timeAgo = TimeAgoUtils.format(c.getCreateDate());
			c.setTimeAgo(timeAgo);
			// 查询并设置关于用户的点赞关系
			c.setIsLike(isUserLikeComment(userId, c.getId()));
			// 设置回复人昵称
			c.setToNickname(userService.queryUserById(c.getToUserId()).getNickname());
		}
		
		PageInfo<UserArticleCommentVO> pageList = new PageInfo<>(list);

		PagedResult grid = new PagedResult();
		grid.setTotal(pageList.getPages());
		grid.setRows(list);
		grid.setPage(page);
		grid.setRecords(pageList.getTotal());

		return grid;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public UserArticleCommentVO getCommentById(String commentId) {
		return userArticleCommentMapperCustom.getCommentById(commentId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveArticleImages(ArticleImage articleImage) {

		String id = sid.nextShort();
		articleImage.setId(id);
		articleImageMapper.insertSelective(articleImage);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void upadtePopByFunction() {
		articleMapperCustom.upadtePopByFunction();
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<ArticleVO> getTop3ByPopularity() {
		return articleMapperCustom.getTop3ByPopularity();
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateLikeArticleSigned(List<String> msgIdList) {
		userLikeArticleMapper.batchUpdateMsgSigned(msgIdList);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateLikeCommentSigned(List<String> msgIdList) {
		userLikeCommentMapper.batchUpdateMsgSigned(msgIdList);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateCommentSigned(List<String> msgIdList) {
		userArticleCommentMapperCustom.batchUpdateMsgSigned(msgIdList);		
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<UserLikeVO> getUnsignedLikeMsg(String userId) {
		List<UserLikeVO> userLikeVOs = userLikeMapperCustom.getUnsignedLikeMsg(userId);
		return userLikeVOs;
	}

	@Override
	public List<UserArticleCommentVO> getUnsignedCommentMsg(String userId) {
		List<UserArticleCommentVO> commentVOs = userArticleCommentMapperCustom.getUnsignedCommentMsg(userId);
		return commentVOs;
	}

}
