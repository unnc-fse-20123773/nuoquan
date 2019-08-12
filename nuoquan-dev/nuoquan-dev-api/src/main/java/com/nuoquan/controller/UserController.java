package com.nuoquan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nuoquan.utils.FastDFSClient;
import com.nuoquan.pojo.User;
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
	 *	Description 上传文件到 fdfs 文件服务器 的实例方法
	 *
	 *	PS：目前还是根据短视频里的储存逻辑，储存在本地。fdfs较为复杂，等我弄清楚了再做调整，本方法仅供参考。
	 *	@author jerrio
	 */
	@ApiOperation(value = "User uploads face image")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", required = true, dataType = "String", paramType = "form"),
	})
	@PostMapping("/uploadFace")
	public JSONResult uploadFace(String userId, 
			@ApiParam(value = "face image", required = true)
			MultipartFile file) throws Exception {
		
		// 删除用户旧头像
		deleteOldFace(userId);
		
		String url = fastDFSClient.uploadBase64(file);
		System.out.println("/uploadFace: url="+url);
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

}
