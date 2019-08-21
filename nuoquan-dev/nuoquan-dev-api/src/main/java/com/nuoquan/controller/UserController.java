package com.nuoquan.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nuoquan.utils.FastDFSClient;
import com.nuoquan.mapper.UserMapper;
import com.nuoquan.pojo.ChatMsg;
import com.nuoquan.pojo.User;
import com.nuoquan.pojo.UserFans;
import com.nuoquan.pojo.vo.FansFollow;
import com.nuoquan.pojo.vo.UserVO;
import com.nuoquan.service.UserService;
import com.nuoquan.utils.JSONResult;

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
	private UserService userService;
	
	/**
	 * Description 上传文件到 fdfs 文件服务器 的实例方法
	 *
	 * PS：目前还是根据短视频里的储存逻辑，储存在本地。fdfs较为复杂，等我弄清楚了再做调整，本方法仅供参考。
	 * 
	 * @author jerrio
	 */
	@ApiOperation(value = "User uploads face image")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"), })
	@PostMapping("/uploadFace")
	public JSONResult uploadFace(String userId, @ApiParam(value = "face image", required = true) MultipartFile file)
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

	@ApiOperation(value = "Be the fans")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "fanId", required = true, dataType = "String", paramType = "form"), })
	@PostMapping("/follow")
	public JSONResult follow(String userId, String fanId) throws Exception {

		if (StringUtils.isBlank(fanId) || StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("Id can't be null");
		}

		userService.saveUserFanRelation(userId, fanId);

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

		return JSONResult.ok("Cancle follow success");
	}
	
	@ApiOperation(value = "Query a user's fans and follow lists")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),})
	@PostMapping("/queryFansAndFollow")
	public JSONResult queryFansAndFollow(String userId) {
		
		List<User> fansList = userService.queryUserFans(userId);
		List<User> followList = userService.queryUserFollow(userId);
		FansFollow fansAndFollow= new FansFollow(fansList, followList);
		
		return JSONResult.ok(fansAndFollow);
	}
	
	@ApiOperation(value = "Wechat first login or change profile")
	@PostMapping("/updateUser")
	public JSONResult updateUser(@RequestBody User userData) throws Exception {
		// 1. 判断用户名和密码不为空
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
	
	@ApiOperation(value = "Get the user's unread msg")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"), })
	@PostMapping("/getUnsignedMsg")
	public JSONResult getUnsignedMsg(String userId) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("User id can not be null.");
		}
		
		// 查询列表
		List<ChatMsg> unreadMsgList = userService.getUnsignedMsgList(userId);

		return JSONResult.ok(unreadMsgList);
	}
}
