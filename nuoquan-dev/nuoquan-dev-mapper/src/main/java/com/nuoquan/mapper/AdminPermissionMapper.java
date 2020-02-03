package com.nuoquan.mapper;

import java.util.List;

import com.nuoquan.pojo.AdminPermission;
import com.nuoquan.utils.MyMapper;

public interface AdminPermissionMapper extends MyMapper<AdminPermission> {
	
	public List<AdminPermission> findByAdminUserId(String userId);
	
	public List<AdminPermission> queryPermissionByRoleId(String roleId);
}