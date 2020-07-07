package com.nuoquan.controller;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nuoquan.enums.MsgActionEnum;
import com.nuoquan.enums.MsgSignFlagEnum;
import com.nuoquan.enums.ReputeWeight;
import com.nuoquan.enums.StatusEnum;
import com.nuoquan.netty.MsgHandler;
import com.nuoquan.pojo.Article;
import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.User;
import com.nuoquan.pojo.UserArticleComment;
import com.nuoquan.pojo.UserCollectArticle;
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

	@Value("${upload.maxFaceImageSize}")
	private long MAX_FACE_IMAGE_SIZE;

	/**
	 * 
	 * @param page
	 * @param pageSize
	 * @param queryType 0 -- 按"所有"请求, 1 -- 按"关注"请求
	 * @param orderType 0 -- 按时间倒序排列, 1 -- 按热度正序排列
	 * @param userId 操作者id
	 * @param selectedTag 选中的标签, 如果为空则正常显示所有文章, 不为空, 则根据传进来的值查询对应文章(拥有该标签的文章)
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "查询全部文章", notes = "查询全部文章的接口")
	@ApiImplicitParams({
		// userId 查询用户和文章的点赞关系
		// dataType 为 String, 应该改为 Integer
		@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "queryType", value = "排列方式", required = true, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "orderType", value = "排列方式", required = true, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "selectedTag", value = "选择的标签", required = false, dataType = "String", paramType = "form")
		})
	@PostMapping("/queryArticles")
	public JSONResult queryArticles(Integer page, Integer pageSize, Integer queryType, Integer orderType, String userId, String selectedTag) throws Exception {

		PagedResult result = new PagedResult();
		
		result = articleService.queryArticle(page, pageSize, queryType, userId, selectedTag);
		
		return JSONResult.ok(result);
	}
	
	/**
	 * 
	 * @param page
	 * @param pageSize
	 * @param type 0 -- 按时间倒序排列, 1 -- 按热度正序排列
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	@ApiOperation(value = "查询我关注的人的全部文章", notes = "查询我关注的人的全部文章的接口")
	@ApiImplicitParams({
		// userId 查询用户和文章的点赞关系
		// dataType 为 String, 应该改为 Integer
		@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "type", value = "文章的排列方式", required = true, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/queryAllSubscribedAuthorArticles")
	public JSONResult queryAllSubscribedAuthorArticles(Integer page, Integer pageSize, Integer type, String userId) throws Exception {
		
		PagedResult result = new PagedResult();
		
		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		
		if (type == 0) {
			result = articleService.getAllSubscribedAuthorArticles(page, pageSize, userId);
		}
		
		if (type == 1) {
			result = articleService.getAllSubscribedAuthorArticlesByPopularity(page, pageSize, userId);
		}

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
			
			//作者影响力++
			userService.updateReputation(articleCreaterId, ReputeWeight.LIKE.weight, 1);
			
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
			
			//作者影响力++
			userService.updateReputation(createrId, ReputeWeight.LIKE.weight, 1);
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
		
		//文章作者影响力--
		userService.updateReputation(articleCreaterId, ReputeWeight.LIKE.weight, -1);
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
		
		//作者影响力--
		userService.updateReputation(createrId, ReputeWeight.LIKE.weight, -1);
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
	@ApiOperation(value = "综合搜索")
	@PostMapping(value = "/searchArticleYANG")
	public JSONResult searchArticleYang(String searchText, Integer isSaveRecord, Integer page, String userId)
			throws Exception {

		if (page == null) {
			page = 1;
		}
		
		PagedResult result = articleService.searchArticleYang(isSaveRecord, page, PAGE_SIZE, searchText, userId);
		return JSONResult.ok(result);
	}
	
	@Deprecated
	@ApiOperation(value = "按标签搜索文章")
	@PostMapping(value = "/queryArticleByTag")
	public JSONResult queryArticleByTag(String searchText, Integer page, String userId)
		throws Exception {
		if (page == null ) {
			page = 1;
		}
		
		PagedResult result = articleService.searchArticleByTag(page, PAGE_SIZE, searchText, userId);
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
			if (resourceConfig.getAutoCheckArticle()) { //查看是否设置自动过审
				article.setStatus(StatusEnum.READABLE.type);
			}else {
				article.setStatus(StatusEnum.CHECKING.type);
			}
		} else {
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

		if (StringUtils.isNoneBlank(userId) && file != null) {
			// 判断是否超出大小限制
			if (file.getSize() > MAX_FACE_IMAGE_SIZE) {
				return JSONResult.errorException("Uploaded file size exceed server's limit (10MB)");
			}
			String fileName = file.getOriginalFilename();
			if (StringUtils.isNotBlank(fileName)) {
				// 获取文件后缀
				String[] strList = fileName.split("\\.");
				String newFileName = order + "." + strList[strList.length-1];	// 把顺序 order.原后缀 作为文件名
				// 保存到数据库中的相对路径
				String uploadPathDB = "/" + userId + "/article" + "/" + articleId + "/" + newFileName;
				String fileSpace = resourceConfig.getFileSpace();	// 文件保存空间地址
				// 文件上传的最终保存路径
				String finalVideoPath = fileSpace + uploadPathDB;
				// 保存图片
				uploadFile(file, finalVideoPath);	// 调用 BasicController 里的方法
				
				Integer imageOrder = Integer.valueOf(order);

				ArticleImage articleImage = new ArticleImage();
				articleImage.setImagePath(uploadPathDB);
				articleImage.setArticleId(articleId);
				articleImage.setImageOrder(imageOrder);
				articleService.saveArticleImages(articleImage);
				
				return JSONResult.ok();
			}else {
				return JSONResult.errorMsg("File name is blank");
			}
		}else {
			return JSONResult.errorMsg("Upload error");
		}
	}

	@ApiOperation(value = "删除文章")
	@ApiImplicitParams({
			// uniapp使用formData时，paramType要改成form
			@ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value="/deleteArticle")
	public JSONResult deleteArticle(String articleId, String userId) throws Exception {
		articleService.deleteArticle(articleId, userId);
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "更改文章状态为unreadble")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value="/fDeleteArticle")
	public JSONResult fDeleteArticle(String articleId) throws Exception {
		articleService.fDeleteArticle(articleId);
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
				
				//文章作者影响力++
				String author = articleService.getArticleById(comment.getArticleId(), null).getUserId();
				userService.updateReputation(author, ReputeWeight.COMMENT.weight, 1);
			}

			return JSONResult.ok();
		}else {
			return JSONResult.errorMsg("内容不合法");
		}
		
	}

	/**
	 * 
	 * @param page
	 * @param pageSize
	 * @param type  0 -- 按时间查询, 1 -- 按热度查询
	 * @param articleId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", required = false, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "pageSize", required = false, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "type", required = true, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "articleId", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "userId", required = false, dataType = "String", paramType = "form")})
	@PostMapping("/getMainComments")
	public JSONResult getFatherArticleComments(Integer page, Integer pageSize, Integer type, String articleId, String userId) throws Exception {

		if (StringUtils.isBlank(articleId)) {
			return JSONResult.errorMsg("articleId can't be null");
		}

		if (page == null) {
			page = 1;
		}
		
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}

		// type: 0 -- 按时间查询, 1 -- 按热度查询
		PagedResult list = articleService.getMainComments(page, pageSize, type, articleId, userId);

		return JSONResult.ok(list);
	}
	
	/**
	 * 
	 * @param page
	 * @param pageSize
	 * @param type  0 -- 按时间查询, 1 -- 按热度查询
	 * @param underCommentId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name = "underCommentId", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "page", required = false, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "type", required = true, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "pageSize", required = false, dataType = "Integer", paramType = "form") })
	@PostMapping("/getSubComments")
	public JSONResult getSonArticleComments(Integer page, Integer pageSize, Integer type, String underCommentId, String userId) throws Exception {
		
		if (StringUtils.isBlank(underCommentId)) {
			return JSONResult.errorMsg("underCommentId can't be null");
		}
		
		if (page == null) {
			page = 1;
		}
		
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		
		// type: 0 -- 按时间查询, 1 -- 按热度查询
		PagedResult reCommentList = articleService.getSonComments(page, pageSize, type, underCommentId, userId);
		
		return JSONResult.ok(reCommentList);
	}
	
	/**
	 * 根据热度查询文章
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Get the top 10 hot article")
	@PostMapping("/getHotTop10")
	public JSONResult getHotTop10(Integer page, Integer pageSize, String userId) throws Exception {

		if(page == null) {
			page = 1;
		}
		if(pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		
		PagedResult result = articleService.getArticleByPopurity(page, pageSize, userId);
//		List<ArticleVO> list = articleService.getTop3ByPopularity(userId);
		return JSONResult.ok(result);
	}
	
	@Deprecated //该接口移动到UserController
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/getUnsignedLikeMsg")
	public JSONResult getUnsignedLikeMsg(String userId) {
		List<UserLikeVO> likeVOs = userService.getUnsignedLikeMsg(userId);
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
	@Deprecated //该接口移动到UserController
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form") })
	@PostMapping("/getUnsignedCommentMsg")
	public JSONResult getUnsignedCommentMsg(String userId) {
		List<UserArticleCommentVO> commentVOs = userService.getUnsignedCommentMsg(userId);
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
	
	/**
	 * 文章浏览量+1
	 * @param userId
	 * @param articleId
	 * @return
	 */
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
			//作者影响力++
			String author = articleService.getArticleById(articleId, null).getUserId();
			userService.updateReputation(author, ReputeWeight.VIEW.weight, 1);
		}
		return JSONResult.ok();
	}
	
	@PostMapping("/getTagsList")
	public JSONResult getTagsList() {
		return JSONResult.ok(tagsService.getTagsList());
	}
	
	@ApiOperation(value = "收藏文章")
	@ApiImplicitParams({
			// uniapp使用formData时，paramType要改成form
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "articleCreaterId", value = "文章作者id", required = true, dataType = "String", paramType = "form") })
	@PostMapping(value = "/userCollectArticle")
	public JSONResult userCollectArticle(String userId, String articleId, String articleCreaterId) throws Exception {
	
		UserCollectArticle uca;

		if (userId.equals(articleCreaterId)) {
			// 收藏自己的文章，标记已签收存入数据
			uca = articleService.userCollectArticle(userId, articleId, MsgSignFlagEnum.SIGNED.type);
		}else {
			// 标记未签收，储存到数据库 返回数据库对象
			uca = articleService.userCollectArticle(userId, articleId, MsgSignFlagEnum.UNSIGN.type);
			// 加上收藏人的信息
			
			// 给目标作者发推送

			//作者影响力++
		}
		if (uca == null) {
			return JSONResult.errorMsg("无法收藏该状态下的文章");
		}
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "取消收藏文章")
	@ApiImplicitParams({
			// uniapp使用formData时，paramType要改成form
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "articleCreaterId", value = "文章作者id", required = true, dataType = "String", paramType = "form") })
	@PostMapping(value = "/userUncollectArticle")
	public JSONResult userUncollectArticle(String userId, String articleId, String articleCreaterId) throws Exception {
		articleService.userUncollectArticle(userId, articleId);
		//作者影响力--
		
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "查询某人收藏的文章", notes = "查看他人时只能查看status为1的, 查询自己时,可显示所有status")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "targetId", value = "目标查询者id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping("/queryCollectArticle")
	public JSONResult queryCollectArticle(Integer page, Integer pageSize, String userId, String targetId) {
		
		if(page == null) {
			page = 1;
		}
		if(pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		
		// 查询targetId收藏状态为1的文章
		PagedResult result = articleService.queryCollectArticle(page, pageSize, userId, targetId);
		
		return JSONResult.ok(result);
		
	}
	
}
