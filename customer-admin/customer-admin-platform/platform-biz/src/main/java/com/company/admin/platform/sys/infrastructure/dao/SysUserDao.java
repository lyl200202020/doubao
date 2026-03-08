package com.company.admin.platform.sys.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.admin.platform.sys.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SysUserDao extends BaseMapper<SysUser> {
    
    @Select("SELECT * FROM sys_user WHERE user_code = #{userCode} AND deleted = 0 LIMIT 1")
    SysUser findByUserCode(@Param("userCode") String userCode);
    
    @Select("SELECT * FROM sys_user WHERE org_id = #{orgId} AND deleted = 0")
    List<SysUser> findByOrgId(@Param("orgId") String orgId);
    
    @Select("SELECT COUNT(*) FROM sys_user WHERE org_id = #{orgId} AND deleted = 0")
    int countByOrgId(@Param("orgId") String orgId);
    
    @Update("UPDATE sys_user SET user_status = #{status} WHERE user_id = #{userId}")
    int updateStatus(@Param("userId") String userId, @Param("status") Integer status);
    
    @Update("UPDATE sys_user SET password = #{password} WHERE user_id = #{userId}")
    int updatePassword(@Param("userId") String userId, @Param("password") String password);
    
    @Update("UPDATE sys_user SET last_login_time = #{loginTime} WHERE user_id = #{userId}")
    int updateLastLoginTime(@Param("userId") String userId, @Param("loginTime") java.time.LocalDateTime loginTime);
    
    Page<SysUser> pageByCondition(
        Page<SysUser> page,
        @Param("orgId") String orgId,
        @Param("roleId") String roleId,
        @Param("userStatus") Integer userStatus,
        @Param("userName") String userName
    );
}
