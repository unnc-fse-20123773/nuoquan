package com.nuoquan.mapper;

import java.util.List;

import com.nuoquan.pojo.AdminRole;
import com.nuoquan.utils.MyMapper;

public interface AdminRoleMapper extends MyMapper<AdminRole> {
	
	public List<AdminRole> queryAdminUserRoles(String uid);
}