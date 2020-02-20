package com.nuoquan.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.enums.StatusEnum;
import com.nuoquan.mapper.ArticleImageMapper;
import com.nuoquan.mapper.ArticleMapper;
import com.nuoquan.mapper.ArticleMapperCustom;
import com.nuoquan.mapper.SearchRecordMapper;
import com.nuoquan.mapper.UserArticleCommentMapper;
import com.nuoquan.mapper.UserArticleCommentMapperCustom;
import com.nuoquan.mapper.UserFansMapper;
import com.nuoquan.mapper.UserLikeArticleMapper;
import com.nuoquan.mapper.UserLikeCommentMapper;
import com.nuoquan.mapper.UserLikeMapperCustom;
import com.nuoquan.mapper.UserMapper;
import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.SearchRecord;
import com.nuoquan.pojo.User;
import com.nuoquan.pojo.UserArticleComment;
import com.nuoquan.pojo.UserFans;
import com.nuoquan.pojo.UserLikeArticle;
import com.nuoquan.pojo.UserLikeComment;
import com.nuoquan.pojo.UserVoteComment;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.pojo.vo.UserArticleCommentVO;
import com.nuoquan.pojo.vo.UserLikeVO;
import com.nuoquan.pojo.vo.VoteVO;
import com.nuoquan.support.Convert;
import com.nuoquan.utils.PageUtils;
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
	
	@Autowired
	private UserFansMapper userFansMapper;

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult list(Integer page, Integer pageSize) {

		// 从controller中获取page和pageSize (经实验，PageHelper 只拦截下一次查询)
		PageHelper.startPage(page, pageSize);

		List<ArticleVO> list = articleMapperCustom.list();
		for (ArticleVO a : list) {
			// 为每个文章添加图片列表
			a.setImgList(articleImageMapper.getArticleImgs(a.getId()));
			// 添加标签list
			if (!StringUtils.isBlank(a.getTags())) {
				String[] tagList = a.getTags().split("#");
				List<String> finalTagList = new ArrayList<String>();
				for (String tag : tagList) {
					if (!StringUtils.isBlank(tag)) {
						finalTagList.add(tag);
					}
				}
				a.setTagList(finalTagList);
			}
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
	public PagedResult listCheckOnly(Integer page, Integer pageSize) {

		// 从controller中获取page和pageSize (经实验，PageHelper 只拦截下一次查询)
		PageHelper.startPage(page, pageSize);

		List<ArticleVO> list = articleMapperCustom.listCheckOnly();
		for (ArticleVO a : list) {
			// 为每个文章添加图片列表
			a.setImgList(articleImageMapper.getArticleImgs(a.getId()));
			// 添加标签list
			if (!StringUtils.isBlank(a.getTags())) {
				String[] tagList = a.getTags().split("#");
				List<String> finalTagList = new ArrayList<String>();
				for (String tag : tagList) {
					if (!StringUtils.isBlank(tag)) {
						finalTagList.add(tag);
					}
				}
				a.setTagList(finalTagList);
			}
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
	public PagedResult getAllArticles(Integer page, Integer pageSize, String userId) {

		// 从controller中获取page和pageSize (经实验，PageHelper 只拦截下一次查询)
		PageHelper.startPage(page, pageSize);
		List<ArticleVO> list = articleMapperCustom.queryAllArticles();
		for (ArticleVO a : list) {
			// 为每个文章添加图片列表
			a.setImgList(articleImageMapper.getArticleImgs(a.getId()));
			// 添加和关于用户的点赞关系
			a.setIsLike(isUserLikeArticle(userId, a.getId()));
			// 添加标签list
			if (!StringUtils.isBlank(a.getTags())) {
				String[] tagList = a.getTags().split("#");
				List<String> finalTagList = new ArrayList<String>();
				for (String tag : tagList) {
					if (!StringUtils.isBlank(tag)) {
						finalTagList.add(tag);
					}
				}
				a.setTagList(finalTagList);
			}
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
	public ArticleVO getArticleById(String articleId, String userId) {
		ArticleVO articleVO = articleMapperCustom.getArticleById(articleId);
		List<ArticleImage> images = articleImageMapper.getArticleImgs(articleId);
		if (!images.isEmpty()) {
			// 添加图片列表
			articleVO.setImgList(images);
			// 添加和关于用户的点赞关系
			articleVO.setIsLike(isUserLikeArticle(userId, articleVO.getId()));
			// 添加标签列表
			if (!StringUtils.isBlank(articleVO.getTags())) {
				String[] tagList = articleVO.getTags().split("#");
				List<String> finalTagList = new ArrayList<String>();
				for (String tag : tagList) {
					if (!StringUtils.isBlank(tag)) {
						finalTagList.add(tag);
					}
				}
				articleVO.setTagList(finalTagList);
			}
		}
		return articleVO;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public UserLikeArticle userLikeArticle(String userId, String articleId, String articleCreaterId, Integer signFlag) {

		// 保存用户和文章的点赞关联关系表
		String likeId = sid.nextShort();

		UserLikeArticle ula = new UserLikeArticle();
		ula.setId(likeId);
		ula.setUserId(userId);
		ula.setArticleId(articleId);
		ula.setSignFlag(signFlag);
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
	public PagedResult searchArticleYang(Integer isSaveRecord, Integer page, Integer pageSize,
			String searchText, String userId) {
		
		String[] texts = searchText.split(" ");
		// 保存热搜词
		if (isSaveRecord != null && isSaveRecord == 1) {
			for (String text : texts) {
				SearchRecord record = new SearchRecord();
				String recordId = sid.nextShort();
				record.setId(recordId);
				record.setContent(text);
				searchRecordMapper.insert(record);
			}
		}
		
		// 开启分页查询并转换为vo对象
		// 在Example中的每一个Criteria相当于一个括号，把里面的内容当成一个整体
		Example articleExample = new Example(Article.class);
		articleExample.setOrderByClause("create_date desc");
		Criteria criteria = articleExample.createCriteria();
		for(String text : texts) {
			criteria.orLike("articleTitle", "%" + text + "%");
			criteria.orLike("articleContent", "%" + text + "%");
			criteria.orLike("tags", "%" + text + "%");
		}
		
		Criteria criteria2 = articleExample.createCriteria();
		criteria2.andEqualTo("status", StatusEnum.READABLE.type);
		articleExample.and(criteria2);
		
		PageHelper.startPage(page, pageSize);
		List<Article> list = articleMapperCustom.selectByExample(articleExample);
		PageInfo<Article> pageInfo = new PageInfo<>(list);
		PageInfo<ArticleVO> pageInfoVo = PageUtils.PageInfo2PageInfoVo(pageInfo);
		
		List<ArticleVO> listVo = new ArrayList<>();
		for (Article a : list) {
			ArticleVO av = new ArticleVO();
			BeanUtils.copyProperties(a, av); //转换对象
			// 添加作者信息
			User user= userMapper.selectByPrimaryKey(av.getUserId());
			if (user!=null) {
				av.setNickname(user.getNickname());
				av.setFaceImg(user.getFaceImg());
				av.setFaceImgThumb(user.getFaceImgThumb());
			}
			// 为每个文章添加图片列表
			av.setImgList(articleImageMapper.getArticleImgs(av.getId()));
			// 添加和关于用户的点赞关系
			av.setIsLike(isUserLikeArticle(userId, av.getId()));
			// 添加标签list
			String[] tagList = av.getTags().split("#");
			List<String> finalTagList = new ArrayList<String>();
			for (String tag : tagList) {
				if (!StringUtils.isBlank(tag)) {
					finalTagList.add(tag);
				}
			}
			av.setTagList(finalTagList);
			listVo.add(av);
		}
		pageInfoVo.setList(listVo);
		
		//为最终返回对象 pagedResult 添加属性
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(pageInfoVo.getPageNum());
		pagedResult.setTotal(pageInfoVo.getPages());
		pagedResult.setRows(pageInfoVo.getList());
		pagedResult.setRecords(pageInfoVo.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<String> getHotWords() {

		return searchRecordMapper.getHotWords();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String saveArticle(Article article) {
		String id = sid.nextShort();
		article.setId(id);
		articleMapper.insertSelective(article);

		return id;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void deleteArticle(String articleId, String userId) {
		// 1. 删除文章图片数据库路径
		// 1.1 删除实际图片
		// 2. 删除文章评论
		// 2.1 删除文章评论的点赞
		// 3. 删除文章的点赞
		// 4. 删除文章

		// 删除文章图片
		String fileSpace = "/Users/xudeyan/Desktop/JumboX/nuoquanTmp";
		String uploadDB = "/" + userId + "/article" + "/" + articleId;
		String path = fileSpace + uploadDB;
		String path1 = "/Users/xudeyan/Desktop/JUMBOX/nuoquanTmp/oDwsO5Btm6HsoGie6E8qB7WN9aYQ/article/191003BSB10GYWM8/0.jpg";
		File file = new File(path1);
		deletefile(file);

		// 删除数据库中的路径
		Example exampleDelImg = new Example(ArticleImage.class);
		Criteria criteria1 = exampleDelImg.createCriteria();
		criteria1.andEqualTo("articleId", articleId);
		articleImageMapper.deleteByExample(exampleDelImg);

//		System.out.println(articleId);

		// 删除文章评论的点赞
		// 先在userArticleComment里找到articleId对应的评论id, 该id为userLikeComment中的commentId
		Example exampleToFindCommentId = new Example(UserArticleComment.class);
		Criteria criteriaToFindCommentId = exampleToFindCommentId.createCriteria();
		criteriaToFindCommentId.andEqualTo("articleId", articleId);
		List<UserArticleComment> list = userArticleCommentMapper.selectByExample(exampleToFindCommentId);
		for (UserArticleComment c : list) {
			// 得到commentId
			String commentId = c.getId();
			// 在userLikeComment中查询
			Example exampleToDelCommentLikeExample = new Example(UserLikeComment.class);
			Criteria criteria = exampleToDelCommentLikeExample.createCriteria();
			criteria.andEqualTo("commentId", commentId);
			userLikeCommentMapper.deleteByExample(exampleToDelCommentLikeExample);
		}
		// 删除目标文章所有评论
		Example exampleDelComment = new Example(UserArticleComment.class);
		Criteria criteria2 = exampleDelComment.createCriteria();
		criteria2.andEqualTo("articleId", articleId);
		userArticleCommentMapper.deleteByExample(exampleToFindCommentId);

		// 删除文章的点赞
		Example exampleDelArticleLike = new Example(UserLikeArticle.class);
		Criteria criteria4 = exampleDelArticleLike.createCriteria();
		criteria4.andEqualTo("articleId", articleId);
		userLikeArticleMapper.deleteByExample(exampleDelArticleLike);

		// 删除文章
		Example exampleDelArticle = new Example(Article.class);
		Criteria criteria3 = exampleDelArticle.createCriteria();
		criteria3.andEqualTo("id", articleId);
		articleMapper.deleteByExample(exampleDelArticle);
	}

//	private static void deletefile(File file) {
//		// TODO Auto-generated method stub
//		if(file.isDirectory()) {
//			File[] files = file.listFiles();
//			for (File key : files) {
//				if (key.isFile()) {
//					key.delete();
//				} else {
//					deletefile(key);
//				}
//			}
//		}
//		file.delete();
//	}

	private void deletefile(File file) {

		System.out.println(file.getAbsolutePath());
		if (file.isFile()) {
			// 判断是否为文件--Y
//			System.out.println(file.getAbsolutePath());
//			file.setExecutable(true);
			file.setWritable(true);
//			file.exists();
//			file.canExecute();
//			file.canWrite();
//			file.getParent();
			boolean t = file.delete();
			System.out.println(t);
			System.out.println("exist? " + file.exists());
			System.out.println("canExecute? " + file.canExecute());
			System.out.println("canWrite? " + file.canWrite());
			System.out.println("parent is " + file.getParent());
		} else {
			String[] childFilePathStrings = file.list();

			System.out.println(childFilePathStrings[1]);
			System.out.println(childFilePathStrings.length);

			for (String path : childFilePathStrings) {
				System.out.println(path);
				File childFile = new File(file.getAbsoluteFile() + "/" + path);
				deletefile(childFile);
			}
			System.out.println(file.getAbsolutePath());
			file.delete();
		}
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
		// 文章评论数累加
		articleMapperCustom.addArticleCommentCount(comment.getArticleId());
		if (!StringUtils.isBlank(comment.getUnderCommentId())) {
			// 主评论的评论数累加
			userArticleCommentMapperCustom.addCommentCount(comment.getUnderCommentId());
		}

		return id;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public UserLikeComment userLikeComment(String userId, String commentId, String createrId, Integer signFlag) {
		// 保存用户和文章的点赞关联关系表
		String likeId = sid.nextShort();

		UserLikeComment ulc = new UserLikeComment();
		ulc.setId(likeId);
		ulc.setUserId(userId);
		ulc.setCommentId(commentId);
		ulc.setSignFlag(signFlag);
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
		if (isLike) {
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
	public PagedResult getMainComments(Integer page, Integer pageSize, Integer type, String articleId, String userId) {

		PageHelper.startPage(page, pageSize);
		List<UserArticleCommentVO> list = null;
		if (type == 0) {
			list = userArticleCommentMapperCustom.queryComments(articleId);
			for (UserArticleCommentVO c : list) {
				// 对时间格式进行处理
//				String timeAgo = TimeAgoUtils.format(c.getCreateDate());
//				c.setTimeAgo(timeAgo);
				// 查询并设置关于用户的点赞关系
				c.setIsLike(isUserLikeComment(userId, c.getId()));
			}
		}
		
		if (type == 1) {
			list = userArticleCommentMapperCustom.queryCommentsByPopularity(articleId);
			for (UserArticleCommentVO c : list) {
				// 对时间格式进行处理
//				String timeAgo = TimeAgoUtils.format(c.getCreateDate());
//				c.setTimeAgo(timeAgo);
				// 查询并设置关于用户的点赞关系
				c.setIsLike(isUserLikeComment(userId, c.getId()));
			}
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
	public PagedResult getSonComments(Integer page, Integer pageSize, Integer type, String underCommentId, String userId) {

		PageHelper.startPage(page, pageSize);
		List<UserArticleCommentVO> list = null;
		if (type == 0) {
			list = userArticleCommentMapperCustom.querySonComments(underCommentId);

			for (UserArticleCommentVO c : list) {
//				String timeAgo = TimeAgoUtils.format(c.getCreateDate());
//				c.setTimeAgo(timeAgo);
				// 查询并设置关于用户的点赞关系
				c.setIsLike(isUserLikeComment(userId, c.getId()));
				// 设置回复人昵称
				c.setToNickname(userService.queryUserById(c.getToUserId()).getNickname());
			}
		}
		
		if (type == 1) {
			list = userArticleCommentMapperCustom.querySonCommentsByPopularity(underCommentId);

			for (UserArticleCommentVO c : list) {
//				String timeAgo = TimeAgoUtils.format(c.getCreateDate());
//				c.setTimeAgo(timeAgo);
				// 查询并设置关于用户的点赞关系
				c.setIsLike(isUserLikeComment(userId, c.getId()));
				// 设置回复人昵称
				c.setToNickname(userService.queryUserById(c.getToUserId()).getNickname());
			}
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
	public UserArticleCommentVO getCommentById(String commentId, String userId) {
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
	public PagedResult getTop3ByPopularity(Integer page, Integer pageSize, String userId) {
		PageHelper.startPage(page, pageSize);
		List<ArticleVO> list = articleMapperCustom.getTop3ByPopularity();
		for (ArticleVO a : list) {
			// 为每个文章添加图片列表
			a.setImgList(articleImageMapper.getArticleImgs(a.getId()));
			// 添加和关于用户的点赞关系
			a.setIsLike(isUserLikeArticle(userId, a.getId()));
			// 添加标签list
			if (!StringUtils.isBlank(a.getTags())) {
				String[] tagList = a.getTags().split("#");
				List<String> finalTagList = new ArrayList<String>();
				for (String tag : tagList) {
					if (!StringUtils.isBlank(tag)) {
						finalTagList.add(tag);
					}
				}
				a.setTagList(finalTagList);
			}
		}
		PageInfo<ArticleVO> pageList = new PageInfo<>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());
		
		return pagedResult;
		
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

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<UserArticleCommentVO> getUnsignedCommentMsg(String userId) {
		List<UserArticleCommentVO> commentVOs = userArticleCommentMapperCustom.getUnsignedCommentMsg(userId);
		return commentVOs;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void fDeleteArticle(String articleId) {
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", articleId);
		Article a = new Article();
		a.setStatus(StatusEnum.UNREADABLE.type);
		articleMapper.updateByExampleSelective(a, example);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void banArticle(String articleId) {
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", articleId);
		Article a = new Article();
		a.setStatus(StatusEnum.BANNED.type);
		articleMapper.updateByExampleSelective(a, example);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void passArticle(String articleId) {
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", articleId);
		Article a = new Article();
		a.setStatus(StatusEnum.READABLE.type);
		articleMapper.updateByExampleSelective(a, example);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void banComment(String commentId) {
		Example example = new Example(UserArticleComment.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", commentId);
		UserArticleComment commentHelper = new UserArticleComment();
		commentHelper.setStatus(StatusEnum.BANNED.type);
		userArticleCommentMapper.updateByExampleSelective(commentHelper, example);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void passComment(String commentId) {
		Example example = new Example(UserArticleComment.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", commentId);
		UserArticleComment commentHelper = new UserArticleComment();
		commentHelper.setStatus(StatusEnum.READABLE.type);
		userArticleCommentMapper.updateByExampleSelective(commentHelper, example);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllMyHisArticle(Integer page, Integer pageSize, String userId) {

		PageHelper.startPage(page, pageSize);

		List<ArticleVO> list = articleMapperCustom.queryAllMyHisArticle(userId);
		for (ArticleVO a : list) {
			// 为每篇文章添加图片列表
			a.setImgList(articleImageMapper.getArticleImgs(a.getId()));
			// 添加和关于用户的点赞关系
			a.setIsLike(isUserLikeArticle(userId, a.getId()));
			// 添加标签列表
			if (!StringUtils.isBlank(a.getTags())) {
				String[] tagList = a.getTags().split("#");
				List<String> finalTagList = new ArrayList<String>();
				for (String tag : tagList) {
					if (!StringUtils.isBlank(tag)) {
						finalTagList.add(tag);
					}
				}
				a.setTagList(finalTagList);
			}
		}
		System.out.print(list.size());
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
	public PagedResult gerOtherslegalHisArticle(Integer page, Integer pageSize, String userId, String targetId) {

		PageHelper.startPage(page, pageSize);
		List<ArticleVO> list = articleMapperCustom.queryOthersLegalHisArticle(targetId);
		for (ArticleVO a : list) {
			// 为每篇文章添加图片列表
			a.setImgList(articleImageMapper.getArticleImgs(a.getId()));
			// 添加和关于用户的点赞关系
			a.setIsLike(isUserLikeArticle(targetId, a.getId()));
			// 添加标签列表
			if (!StringUtils.isBlank(a.getTags())) {
				String[] tagList = a.getTags().split("#");
				List<String> finalTagList = new ArrayList<String>();
				for (String tag : tagList) {
					if (!StringUtils.isBlank(tag)) {
						finalTagList.add(tag);
					}
				}
				a.setTagList(finalTagList);
			}
		}
		PageInfo<ArticleVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void addViewCount(String articleId) {
		articleMapperCustom.addViewCount(articleId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public int updateArticleStatus(String articleIds, int status) {
		if (StringUtils.isEmpty(articleIds)) {
			return -1;
		}

		List<String> listId = Convert.toListStrArray(articleIds);

		Example example = new Example(Article.class);
		example.createCriteria().andIn("id", listId);
		Article a = new Article();
		a.setStatus(status);
		return articleMapper.updateByExampleSelective(a, example);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllSubscribedAuthorArticles(Integer page, Integer pageSize, String userId) {
		// 查询全部我(操作者)关注的用户
		Example example = new Example(UserFans.class);
		Criteria criteria = example.createCriteria();
		// 此处userId为操作者id
		criteria.andEqualTo("fansId", userId);
		List<UserFans> userList = userFansMapper.selectByExample(example);
		
		Example example2 = new Example(Article.class);
		example2.setOrderByClause("create_date desc");
		Criteria criteria2 = example2.createCriteria();
		for (UserFans userFans : userList) {
//			System.out.println(userFans.getUserId());
			criteria2.orEqualTo("userId", userFans.getUserId());
		}

		List<Article> list = articleMapper.selectByExample(example2);
		PageInfo<Article> pageInfo = new PageInfo<>(list);
		PageInfo<ArticleVO> pageInfoVO = PageUtils.PageInfo2PageInfoVo(pageInfo);
		
		List<ArticleVO> listVO = new ArrayList<>();
		for (Article a : list) {
			ArticleVO av = new ArticleVO();
			BeanUtils.copyProperties(a, av); //转换对象
			// 添加作者信息
			User user= userMapper.selectByPrimaryKey(av.getUserId());
			if (user!=null) {
				av.setNickname(user.getNickname());
				av.setFaceImg(user.getFaceImg());
				av.setFaceImgThumb(user.getFaceImgThumb());
			}
			// 为每个文章添加图片列表
			av.setImgList(articleImageMapper.getArticleImgs(av.getId()));
			// 添加和关于用户的点赞关系
			av.setIsLike(isUserLikeArticle(userId, av.getId()));
			// 添加标签list
			String[] tagList = av.getTags().split("#");
			List<String> finalTagList = new ArrayList<String>();
			for (String tag : tagList) {
				if (!StringUtils.isBlank(tag)) {
					finalTagList.add(tag);
				}
			}
			av.setTagList(finalTagList);
			
			listVO.add(av);
		}
		pageInfoVO.setList(listVO);
		System.out.println("1");
		
		//为最终返回对象 pagedResult 添加属性
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(pageInfoVO.getPageNum());
		pagedResult.setTotal(pageInfoVO.getPages());
		pagedResult.setRows(pageInfoVO.getList());
		pagedResult.setRecords(pageInfoVO.getTotal());

		return pagedResult;
	}
}


//
//@Transactional(propagation = Propagation.REQUIRED)
//@Override
//public void passComment(String commentId) {
//	Example example = new Example(UserVoteComment.class);
//	Criteria criteria = example.createCriteria();
//	criteria.andEqualTo("id", commentId);
//	
//	UserVoteComment userVoteCommentHelper = new UserVoteComment();
//	userVoteCommentHelper.setStatus(StatusEnum.READABLE.type);
//	userVoteCommentMapper.updateByExampleSelective(userVoteCommentHelper, example);
