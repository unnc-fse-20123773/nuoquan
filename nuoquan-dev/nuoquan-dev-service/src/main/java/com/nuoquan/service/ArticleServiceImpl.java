package com.nuoquan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.mapper.ArticleMapper;
import com.nuoquan.mapper.ArticleMapperCustom;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.utils.PagedResult;


@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleMapper articleMapper;
	
	@Autowired
	private ArticleMapperCustom articleMapperCustom;
	
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

}
