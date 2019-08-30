package com.nuoquan.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.scripting.xmltags.VarDeclSqlNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;


import com.nuoquan.enums.ArticleStatusEnums;
import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.UserArticleComment;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.service.ArticleService;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.PagedResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value="文章相关接口", tags= {"Article-Controller"})
@RequestMapping("/article")
public class ArticleController extends BasicController{
	
	@Autowired
	private ArticleService articleService;

	@Value("${upload.maxFaceImageSize}")
	private long MAX_FACE_IMAGE_SIZE;
	
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
		
		PagedResult result = articleService.searchYangArticlesContent(article, isSaveRecord, page, PAGE_SIZE);
		return JSONResult.ok(result);
	}
	
	@PostMapping(value="/hot")
	public JSONResult hot() throws Exception {
		return JSONResult.ok(articleService.getHotWords());
	}
	
	@ApiOperation(value="上传文章", notes="上传文章的接口")
	@ApiImplicitParams({
		// uniapp使用formData时，paramType要改成form
		@ApiImplicitParam(name="userId", value="作者id", required=true, dataType="String", paramType="form"),
		@ApiImplicitParam(name="articleTag", value="文章标签", required=false, dataType="String", paramType="form"),
		@ApiImplicitParam(name="articleTitle", value="文章题目", required=true, dataType="String", paramType="form"),
		@ApiImplicitParam(name="articleContent", value="文章内容", required=true, dataType="String", paramType="form")
	})	
	@PostMapping(value="/uploadArticle")
	public JSONResult upload(String userId, String articleTag, String articleTitle, String articleContent) throws Exception {
		
//		if (StringUtils.isBlank(userId) || StringUtils.isEmpty(userId)) {
//			return JSONResult.errorMsg("Id can't be null");
//		}

		// 保存视频信息到数据库
		Article article = new Article();
		article.setArticleTitle(articleTitle);
		article.setArticleContent(articleContent);
		article.setUserId(userId);
		article.setTags(articleTag);
		article.setStatus(ArticleStatusEnums.SUCCESS.value);
		article.setCreateDate(new Date());
		
		String articleId = articleService.saveArticle(article);
		
		return JSONResult.ok(articleId);
	}
	
	@PostMapping(value="/uploadArticleImg")
	public JSONResult uploadArticleImg(String userId ,String articleId, @ApiParam(value="file", required=false) MultipartFile file) throws Exception {

		ArticleImage articleImage = new ArticleImage();
		
		if (file != null) {
			// 判断是否超出大小限制
			if (file.getSize() > MAX_FACE_IMAGE_SIZE) {
				return JSONResult.errorException("Uploaded file size exceed server's limit (10MB)");
			}
			// 保存图片
			String fileSpace = resourceConfig.getFileSpace();	// 文件保存空间地址
			String fileName = file.getOriginalFilename();		// 获取原文件名
			
			// 保存到数据库中的相对路径
			String uploadPathDB = "/" + userId + "/article" + "/" + articleId + fileName;
			// 文件上传的最终保存路径
			String finalVideoPath = "";
			
			if (StringUtils.isNotBlank(fileName)) {
				finalVideoPath = fileSpace + uploadPathDB;
				uploadFile(file, finalVideoPath);	// 调用 BasicController 里的方法
				articleImage.setImagePath(uploadPathDB);
				articleImage.setArticleId(articleId);
			}
			articleService.saveArticleImages(articleImage);
			
		}
		
		return JSONResult.ok();
	}
	
	@PostMapping("/saveComment")
	public JSONResult saveComment(@RequestBody UserArticleComment comment) throws Exception {
		
		articleService.saveComment(comment);
		return JSONResult.ok();
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "articleId", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "page", required = false, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "pageSize", required = false, dataType = "Integer", paramType = "form")
		})
	@PostMapping("/getArticleComments")
	public JSONResult getArticleComments(String articleId, Integer page, Integer pageSize) throws Exception {
		
		if (StringUtils.isBlank(articleId)) {
			return JSONResult.errorMsg("articleId can't be null");
		}
		
		if(page == null) {
			page = 1;
		}
		if(pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		
		PagedResult list = articleService.getAllComments(articleId, page,pageSize);
		
		return JSONResult.ok(list);
	}
	
	@ApiOperation(value="Get the top 3 hot article")
	@PostMapping("/getHotTop3")
	public JSONResult getHotTop3() throws Exception {
		
		List<ArticleVO> list = articleService.getTop3ByPopularity();
		return JSONResult.ok(list);
	}
}
