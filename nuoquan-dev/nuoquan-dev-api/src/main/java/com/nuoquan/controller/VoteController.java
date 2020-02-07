package com.nuoquan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import com.nuoquan.enums.MsgActionEnum;
import com.nuoquan.enums.MsgSignFlagEnum;
import com.nuoquan.enums.StatusEnum;
import com.nuoquan.netty.MsgHandler;
import com.nuoquan.pojo.User;
import com.nuoquan.pojo.UserLikeCommentVote;
import com.nuoquan.pojo.UserVoteComment;
import com.nuoquan.pojo.Vote;
import com.nuoquan.pojo.VoteImage;
import com.nuoquan.pojo.VoteOption;
import com.nuoquan.pojo.netty.DataContent;
import com.nuoquan.pojo.netty.NoticeCard;
import com.nuoquan.pojo.vo.UserLikeVO;
import com.nuoquan.pojo.vo.UserVoteCommentVO;
import com.nuoquan.pojo.vo.VoteVO;
import com.nuoquan.service.UserService;
import com.nuoquan.service.VoteService;
import com.nuoquan.service.WeChatService;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.PagedResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "投票相关接口", tags = {"Vote-Controller"})
@RequestMapping("/vote")
public class VoteController extends BasicController{
	
	@Autowired
	private WeChatService weChatService;
	
	@Autowired
	private VoteService voteService;
	
	@Autowired
	private UserService userService;
	
	@Value("${upload.maxFaceImageSize}")
	private long MAX_FACE_IMAGE_SIZE;
	
