package com.nuoquan.admin.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Basic;
import com.github.pagehelper.PageInfo;
import com.nuoquan.controller.BasicController;
import com.nuoquan.mapper.AdminRoleMapper;
import com.nuoquan.pojo.AdminRole;
import com.nuoquan.pojo.AdminUser;
import com.nuoquan.pojo.admin.Tablepar;
import com.nuoquan.pojo.vo.AdminRoleVo;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.pojo.vo.TitleVo;
import com.nuoquan.service.AdminRoleService;
import com.nuoquan.service.AdminUserService;
import com.nuoquan.service.ArticleService;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.PagedResult;

import io.swagger.annotations.Api;

/**
 * 文章人工审核
 * 
 * @ClassName: UserController
 * @author fuce
 * @date 2019-11-20 22:35
 */
@Api(value = "文章人工审核")
@Controller
@RequestMapping("/CheckArticle")
public class CheckArticleController extends BasicController {

	private String prefix = "admin/article";
	
	@Autowired
	private ArticleService articleService;

	/**
	 * 展示跳转页面
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/view")
//	@RequiresPermissions("system:user:view")
	public String view(ModelMap model) {
		String str = "审核";
		setTitle(model, new TitleVo("列表", str + "管理", true, "欢迎进入" + str + "页面", true, false));
		return prefix + "/list";
	}

	/**
	 * list集合
	 * 
	 * @param tablepar
	 * @param searchText
	 * @return
	 * @author fuce
	 * @Date 2019年11月11日 下午4:14:40
	 */
	@PostMapping("/list")
	@RequiresPermissions("system:user:list")
	@ResponseBody
	public Object list(Tablepar tablepar, String searchText) {
		PagedResult result = articleService.list(tablepar.getPageNum(), tablepar.getPageSize());
		return result;
	}
	
	@PostMapping("/listCheckOnly")
	@RequiresPermissions("system:user:list")
	@ResponseBody
	public Object listCheckOnly(Tablepar tablepar, String searchText) {	
		PagedResult result = articleService.listCheckOnly(tablepar.getPageNum(), tablepar.getPageSize());
		return result;
	}

	/**
	 * 修改文章状态
	 * 
	 * @param id 文章id
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		ArticleVO article = articleService.getArticleById(id, null);
		mmap.put("Article", article);
		return prefix + "/edit";
	}
	
	/**
	 * 修改文章状态
	 * 
	 * @param id 文章id
	 * @param mmap
	 * @return
	 */
	@GetMapping("/batchEdit/{ids}")
	public String batchEdit(@PathVariable("ids") String ids, ModelMap mmap) {
//		ArticleVO article = articleService.getArticleById(id, null);
//		mmap.put("Article", article);
		return prefix + "/batchEdit";
	}
	
	/**
	 * 修改保存文章状态
	 */
	@RequiresPermissions("system:user:edit")
	@PostMapping("/edit")
	@ResponseBody
	public JSONResult editSave(String id, Integer status) {
		return articleService.updateArticleStatus(id, status) > 0 ? JSONResult.ok() : JSONResult.errorMsg("保存失败");
	}

}
