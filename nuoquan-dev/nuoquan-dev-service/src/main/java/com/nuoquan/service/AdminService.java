package com.nuoquan.service;

import java.util.List;

import com.nuoquan.pojo.AdminUser;
import com.nuoquan.pojo.AdminNotice;
import com.nuoquan.pojo.AdminPermission;
import com.nuoquan.pojo.AdminRole;

/**
 * 后台管理业务接口类
 * @author Jerrio
 */
public interface AdminService {
	/**
	 * 查询后台管理员用户信息
	 * @param username
	 * @return 用户对象
	 */
	public AdminUser queryAdminUserName(String username);
	
	/**
	 * 查询用户角色, 一个用户可能多个角色
	 * @param uid
	 * @return
	 */
	public List<AdminRole> queryAdminUserRoles(String uid);
	
	/**
	 * 查询某角色的权限列表
	 * @param roleId
	 * @return
	 */
	public List<AdminPermission> queryPermissionByRoleId(String roleId);
	
	/**
	 * 获取用户公告
	 * @param adminUser
	 * @param state 阅读状态  0未阅读 1 阅读  -1全部
	 * @return
	 */
	public List<AdminNotice> getUserNotice(AdminUser adminUser, int state);

}