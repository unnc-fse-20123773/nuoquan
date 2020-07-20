package com.nuoquan.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nuoquan.utils.FastDFSClient;
import com.nuoquan.email.EmailTool;
import com.nuoquan.enums.MsgActionEnum;
import com.nuoquan.enums.ReputeWeight;
import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.ChatMsg;
import com.nuoquan.pojo.User;
import com.nuoquan.pojo.netty.DataContent;
import com.nuoquan.pojo.netty.NoticeCard;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.pojo.vo.FansFollow;
import com.nuoquan.pojo.vo.UserArticleCommentVO;
import com.nuoquan.pojo.vo.UserLikeVO;
import com.nuoquan.pojo.vo.UserVO;
import com.nuoquan.pojo.vo.WxRes;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.JsonUtils;
import com.nuoquan.utils.UrlUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "User workflow logic")
@RequestMapping("/user")
public class UserController extends BasicController {
	@Autowired
	private FastDFSClient fastDFSClient;
	
	@Autowired
	private EmailTool emailTool;
	
	/**
	 * Description 上传文件到 fdfs 文件服务器 的实例方法
	 *
	 * PS：目前文件储存在本地。fdfs较为复杂，将来业务如果偏向图片/视频储存，可考虑启用，本方法仅供参考。
	 * 
	 * @author jerrio
	 */
	@Deprecated
	@ApiOperation(value = "An example for user uploading face image")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"), })
	@PostMapping("/uploadFaceExample")
	public JSONResult uploadFaceExample(String userId, @ApiParam(value = "face image", required = true) MultipartFile file)
			throws Exception {

		// 删除用户旧头像
		deleteOldFace(userId);

		String url = fastDFSClient.uploadBase64(file);
		System.out.println("/uploadFace: url=" + url);
		// 分割url
		// url example: "hiao30j02joajd156.png" -> "hiao30j02joajd156_80x80.png"
		String thumb = "_80x80.";
		String[] arr = url.split("\\.");
		String thumbImgUrl = arr[0] + thumb + arr[1];

		// 更新用户头像
		User user = new User();
		user.setId(userId);
		user.setFaceImg(url);
		user.setFaceImgThumb(thumbImgUrl);

		User userResult = userService.updateUserInfo(user);

		UserVO userVO = ConvertUserToUserVO(userResult);
		return JSONResult.ok(userVO);
	}

	public void deleteOldFace(String userId) {
		User user = userService.queryUserById(userId);
		String url = user.getFaceImg();
		String thumbUrl = user.getFaceImgThumb();
		fastDFSClient.deleteFile(fdfsGroupName, url);
		fastDFSClient.deleteFile(fdfsGroupName, thumbUrl);
	}
	
	@ApiOperation(value = "User uploading face image")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"), })
	@PostMapping("/uploadFace")
	public JSONResult uploadFace(String userId, @ApiParam(value = "face image", required = true) MultipartFile file)
			throws Exception {
				
		if (StringUtils.isNoneBlank(userId) && file != null) {
			// 判断是否超出大小限制
			if (file.getSize() > MAX_FACE_IMAGE_SIZE) {
				return JSONResult.errorException("Uploaded file size exceed server's limit (10MB)");
			}
			// 暂时不用删除用户旧头像 @Jerrio
			
			// 上传文件
			String fileName = file.getOriginalFilename();
			if (StringUtils.isNotBlank(fileName)) {
				// 保存到数据库中的相对路径
				String uploadPathDB = "/" + userId + "/face" + "/" + fileName;
				String fileSpace = resourceConfig.getFileSpace();	// 文件保存空间地址
				// 文件上传的最终保存路径
				String finalVideoPath = fileSpace + uploadPathDB;
				// 保存图片
				uploadFile(file, finalVideoPath);	// 调用 BasicController 里的方法

				User user = new User();
				user.setId(userId);
				user.setFaceImg(uploadPathDB);
				userService.updateUserInfo(user);
				return JSONResult.ok(uploadPathDB);
			}else {
				return JSONResult.errorMsg("File name is blank");
			}	
		}else {
			return JSONResult.errorMsg("Upload error");
		}
	}


