package com.company.admin.platform.sys.domain.repository;

import com.company.admin.platform.sys.domain.entity.SysUser;

import java.util.List;

public interface SysUserRepository {
    SysUser findById(String userId);
    
    SysUser findByUserCode(String userCode);
    
    List<SysUser> findByOrgId(String orgId);
    
    List<SysUser> findByIds(List<String> userIds);
    
    boolean save(SysUser user);
    
    boolean update(SysUser user);
    
    boolean deleteById(String userId);
    
    long count();
}
