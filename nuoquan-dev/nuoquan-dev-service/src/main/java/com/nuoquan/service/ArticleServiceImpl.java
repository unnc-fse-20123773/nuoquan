package com.nuoquan.service;

import java.util.Date;
import java.util.List;

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
import com.nuoquan.mapper.UserMapper;
import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.SearchRecord;
import com.nuoquan.pojo.UserArticleComment;
import com.nuoquan.pojo.UserLikeArticle;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.pojo.vo.UserArticleCommentVO;
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
	private UserMapper userMapper;
	
	@Autowired
	private SearchRecordMapper searchRecordMapper;
	
	@Autowired
	private UserArticleCommentMapper userArticleCommentMapper;
	
	@Autowired
	private UserArticleCommentMapperCustom userArticleCommentMapperCustom;
	
	@Autowired
	private ArticleImageMapper articleImageMapper;
	
	@Override
	public PagedResult getAllArticles(Integer page, Integer pageSize) {
		
		// 从controller中获取page和pageSize
		PageHelper.startPage(page, pageSize);
		
		List<ArticleVO> list = articleMapperCustom.queryAllArticles();
		
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
	public void userLikeArticle(String userId, String articleId, String articleCreaterId) {
		
		//保存用户和文章的点赞关联关系表
		String likeId = sid.nextShort();
		
		UserLikeArticle ula = new UserLikeArticle();
		ula.setId(likeId);
		ula.setUserId(userId);
		ula.setArticleId(articleId);
		
		userLikeArticleMapper.insert(ula);
		
		//文章喜欢数量累加
		articleMapperCustom.addArticleLikeCount(articleId);
		
		//用户受喜欢数量的累加
		userMapper.addReceiveLikeCount(articleCreaterId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userUnLikeArticle(String userId, String articleId, String articleCreaterId) {
		
		//1.删除用户和文章的点赞关联关系表
		Example example = new Example(UserLikeArticle.class);
		//创造条件
		Criteria criteria = example.createCriteria();
		//条件的判断 里面的变量无需和数据库一致，与pojo类中的变量一致。在pojo类中变量与column有映射
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("articleId", articleId);
		
		userLikeArticleMapper.deleteByExample(example);
		
		//2.文章喜欢数量累减
		articleMapperCustom.reduceArticleLikeCount(articleId);
		
		//3.用户受喜欢数量的累减
		userMapper.reduceReceiveLikeCount(articleCreaterId);
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public PagedResult searchYangArticles(Article article, Integer isSaveRecord, Integer page, Integer pageSize) {
		
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
		
		String id = sid.nextShort();
		article.setId(id);
		articleMapper.insertSelective(article);
		
		return id;
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveComment(UserArticleComment comment) {
		
		String id = sid.nextShort();
		comment.setId(id);
		comment.setDislikeNum(0);
		comment.setLikeNum(0);
		comment.setCreateTime(new Date());
		comment.setCommentNum(0);
		userArticleCommentMapper.insert(comment);
		
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllComments(String articleId, Integer page, Integer pageSize) {
		
		PageHelper.startPage(page, pageSize);
		
		List<UserArticleCommentVO> list = userArticleCommentMapperCustom.queryComments(articleId);
			// 对时间格式进行处理
			for (UserArticleCommentVO c : list) {
				String timeAgo = TimeAgoUtils.format(c.getCreateTime());
				c.setTimeAgo(timeAgo);
			}
			
		PageInfo<UserArticleCommentVO> pageList = new PageInfo<>(list);
		
		PagedResult grid = new PagedResult();
		grid.setTotal(pageList.getPages());
		grid.setRows(list);
		grid.setPage(page);
		grid.setRecords(pageList.getTotal());
		
		return grid;
	}

	@Override
	public void saveArticleImages(ArticleImage articleImage) {
		
		String id = sid.nextShort();
		articleImage.setId(id);
		articleImageMapper.insertSelective(articleImage);
	}

	@Override
	public void upadtePopByFunction() {
		articleMapperCustom.upadtePopByFunction();
	}

	@Override
	public List<ArticleVO> getTop3ByPopularity() {
		return articleMapperCustom.getTop3ByPopularity();
	}

}