	@ApiOperation(value = "Be the fans")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "fanId", required = true, dataType = "String", paramType = "form"), })
	@PostMapping("/follow")
	public JSONResult follow(String userId, String fanId) throws Exception {

		if (StringUtils.isBlank(fanId) || StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("Id can't be null");
		}

		userService.saveUserFanRelation(userId, fanId);
		
		//被关注者影响力++
		userService.updateReputation(userId, ReputeWeight.FOLLOW.weight, 1);
		
		return JSONResult.ok("Follow success");
	}

	@ApiOperation(value = "Don't be the fans")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "fanId", required = true, dataType = "String", paramType = "form"), })
	@PostMapping("/dontFollow")
	public JSONResult dontFollow(String userId, String fanId) throws Exception {

		if (StringUtils.isBlank(fanId) || StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("Id can't be null");
		}

		userService.deleteUserFanRelation(userId, fanId);
		
		//被关注者影响力--
		userService.updateReputation(userId, ReputeWeight.FOLLOW.weight, -1);

		return JSONResult.ok("Cancle follow success");
	}
	
	@ApiOperation(value = "Query a user's fans and follow lists")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),})
	@PostMapping("/queryFansAndFollow")
	public JSONResult queryFansAndFollow(String userId, String myId) {
		
		List<UserVO> fansList = userService.queryUserFans(userId, myId);
		List<UserVO> followList = userService.queryUserFollow(userId, myId);
		FansFollow fansAndFollow= new FansFollow(fansList, followList);
		
		return JSONResult.ok(fansAndFollow);
	}
	
	@ApiOperation(value = "Wechat first login or change profile")
	@PostMapping("/updateUser")
	public JSONResult updateUser(@RequestBody User userData) throws Exception {
		// 1. 判断用户名不为空
		if (StringUtils.isEmpty(userData.getId())) {
			return JSONResult.errorMsg("该账号为空");
		}
		User user = new User();
		// 2. 判断用户名是否存在
		boolean isIdExist = userService.checkIdIsExist(userData.getId());
		// 3. 注册信息
		if (!isIdExist) {
			// 只添加用户id（openId）头像和昵称
			user.setId(userData.getId());
			user.setNickname(userData.getNickname());
			user.setFaceImg(userData.getFaceImg());
			user.setFaceImgThumb(userData.getFaceImgThumb());
			user.setPassword("ChangeMe");
			user.setFollowNum(0);
			user.setFansNum(0);
			user.setCreateDate(new Date());
			user = userService.saveUserDirectly(user);
		} else {
			// 3.1 修改信息
			user.setId(userData.getId()); // 用作索引
			user.setNickname(userData.getNickname());
			user.setFaceImg(userData.getFaceImg());
			user.setFaceImgThumb(userData.getFaceImgThumb());
			user.setEmail(userData.getEmail());
			user.setDegree(userData.getDegree());
			user.setGraduationYear(userData.getGraduationYear());
			user.setGender(userData.getGender());
			user.setMajor(userData.getMajor());
			user.setSignature(userData.getSignature());
			user = userService.updateUserInfo(user);
		}
		// user.setPassword(null);
		// 将 user 对象转换为 userVO 输出，userVO 中不返回密码，且可加上其他属性。
		UserVO userVO = ConvertUserToUserVO(user);

		return JSONResult.ok(userVO);
	}

	@ApiOperation(value = "query user's info")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"), })
	@PostMapping("/queryUser")
	public JSONResult queryUser(String userId) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("User id can not be null.");
		}
		User user = userService.queryUserById(userId);
		// 将 user 对象转换为 userVO 输出，userVO 中不返回密码，且可加上其他属性。
		UserVO userVO = ConvertUserToUserVO(user);

		return JSONResult.ok(userVO);
	}
	
	@ApiOperation(value = "update the latest login time of user.")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form")
	})
	@PostMapping("/updateLatestLoginTime")
	public JSONResult updateLatestLoginTime(String userId) throws Exception{
		
		// 1. 判断用户名不为空
		if (StringUtils.isEmpty(userId)) {
			return JSONResult.errorMsg("该账号为空");
		}
		User user = new User();
		// 2. 判断用户名是否存在
		boolean isIdExist = userService.checkIdIsExist(userId);
		if (isIdExist) {
			userService.updateLatestLoginTime(userId);
			return JSONResult.ok();
		} else {
			return JSONResult.errorMsg("用户不存在");
		}
		
	}
	
	@ApiOperation(value = "query the user's info and whether I followed him")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "fanId", required = true, dataType = "String", paramType = "form"),})
	@PostMapping("/queryUserWithFollow")
	public JSONResult queryUserWithFollow(String userId, String fanId) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("User id can not be null.");
		}
		User user = userService.queryUserById(userId);
		// 将 user 对象转换为 userVO 输出，userVO 中不返回密码，且可加上其他属性。
		UserVO userVO = ConvertUserToUserVO(user);
		userVO.setFollow(userService.queryIfFollow(userId, fanId));
		
		return JSONResult.ok(userVO);
	}
	
	@ApiOperation(value = "Get the user's unread chat msg")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"), })
	@PostMapping("/getUnsignedMsg")
	public JSONResult getUnsignedMsg(String userId) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("User id can not be null.");
		}
		
		// 查询列表
		List<ChatMsg> unreadMsgList = userService.getUnsignedChat(userId);

		return JSONResult.ok(unreadMsgList);
	}
	
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
	 * 微信登陆获取openId
	 * @param code
	 * @param iv 加密算法的初始向量
	 * @param encryptedData 加密数据，好像可以根据官方提供的方式自行解密，还没试验
	 * @return
	 */
	@Value("${WXConst.appId}")
	public String appId;
	@Value("${WXConst.appSecret}")
	public String appSecret;
	@Value("${WXConst.wxGetOpenIdUrl}")
	public String wxGetOpenIdUrl;
	@ApiOperation(value = "get Wechat UserInfo")
	@PostMapping("/getWxUserInfo")
	public JSONResult getWxUserInfo(String code,String iv,String encryptedData, String nickname, String faceImg) throws Exception {
			
			// 获取openid
			Map<String,String> requestUrlParam = new HashMap<String,String>();
			requestUrlParam.put("appid", appId);	 //开发者设置中的appId  
			requestUrlParam.put("secret", appSecret); //开发者设置中的appSecret  
			requestUrlParam.put("js_code", code);	//小程序调用wx.login返回的code 

			//发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识  
			String res = UrlUtil.sendPost(wxGetOpenIdUrl, requestUrlParam);
			WxRes wxRes= JsonUtils.jsonToPojo(res, WxRes.class);
//			System.out.println(res);
//			System.out.println(wxRes.getOpenid());
			
			User user = new User();
			user.setId(wxRes.getOpenid());
			user.setNickname(nickname);
			user.setFaceImg(faceImg);
			UserVO userVO= wxLogin(user);
			if (userVO == null) {
				return JSONResult.errorMsg("空用户");
			}
			return JSONResult.ok(userVO);
	}
	
	/**
	 * 把微信 login 业务从 updateUser 里剥离出来
	 * @param userData
	 * @return
	 * @throws Exception
	 */
	public UserVO wxLogin(@RequestBody User userData) throws Exception {
		// 1. 判断用户名不为空
		if (StringUtils.isEmpty(userData.getId())) {
			return null;
		}
		User user = new User();
		// 2. 判断用户名是否存在
		boolean isIdExist = userService.checkIdIsExist(userData.getId());
		// 3. 注册信息
		if (!isIdExist) {
			// 3.1 只添加用户id（openId）头像和昵称
			user.setId(userData.getId());
			user.setNickname(userData.getNickname());
			user.setFaceImg(userData.getFaceImg());
			user.setFaceImgThumb(userData.getFaceImgThumb());
			user.setPassword("ChangeMe");
			user.setFollowNum(0);
			user.setFansNum(0);
			user.setCreateDate(new Date());
			user = userService.saveUserDirectly(user);
		} else {
			// 3.2 查询信息
			user = userService.queryUserById(userData.getId());
		}
		// 将 user 对象转换为 userVO 输出，userVO 中不返回密码，且可加上其他属性。
		UserVO userVO = ConvertUserToUserVO(user);

		return userVO;
	}
	
	@ApiOperation(value = "Get the identifying code by email")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"), 
			@ApiImplicitParam(name = "email", required = true, dataType = "String", paramType = "form")})
	@PostMapping("/getCode")
	public JSONResult getCode(String userId, String email) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("User id can not be null.");
		}
		// 生成验证码
		int length = 6; // 位数
		String code = "";
		
		Random random = new Random();
		for (int i = 0 ; i<length; i++) {
			code+=random.nextInt(10);
		}
//		System.out.println(code);
		// 存入 redis
		redis.set(USER_EMAIL_CODE + ":" + userId, email + code, 60 * 10); // 过期时间单位为秒 10分钟过期
		// 发送验证码邮件模板
		emailTool.sendCodeToMail(email, code);
		
		return JSONResult.ok();
	}
	
	@ApiOperation(value = "Confirm identifying code")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"), 
			@ApiImplicitParam(name = "code", required = true, dataType = "String", paramType = "form")})
	@PostMapping("/confirmCode")
	public JSONResult confirmCode(String userId, String email, String code) throws Exception {

		String rightCode = redis.get(USER_EMAIL_CODE + ":" + userId);
		if (StringUtils.isBlank(rightCode)) {
			return JSONResult.errorMsg("The code for this user is blank.");
		}else {
			String finalCode = email + code;
			if (finalCode.equals(rightCode)) {
				// 认证成功，修改用户邮箱
				User user = new User();
				user.setId(userId);
				user.setEmail(email);
				UserVO userVO = ConvertUserToUserVO(userService.updateUserInfo(user));
				return JSONResult.ok(userVO);
			}
		}
		return JSONResult.errorMsg("Wrong code.");
	}
}
