package com.nuoquan.service;

import java.util.List;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.mapper.ArticleMapper;
import com.nuoquan.mapper.ArticleMapperCustom;
import com.nuoquan.mapper.SearchRecordMapper;
import com.nuoquan.mapper.UserLikeArticleMapper;
import com.nuoquan.mapper.UserMapper;
import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.SearchRecord;
import com.nuoquan.pojo.UserLikeArticle;
import com.nuoquan.pojo.vo.ArticleVO;
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
	
	@Override
	public PagedResult getAllArticles(Integer page, Integer pageSize) {
		
		// 从controller中获取page和pageSize
		PageHelper.startPage(page, pageSize);
		
		List<ArticleVO> list = articleMapperCustom.queryAllArticles();
		
		PageInfo<ArticleVO> pageList = new PageInfo<ArticleVO>();
		
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
		userMapper.addReceiveLikeCount(userId);
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
		userMapper.reduceReceiveLikeCount(userId);
		
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
		
		PageInfo<ArticleVO> pageList = new PageInfo<ArticleVO>();
		
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

}
