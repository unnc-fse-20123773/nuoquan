package com.nuoquan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nuoquan.pojo.Article;
import com.nuoquan.service.ArticleService;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.PagedResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="文章相关接口", tags= {"操作文章的controller"})
public class ArticleController extends BasicController{
	
	@Autowired
	private ArticleService articleService;
	
	@ApiOperation(value="查询全部文章", notes="查询全部文章的接口")
	@PostMapping("/queryAllArticles")
	public JSONResult showAllArticles(Integer page, Integer pageSize) throws Exception {
		
		if(page == null) {
			page = 1;
		}
		if(pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		
		PagedResult result = articleService.getAllArticles(page,pageSize);
		
		return JSONResult.ok(result);
	}
	
	@PostMapping(value="/userLike")
	public JSONResult userLike(String userId, String articleId, String articleCreaterId) throws Exception {
		
		articleService.userLikeArticle(userId, articleId, articleCreaterId);	
		return JSONResult.ok();
	}
	
	@PostMapping(value="/userUnLike")
	public JSONResult userUnLike(String userId, String articleId, String articleCreaterId) throws Exception {
		articleService.userUnLikeArticle(userId, articleId, articleCreaterId);
		return JSONResult.ok();
	}
	
	/**
	 * 分页和搜索文章 isSaveRecord：1 = 需要保存
	 * 							0/null = 不需要保存
	 * @param article
	 * @param isSaveRecord
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value="/searchArticleYANG")
	public JSONResult searchArticleYang(@RequestBody Article article, Integer isSaveRecord, Integer page) throws Exception {
		
		if (page == null) {
			page = 1;
		}
		
		PagedResult result = articleService.searchYangArticles(article, isSaveRecord, page, PAGE_SIZE);
		return JSONResult.ok(result);
	}
	
	@PostMapping(value="/hot")
	public JSONResult hot() throws Exception {
		return JSONResult.ok(articleService.getHotWords());
	}
	
	
	
}
