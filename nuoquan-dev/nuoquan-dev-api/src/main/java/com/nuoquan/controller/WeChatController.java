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
import org.springframework.web.bind.annotation.RequestParam;
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
@Api(value = "微信相关接口", tags = { "WeChat-Controller" })
@RequestMapping("/wechat")
public class WeChatController extends BasicController {
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", required = false, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "scene", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "width", required = false, dataType = "Number", paramType = "form"),
		@ApiImplicitParam(name = "isHyaline", required = false, dataType = "boolean", paramType = "form") })
	@PostMapping("/getQrcodeUnlimit")
	public JSONResult getQrcodeUnlimit(String page, String scene, Number width, boolean isHyaline) {
		String imgBuffer = weChatService.getQrcodeUnlimit(page, scene, width, isHyaline);
		return JSONResult.ok(imgBuffer);
	}
}
