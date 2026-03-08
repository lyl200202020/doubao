package com.company.admin.platform.sys.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.admin.platform.sys.domain.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

@Mapper
public interface SysRoleDao extends BaseMapper<SysRole> {
    
    @Select("SELECT * FROM sys_role WHERE deleted = 0 ORDER BY role_code")
    List<SysRole> findAll();
    
    @Select("SELECT * FROM sys_role WHERE role_id = #{roleId} AND deleted = 0")
    SysRole findByRoleId(@Param("roleId") String roleId);
    
    @Select("SELECT r.* FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.deleted = 0")
    List<SysRole> findByUserId(@Param("userId") String userId);
    
    @Select("SELECT COUNT(*) FROM sys_user_role WHERE role_id = #{roleId}")
    int countUsersByRoleId(@Param("roleId") String roleId);
    
    @Delete("DELETE FROM sys_user_role WHERE role_id = #{roleId}")
    void deleteUserRoles(@Param("roleId") String roleId);
    
    Page<SysRole> pageByCondition(Page<SysRole> page, @Param("roleStatus") Integer roleStatus, @Param("roleName") String roleName);
}
