package com.nuoquan.controllerTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.nuoquan.pojo.UserCollectArticle;
import com.nuoquan.test.BasicTest;

/**
 * articleController 的单元测试类，检测接口功能是否正常
 * @author jerrio
 *
 */
public class articleControllerTest extends BasicTest{
	
	@Test
	@Transactional //不污染数据库
	public void UserCollectArticle() throws Exception {
		String test_userId = "test1";
		String test_articleId = "1912128SBG07Z9P0";
		articleController.userCollectArticle(test_userId, test_articleId, null);
		Assert.assertEquals("用户收藏文章出错",true, articleService.isUserCollectArticle(test_userId, test_articleId));
		
		articleController.userUncollectArticle(test_userId, test_articleId, null);
		Assert.assertEquals("用户取消收藏文章出错",false, articleService.isUserCollectArticle(test_userId, test_articleId));
	}
}
