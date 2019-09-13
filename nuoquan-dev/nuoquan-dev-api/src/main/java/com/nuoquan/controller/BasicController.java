package com.nuoquan.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nuoquan.config.ResourceConfig;
import com.nuoquan.pojo.User;
import com.nuoquan.pojo.vo.UserLikeVO;
import com.nuoquan.pojo.vo.UserVO;

@RestController
public class BasicController {
	
	@Value("${fdfs.groupName}")
	public String fdfsGroupName;
	
	@Autowired
	public ResourceConfig resourceConfig;
	
	// 每页分页的记录数
	public static final Integer PAGE_SIZE = 10;
	
	public UserVO ConvertUserToUserVO (User user) {
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(user, userVO);
		return userVO;
	}
	
	/**
	 * @param like can be UserLikeArticle or UserLikeComment
	 * @return
	 */
	public UserLikeVO ConvertLikeToLikeVO (Object like) {
		UserLikeVO likeVO = new UserLikeVO();
		BeanUtils.copyProperties(like, likeVO);
		return likeVO;
	}
	
	public void uploadFile(MultipartFile file, String path) throws Exception {

		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try {
			if (file != null) {

				File outFile = new File(path);
				if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
					// Create the parent directory.
					outFile.getParentFile().mkdirs();
				}

				fileOutputStream = new FileOutputStream(outFile);
				inputStream = file.getInputStream();
				IOUtils.copy(inputStream, fileOutputStream);
			} else {
				System.out.println("The file is null");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
	}
}