	@ApiOperation(value = "查询全部投票", notes = "查询全部文章的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping("/queryAllVotes")
	public JSONResult queryAllVotes(Integer page, Integer pageSize, String userId) throws Exception {
		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		PagedResult result = voteService.getAllVotes(page, pageSize, userId);
		
		return JSONResult.ok(result);
	}
	
	@ApiOperation(value = "上传投票", notes = "上传投票的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId", value="作者id", required=true, dataType="String", paramType="form"),
		@ApiImplicitParam(name="voteTitle", value="投票题目", required=true, dataType="String", paramType="form"),
		@ApiImplicitParam(name="voteContent", value="投票内容", required=true, dataType="String", paramType="form"),
		@ApiImplicitParam(name="optionContent", value="投票选项", required=true, dataType="String", paramType="form")
	})
	@PostMapping(value="uploadVote")
	public JSONResult uploadVote(String userId, String voteTitle, String voteContent, String optionContent) throws Exception {
		if (StringUtils.isBlank(userId) || StringUtils.isEmpty(userId)) {
			return JSONResult.errorMsg("UserId can't be null");
		}
		if (StringUtils.isEmpty(optionContent) || StringUtils.isBlank(optionContent)) {
			return JSONResult.errorMsg("voteContent can't be null");
		}
		boolean isLegal = false;
		// 保存投票信息到数据库
		Vote vote = new Vote();
		vote.setVoteTitle(voteTitle);
		vote.setVoteContent(voteContent);
		vote.setUserId(userId);
		Date date = new Date();
		vote.setCreateDate(date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.DATE, 3);
		Date expiryDate = ca.getTime();
		vote.setExpiryDate(expiryDate);
		
		// 检测内容是否合法
		if (weChatService.msgSecCheck(voteTitle)
				&& weChatService.msgSecCheck(voteContent)
				&& weChatService.msgSecCheck(optionContent)) {
			// 合法
			isLegal = true;
			vote.setStatus(StatusEnum.READABLE.type);
		} else {
			// 非法, 存入数据库, 状态为不可见
			vote.setStatus(StatusEnum.UNREADABLE.type);
		}
		
		// save to database
		String voteId = voteService.saveVote(vote);
		VoteOption voteOption = new VoteOption();
		voteOption.setOptionContent(optionContent);
		voteOption.setVoteId(voteId);
		voteService.saveVoteOption(voteOption);
		
		if (isLegal) {
			return JSONResult.ok(voteId);
		} else {
			return JSONResult.errorMap(voteId);
		}
	}
	

	@PostMapping(value = "/uploadVoteImg")
	public JSONResult uploadVoteImg(String userId, String voteId, String order, @ApiParam(value="file", required=true) MultipartFile file) throws Exception {
		VoteImage voteImage = new VoteImage();
		
		if (file != null) {
			// 判断是否超出大小限制
			if (file.getSize() > MAX_FACE_IMAGE_SIZE) {
				return JSONResult.errorException("Uploaded file size exceed server's limit (10MB)");
			}
			// 保存图片
			String fileSpace = resourceConfig.getFileSpace();
			// 获取文件后缀
			String fileName = file.getOriginalFilename();
			String[] strList = fileName.split("\\.");
			
			// 把顺序 order.原后缀 作为文件名
			String newFileName = order + "." + strList[strList.length-1];
			// 保存到数据库中的相对路径
			String uploadPathDB = "/" + userId + "/vote" + "/" + voteId + "/" + newFileName;
			// 文件上传的最终保存路径
			String finalVideoPath = "";
			
			if (StringUtils.isNotBlank(newFileName)) {
				finalVideoPath = fileSpace + uploadPathDB;
				uploadFile(file, finalVideoPath);
				voteImage.setImagePath(uploadPathDB);
				voteImage.setVoteId(voteId);
			}
			voteService.saveVoteImages(voteImage);
		}
		return JSONResult.ok();
	}
	
	/**
	 * fromUserId 必填
	 * toUserId 必填
	 * voteId 必填 // 为了计算投票总评论数
	 * underCommentId // 显示在该主评论层ID下
	 * fatherCommentId // 父级评论ID
	 * comment 必填
	 * PS: 父级（一级，给文章评论）评论 无 fatherCommentId, underCommentId;
	 *     子级评论有 fatherCommentId, underCommentId;
	 */
	@PostMapping("/saveVoteComment")
	public JSONResult saveVoteComment(@RequestBody UserVoteComment comment) throws Exception{
		
		// 内容安全监测
		if (weChatService.msgSecCheck(comment.getComment())) {
			
			if (comment.getToUserId().equals(comment.getFromUserId())) {
				// 给自己评论, 设为已签收存入数据库
				comment.setSignFlag(MsgSignFlagEnum.SIGNED.type);
				voteService.saveComment(comment);
			} else {
				// 给他人评论, 设为未签收存入数据库
				comment.setSignFlag(MsgSignFlagEnum.UNSIGN.type);
				String commentId = voteService.saveComment(comment);
				
				// 给作者发推送
				DataContent dataContent = new DataContent();
				
				UserVoteCommentVO commentVO = voteService.getCommentById(commentId, null);
				if (StringUtils.isBlank(comment.getFatherCommentId())) {
					// 给文章评论
					VoteVO targetVote = voteService.getVoteById(comment.getVoteId(), null);
					dataContent.setData(new NoticeCard(commentVO, targetVote));
					dataContent.setAction(MsgActionEnum.COMMENTARTICLE.type);
				} else {
					// 给评论评论
					UserVoteCommentVO targetComment = voteService.getCommentById(comment.getFatherCommentId(), null);
					dataContent.setData(new NoticeCard(commentVO, targetComment));
					dataContent.setAction(MsgActionEnum.COMMENTCOMMENT.type);
				}
				
				MsgHandler.sendMsgTo(comment.getToUserId(), dataContent);
			}
			return JSONResult.ok();
		} else {
			return JSONResult.errorMsg("非法内容");
		}
	}
	
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", required = false, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "pageSize", required = false, dataType = "Integer", paramType = "form"),
			@ApiImplicitParam(name = "voteId", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "userId", required = false, dataType = "String", paramType = "form")})
	@PostMapping("/getMainVoteComments")
	public JSONResult getFatherVoteComments(Integer page, Integer pageSize, String voteId, String userId) throws Exception{
		
		if (StringUtils.isBlank(voteId)) {
			return JSONResult.errorMsg("voteId can't be null");
		}
		
		if (page == null) {
			page = 1;
		}
		
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		
		PagedResult list = voteService.getFatherComments(page, pageSize, voteId, userId);
		
		return JSONResult.ok(list);
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "underCommentId", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "page", required = false, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "pageSize", required = false, dataType = "Integer", paramType = "form"),
		@ApiImplicitParam(name = "userId", required = false, dataType = "String", paramType = "form")})
	@PostMapping("/getSubComments")
	public JSONResult getSonVoteComments(Integer page, Integer pageSize, String underCommentId, String userId) throws Exception{
		
		if (StringUtils.isBlank(underCommentId)) {
			return JSONResult.errorMsg("underCommentId can't be null");
		}
		
		if (page == null) {
			page = 1;
		}
		
		if (pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		
		PagedResult reCommentList = voteService.getSonComments(page, pageSize, underCommentId, userId);
		
		return JSONResult.ok(reCommentList);
	}
	
	@ApiOperation(value = "点赞投票的评论")
	@ApiImplicitParams({
		// uniapp使用formData时，paramType要改成form
		@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "createrId", value = "作者id", required = true, dataType = "String", paramType = "form")})
	@PostMapping(value = "/userLikeVoteComment")
	public JSONResult userLikeVoteComment(String userId, String commentId, String createrId) throws Exception {
		if (userId.equals(createrId)) {
			// 点赞自己, 标记已签收存入数据
			voteService.userLikeComment(userId, commentId, createrId, MsgSignFlagEnum.SIGNED.type);
		} else {
			// 标记未签收, 储存到数据库, 返回数据库对象
			UserLikeCommentVote likeVote = voteService.userLikeComment(userId, commentId, createrId, MsgSignFlagEnum.UNSIGN.type);
			// 加上点赞人信息
			UserLikeVO likeVO = ConvertLikeToLikeVO(likeVote);
			User user = userService.queryUserById(likeVO.getUserId());
			likeVO.setNickname(user.getNickname());
			likeVO.setFaceImg(user.getFaceImg());
			likeVO.setFaceImgThumb(user.getFaceImgThumb());
			
			// 给作者发推送
			DataContent dataContent = new DataContent();
			dataContent.setAction(MsgActionEnum.LIKECOMMENT.type);
			dataContent.setData(new NoticeCard(likeVO, voteService.getCommentById(commentId, userId)));
			
			MsgHandler.sendMsgTo(createrId, dataContent);
		}
		
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "取消点赞评论")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "createrId", value = "作者id", required = true, dataType = "String", paramType = "form") })
	@PostMapping(value = "/userUnLikeVoteComment")
	public JSONResult userUnlikeComment(String userId, String commentId, String createrId) throws Exception {
		voteService.userUnLikeComment(userId, commentId, createrId);
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "更改投票的状态为unvotable")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "voteId", value = "投票id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value="/endVote")
	public JSONResult endVote(String voteId) throws Exception {
		voteService.fDeleteVote(voteId);
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "更改投票的状态为votable")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "voteId", value = "投票id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value="/passVote")
	public JSONResult passVote(String voteId) throws Exception {
		voteService.passVote(voteId);
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "更改投票的状态为unqualified")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "voteId", value = "投票id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value="/banVote")
	public JSONResult banVote(String voteId) throws Exception {
		voteService.banVote(voteId);
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "更改评论状态为ban")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value="/banComment")
	public JSONResult banComment(String commentId) throws Exception {
		voteService.banComment(commentId);
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "更改评论状态为pass")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping(value="/passComment")
	public JSONResult passComment(String commentId) throws Exception {
		voteService.passComment(commentId);
		return JSONResult.ok();
	}
}










