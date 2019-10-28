package com.nuoquan.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nuoquan.enums.MsgActionEnum;
import com.nuoquan.enums.MsgSignFlagEnum;
import com.nuoquan.enums.StatusEnum;
import com.nuoquan.netty.MsgHandler;
import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.User;
import com.nuoquan.pojo.UserArticleComment;
import com.nuoquan.pojo.UserLikeArticle;
import com.nuoquan.pojo.UserLikeComment;
import com.nuoquan.pojo.netty.NoticeCard;
import com.nuoquan.pojo.netty.DataContent;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.pojo.vo.UserArticleCommentVO;
import com.nuoquan.pojo.vo.UserLikeVO;
import com.nuoquan.service.ArticleService;
import com.nuoquan.service.UserService;
import com.nuoquan.service.WeChatService;
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
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WeChatService weChatService;

	@Value("${upload.maxFaceImageSize}")
	private long MAX_FACE_IMAGE_SIZE;

	@ApiOperation(value = "查询全部文章", notes = "查询全部文章的接口")
	@ApiImplicitParams({
		// userId 查询用户和文章的点赞关系
		// dataType 为 String, 应该改为 Integer
		@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/queryAllArticles")
	public JSONResult showAllArticles(Integer page, Integer pageSize, String userId) throws Exception {

		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		PagedResult result = articleService.getAllArticles(page, pageSize, userId);

		return JSONResult.ok(result);
	}
	
	@ApiOperation(value = "查询我的发布的文章和他人发布的文章", notes = "查看他人时只能查看status为1的, 查询自己时,可显示所有status")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "targetId", value = "目标查询者id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping("/queryPublishHistory")
	public JSONResult queryPublishHistory(Integer page, Integer pageSize, String userId, String targetId) {
		
		PagedResult finalResult = new PagedResult();
		
		if(page == null) {
			page = 1;
		}
		if(pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		if(userId.equals(targetId)) {
			// 查询所有状态的文章
			PagedResult result = articleService.getAllMyHisArticle(page, pageSize, userId);
			finalResult = result;
		} else if (!userId.equals(targetId)) {
			// 查询他人文章状态为1的文章
			PagedResult result = articleService.gerOtherslegalHisArticle(page, pageSize, userId, targetId);
			finalResult = result;
		}
		
		
		return JSONResult.ok(finalResult);
		
	}
	
	@ApiOperation(value = "按文章 id 查询文章", notes = "查询全部文章的接口")
	@ApiImplicitParams({
		// userId 查询用户和文章的点赞关系
		@ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/getArticleById")
	public JSONResult getArticleById(String articleId, String userId) throws Exception {

		return JSONResult.ok(articleService.getArticleById(articleId, userId));
	}

	@ApiOperation(value = "点赞文章")
	@ApiImplicitParams({
			// uniapp使用formData时，paramType要改成form
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "articleCreaterId", value = "文章作者id", required = true, dataType = "String", paramType = "form") })
	@PostMapping(value = "/userLikeArticle")
	public JSONResult userLikeArticle(String userId, String articleId, String articleCreaterId) throws Exception {
		
		if (userId.equals(articleCreaterId)) {
			// 点赞自己，标记已签收存入数据
			articleService.userLikeArticle(userId, articleId, articleCreaterId, MsgSignFlagEnum.SIGNED.type);
		}else {
			// 标记未签收，储存到数据库 返回数据库对象
			UserLikeArticle like = articleService.userLikeArticle(userId, articleId, articleCreaterId, MsgSignFlagEnum.UNSIGN.type);
			// 加上点赞人的信息
			UserLikeVO likeVO = ConvertLikeToLikeVO(like);
			User user = userService.queryUserById(likeVO.getUserId());
			likeVO.setNickname(user.getNickname());
			likeVO.setFaceImg(user.getFaceImg());
			likeVO.setFaceImgThumb(user.getFaceImgThumb());
			
			// 给目标作者发推送
			DataContent dataContent = new DataContent();
			dataContent.setAction(MsgActionEnum.LIKEARTICLE.type);
			dataContent.setData(new NoticeCard(likeVO, articleService.getArticleById(articleId, userId)));
			
			MsgHandler.sendMsgTo(articleCreaterId, dataContent);
		}
		
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
	
		if (userId.equals(createrId)) {
			// 点赞自己，标记已签收存入数据
			articleService.userLikeComment(userId, commentId, createrId, MsgSignFlagEnum.SIGNED.type);
		} else {
			// 标记未签收，储存到数据库 返回数据库对象
			UserLikeComment like = articleService.userLikeComment(userId, commentId, createrId, MsgSignFlagEnum.UNSIGN.type);
			// 加上点赞人的信息
			UserLikeVO likeVO = ConvertLikeToLikeVO(like);
			User user = userService.queryUserById(likeVO.getUserId());
			likeVO.setNickname(user.getNickname());
			likeVO.setFaceImg(user.getFaceImg());
			likeVO.setFaceImgThumb(user.getFaceImgThumb());
			
			// 给作者发推送
			DataContent dataContent = new DataContent();
			dataContent.setAction(MsgActionEnum.LIKECOMMENT.type);
			dataContent.setData(new NoticeCard(likeVO, articleService.getCommentById(commentId, userId)));
			
			MsgHandler.sendMsgTo(createrId, dataContent);
		}
	
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
	@ApiOperation(value = "按文章内容搜索")
	@PostMapping(value = "/searchArticleYANG")
	public JSONResult searchArticleYang(@RequestBody Article article, Integer isSaveRecord, Integer page, String userId)
			throws Exception {

		if (page == null) {
			page = 1;
		}
		
		PagedResult result = articleService.searchYangArticlesContent(isSaveRecord, page, PAGE_SIZE, article, userId);
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
		boolean isLegal = false;
		// 保存文章信息到数据库
		Article article = new Article();
		article.setArticleTitle(articleTitle);
		article.setArticleContent(articleContent);
		article.setUserId(userId);
		article.setTags(articleTag);
		article.setCreateDate(new Date());
		// 检测内容是否非法
		if (weChatService.msgSecCheck(articleTitle) 
			&& weChatService.msgSecCheck(articleTag)
			&& weChatService.msgSecCheck(articleContent)) {
			// 合法
			isLegal = true;
			article.setStatus(StatusEnum.READABLE.type);
		}else {
			// 非法，尽管非法也保存到数据库
			article.setStatus(StatusEnum.UNREADABLE.type);
		}
		String articleId = articleService.saveArticle(article); // 存入数据库
		
		if (isLegal) {
			return JSONResult.ok(articleId);
		}else {
			return JSONResult.errorMap(articleId);
		}
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
			String uploadPathDB = "/" + userId + "/article" + "/" + articleId + "/" + newFileName;
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

	@ApiOperation(value = "删除文章")
	@ApiImplicitParams({
			// uniapp使用formData时，paramType要改成form
			@ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String", paramType = "form") })
	@PostMapping(value="/deleteArticle")
	public JSONResult deleteArticle(String articleId) throws Exception {
		articleService.deleteArticle(articleId);
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "更改文章状态为ban")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value="/banArticle")
	public JSONResult banArticle(String articleId) throws Exception {
		articleService.banArticle(articleId);
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "更改文章状态为pass")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value="/passArticle")
	public JSONResult passArticle(String articleId) throws Exception {
		articleService.passArticle(articleId);
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "更改评论状态为ban")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value="/banComment")
	public JSONResult banComment(String commentId) throws Exception {
		articleService.banComment(commentId);
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "更改评论状态为pass")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value="/passComment")
	public JSONResult passComment(String commentId) throws Exception {
		articleService.passComment(commentId);
		return JSONResult.ok();
	}
	
	/**
	 * fromUserId 必填
	 * toUserId 必填
	 * articleId 必填 // 为了计算文章总评论数
	 * underCommentId // 显示在该主评论层ID下
	 * fatherCommentId // 父级评论ID
	 * comment 必填
	 * PS: 父级（一级，给文章评论）评论 无 fatherCommentId, underCommentId;
	 *     子级评论有 fatherCommentId, underCommentId;
	 */
	@PostMapping("/saveComment")
	public JSONResult saveComment(@RequestBody UserArticleComment comment) throws Exception {
		// 内容安全检测
		if (weChatService.msgSecCheck(comment.getComment()) ) {
			
			if(comment.getToUserId().equals(comment.getFromUserId())) {
				// 给自己评论，设为已签收存入数据库
				comment.setSignFlag(MsgSignFlagEnum.SIGNED.type);
				articleService.saveComment(comment);

			}else {
				// 给他人评论，设为未签收存入数据库
				comment.setSignFlag(MsgSignFlagEnum.UNSIGN.type);
				String commentId = articleService.saveComment(comment);

				// 给作者发推送
				DataContent dataContent = new DataContent();
				
				UserArticleCommentVO commentVO = articleService.getCommentById(commentId, null); // 无需查询用户点赞关系
				if (StringUtils.isBlank(comment.getFatherCommentId())) {
					// 给文章评论
					ArticleVO targetArticle = articleService.getArticleById(comment.getArticleId(), null);
					dataContent.setData(new NoticeCard(commentVO, targetArticle));
					dataContent.setAction(MsgActionEnum.COMMENTARTICLE.type);
				}else {
					// 给评论评论
					UserArticleCommentVO targetComment = articleService.getCommentById(comment.getFatherCommentId(), null);
					dataContent.setData(new NoticeCard(commentVO, targetComment));
					dataContent.setAction(MsgActionEnum.COMMENTCOMMENT.type);
				}
				
				MsgHandler.sendMsgTo(comment.getToUserId(), dataContent);
			}

			return JSONResult.ok();
		}else {
			return JSONResult.errorMsg("内容不合法");
		}
		
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", required = false, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "pageSize", required = false, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "articleId", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "userId", required = false, dataType = "String", paramType = "form")})
	@PostMapping("/getMainComments")
	public JSONResult getFatherArticleComments(Integer page, Integer pageSize, String articleId, String userId) throws Exception {

		if (StringUtils.isBlank(articleId)) {
			return JSONResult.errorMsg("articleId can't be null");
		}

		if (page == null) {
			page = 1;
		}
		
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		PagedResult list = articleService.getMainComments(page, pageSize, articleId, userId);

		return JSONResult.ok(list);
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "underCommentId", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "page", required = false, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "pageSize", required = false, dataType = "Integer", paramType = "form") })
	@PostMapping("/getSubComments")
	public JSONResult getSonArticleComments(Integer page, Integer pageSize, String underCommentId, String userId) throws Exception {
		
		if (StringUtils.isBlank(underCommentId)) {
			return JSONResult.errorMsg("underCommentId can't be null");
		}
		
		if (page == null) {
			page = 1;
		}
		
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		
		PagedResult reCommentList = articleService.getSonComments(page, pageSize, underCommentId, userId);
		
		return JSONResult.ok(reCommentList);
	}

	@ApiOperation(value = "Get the top 3 hot article")
	@PostMapping("/getHotTop3")
	public JSONResult getHotTop3(String userId) throws Exception {

		List<ArticleVO> list = articleService.getTop3ByPopularity(userId);
		return JSONResult.ok(list);
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/getUnsignedLikeMsg")
	public JSONResult getUnsignedLikeMsg(String userId) {
		List<UserLikeVO> likeVOs = articleService.getUnsignedLikeMsg(userId);
		List<DataContent> dataList = new LinkedList<>();
		for (UserLikeVO like : likeVOs) {
			DataContent dataContent = new DataContent();
			if (!StringUtils.isBlank(like.getArticleId())) {
				//System.out.println("点赞文章");
				ArticleVO articleVO = articleService.getArticleById(like.getArticleId(), userId);
				
				dataContent.setAction(MsgActionEnum.LIKEARTICLE.type);
				dataContent.setData(new NoticeCard(like, articleVO));
				
				dataList.add(dataContent);
			} else {
				//System.out.println("点赞评论");
				UserArticleCommentVO commentVO = articleService.getCommentById(like.getCommentId(), userId);
				
				dataContent.setAction(MsgActionEnum.LIKECOMMENT.type);
				dataContent.setData(new NoticeCard(like, commentVO));
				
				dataList.add(dataContent);
			}
		}
		return JSONResult.ok(dataList);
	}
	
	/**
	 * TODO: 该接口最多返回100个对象...并造成卡顿 解决方法：分页并自动累加页数
	 * @param userId
	 * @return
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/getUnsignedCommentMsg")
	public JSONResult getUnsignedCommentMsg(String userId) {
		List<UserArticleCommentVO> commentVOs = articleService.getUnsignedCommentMsg(userId);
		List<DataContent> dataList = new LinkedList<>();
		for (UserArticleCommentVO comment : commentVOs) {
			DataContent dataContent = new DataContent();
			if (StringUtils.isBlank(comment.getFatherCommentId())) {
				//System.out.println("评论文章");
				ArticleVO targetArticle = articleService.getArticleById(comment.getArticleId(), userId);
				
				dataContent.setData(new NoticeCard(comment, targetArticle));
				dataContent.setAction(MsgActionEnum.COMMENTARTICLE.type);
				
				dataList.add(dataContent);
			} else {
				//System.out.println("评论评论");
				UserArticleCommentVO targetComment = articleService.getCommentById(comment.getFatherCommentId(), userId);
				
				dataContent.setData(new NoticeCard(comment, targetComment));
				dataContent.setAction(MsgActionEnum.COMMENTCOMMENT.type);
				
				dataList.add(dataContent);
			}
		}
		return JSONResult.ok(dataList);
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "articleId", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/addViewCount")
	public JSONResult addViewCount(String userId, String articleId) {
		String key = "VIEW_COUNT:" + userId + articleId;
		String value = redis.get(key);
		if (StringUtils.isBlank(value)) {
			redis.set(key, "ture", 7200); //两小时内不重复计算浏览量
			articleService.addViewCount(articleId);
		}
		return JSONResult.ok();
	}
	
}
