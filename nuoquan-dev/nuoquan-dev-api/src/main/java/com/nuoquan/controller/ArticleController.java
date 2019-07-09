package com.nuoquan.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.util.StringUtil;
import com.nuoquan.pojo.User;
import com.nuoquan.service.ArticleService;
import com.nuoquan.service.UserService;
import com.nuoquan.utils.IMoocJSONResult;
import com.nuoquan.utils.MD5Utils;
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
	public IMoocJSONResult showAllArticles(Integer page, Integer pageSize) throws Exception {
		
		if(page == null) {
			page = 1;
		}
		if(pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		
		PagedResult result = articleService.getAllArticles(page,pageSize);
		
		return IMoocJSONResult.ok(result);
	}
}
