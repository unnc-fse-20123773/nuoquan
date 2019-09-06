package com.nuoquan.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nuoquan.enums.ArticleStatusEnums;
import com.nuoquan.enums.MsgActionEnum;
import com.nuoquan.netty.MsgHandler;
import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.UserArticleComment;
import com.nuoquan.pojo.netty.CommentCard;
import com.nuoquan.pojo.netty.DataContent;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.pojo.vo.UserArticleCommentVO;
import com.nuoquan.service.ArticleService;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.PagedResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "文章相关接口", tags = { "Article-Controller" })
@RequestMapping("/article")
public class ArticleController extends BasicController {

	@Autowired
	private ArticleService articleService;

	@Value("${upload.maxFaceImageSize}")
	private long MAX_FACE_IMAGE_SIZE;

	@ApiOperation(value = "查询全部文章", notes = "查询全部文章的接口")
	@PostMapping("/queryAllArticles")
	public JSONResult showAllArticles(Integer page, Integer pageSize) throws Exception {

		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		PagedResult result = articleService.getAllArticles(page, pageSize);

		return JSONResult.ok(result);
	}

	@ApiOperation(value = "点赞文章")
	@ApiImplicitParams({
			// uniapp使用formData时，paramType要改成form
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "articleCreaterId", value = "文章作者id", required = true, dataType = "String", paramType = "form") })
	@PostMapping(value = "/userLikeArticle")
	public JSONResult userLike(String userId, String articleId, String articleCreaterId) throws Exception {

		// 储存到数据库 返回数据库对象
		articleService.userLikeArticle(userId, articleId, articleCreaterId);
		// 给作者发推送
		DataContent dataContent = new DataContent();
		dataContent.setAction(MsgActionEnum.LIKEARTICLE.type);
		dataContent.setData(articleService.getArticleById(articleId));

		MsgHandler.sendMsgTo(articleCreaterId, dataContent);
		return JSONResult.ok();
	}

	@ApiOperation(value = "点赞评论")
	@ApiImplicitParams({
			// uniapp使用formData时，paramType要改成form
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "createrId", value = "作者id", required = true, dataType = "String", paramType = "form") })
	@PostMapping(value = "/userLikeComment")
	public JSONResult userLikeComment(String userId, String commentId, String createrId) throws Exception {

		// 储存到数据库 返回数据库对象
		articleService.userLikeComment(userId, commentId, createrId);
		// 给作者发推送
		DataContent dataContent = new DataContent();
		dataContent.setAction(MsgActionEnum.LIKECOMMENT.type);
		dataContent.setData(articleService.getCommentById(commentId));

		MsgHandler.sendMsgTo(createrId, dataContent);
		return JSONResult.ok();
	}

	@ApiOperation(value = "取消点赞文章")
	@ApiImplicitParams({
			// uniapp使用formData时，paramType要改成form
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "articleCreaterId", value = "文章作者id", required = true, dataType = "String", paramType = "form") })
	@PostMapping(value = "/userUnLikeArticle")
	public JSONResult userUnLikeArticle(String userId, String articleId, String articleCreaterId) throws Exception {
		articleService.userUnLikeArticle(userId, articleId, articleCreaterId);
		return JSONResult.ok();
	}

	@ApiOperation(value = "取消点赞评论")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "createrId", value = "作者id", required = true, dataType = "String", paramType = "form") })
	@PostMapping(value = "/userUnLikeComment")
	public JSONResult userUnLikeComment(String userId, String commentId, String createrId) throws Exception {
		articleService.userUnLikeComment(userId, commentId, createrId);
		return JSONResult.ok();
	}

	/**
	 * 分页和搜索文章 isSaveRecord：1 = 需要保存 0/null = 不需要保存
	 * 
	 * @param article
	 * @param isSaveRecord
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/searchArticleYANG")
	public JSONResult searchArticleYang(@RequestBody Article article, Integer isSaveRecord, Integer page)
			throws Exception {

		if (page == null) {
			page = 1;
		}
		
		PagedResult result = articleService.searchYangArticlesContent(article, isSaveRecord, page, PAGE_SIZE);
		return JSONResult.ok(result);
	}

	@PostMapping(value = "/hot")
	public JSONResult hot() throws Exception {
		return JSONResult.ok(articleService.getHotWords());
	}

	@ApiOperation(value = "上传文章", notes = "上传文章的接口")
	@ApiImplicitParams({
		// uniapp使用formData时，paramType要改成form
		@ApiImplicitParam(name="userId", value="作者id", required=true, dataType="String", paramType="form"),
		@ApiImplicitParam(name="articleTag", value="文章标签", required=false, dataType="String", paramType="form"),
		@ApiImplicitParam(name="articleTitle", value="文章题目", required=true, dataType="String", paramType="form"),
		@ApiImplicitParam(name="articleContent", value="文章内容", required=true, dataType="String", paramType="form")
	})	
	@PostMapping(value="/uploadArticle")
	public JSONResult upload(String userId, String articleTag, String articleTitle, String articleContent) throws Exception {
		
		if (StringUtils.isBlank(userId) || StringUtils.isEmpty(userId)) {
			return JSONResult.errorMsg("Id can't be null");
		}

		// 保存文章信息到数据库
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
	
	/**
	 * @param userId
	 * @param articleId
	 * @param order 作为文件名 保证序列和发送一直
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value="/uploadArticleImg")
	public JSONResult uploadArticleImg(String userId ,String articleId, String order, @ApiParam(value="file", required=true) MultipartFile file) throws Exception {

		ArticleImage articleImage = new ArticleImage();
		
		if (file != null) {
			// 判断是否超出大小限制
			if (file.getSize() > MAX_FACE_IMAGE_SIZE) {
				return JSONResult.errorException("Uploaded file size exceed server's limit (10MB)");
			}
			// 保存图片
			String fileSpace = resourceConfig.getFileSpace();	// 文件保存空间地址
			// 获取文件后缀
			String fileName = file.getOriginalFilename();
			String[] strList = fileName.split("\\.");
	
			String newFileName = order + "." + strList[strList.length-1];	// 把顺序 order.原后缀 作为文件名
			// 保存到数据库中的相对路径
<<<<<<< HEAD

			String uploadPathDB = "/" + userId + "/article" + "/" + articleId + "/" + newFileName;

=======
			String uploadPathDB = "/" + userId + "/article" + "/" + articleId + "/" + newFileName;
>>>>>>> deyan
			// 文件上传的最终保存路径
			String finalVideoPath = "";
			
			if (StringUtils.isNotBlank(newFileName)) {
				finalVideoPath = fileSpace + uploadPathDB;
				uploadFile(file, finalVideoPath);	// 调用 BasicController 里的方法
				articleImage.setImagePath(uploadPathDB);
				articleImage.setArticleId(articleId);
			}
			articleService.saveArticleImages(articleImage);
			
		}
		
		return JSONResult.ok();
	}

	/**
	 * fromUserId 必填
	 * toUserId 必填
	 * articleId 必填
	 * fatherCommentId
	 * comment 必填
	 * PS: 父级（一级，给文章评论）评论 无 fatherCommentId;
	 *     子级评论有 fatherCommentId;
	 */
	@PostMapping("/saveComment")
	public JSONResult saveComment(@RequestBody UserArticleComment comment) throws Exception {
		// 存入数据库
		String commentId = articleService.saveComment(comment);

		// 给作者发推送
		DataContent dataContent = new DataContent();
		
		UserArticleCommentVO commentVO = articleService.getCommentById(commentId);
		if (StringUtils.isBlank(comment.getFatherCommentId())) {
			// 给文章评论
			ArticleVO targetArticle = articleService.getArticleById(comment.getArticleId());
			dataContent.setData(new CommentCard(commentVO, targetArticle));
			dataContent.setAction(MsgActionEnum.COMMENTARTICLE.type);
		}else {
			// 给评论评论
			UserArticleCommentVO targetComment = articleService.getCommentById(comment.getFatherCommentId());
			dataContent.setData(new CommentCard(commentVO, targetComment));
			dataContent.setAction(MsgActionEnum.COMMENTCOMMENT.type);
		}

		MsgHandler.sendMsgTo(comment.getToUserId(), dataContent);

		return JSONResult.ok();
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "articleId", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "page", required = false, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "pageSize", required = false, dataType = "Integer", paramType = "form") })
	@PostMapping("/getFatherComments")
	public JSONResult getFatherArticleComments(String articleId, Integer page, Integer pageSize) throws Exception {

		if (StringUtils.isBlank(articleId)) {
			return JSONResult.errorMsg("articleId can't be null");
		}

		if (page == null) {
			page = 1;
		}
		
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		PagedResult list = articleService.getAllComments(articleId, page, pageSize);

		return JSONResult.ok(list);
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "fatherCommentId", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "page", required = false, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "pageSize", required = false, dataType = "Integer", paramType = "form") })
	@PostMapping("/getSonComments")
	public JSONResult getSonArticleComments(String fatherCommentId, Integer page, Integer pageSize) throws Exception {
		
		if (StringUtils.isBlank(fatherCommentId)) {
			return JSONResult.errorMap("fatherCommentId can't be null");
		}
		
		if (page == null) {
			page = 1;
		}
		
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		
		PagedResult reCommentList = articleService.getSonComments(fatherCommentId, page, pageSize);
		
		return JSONResult.ok(reCommentList);
	}

	@ApiOperation(value = "Get the top 3 hot article")
	@PostMapping("/getHotTop3")
	public JSONResult getHotTop3() throws Exception {

		List<ArticleVO> list = articleService.getTop3ByPopularity();
		return JSONResult.ok(list);
	}
}
