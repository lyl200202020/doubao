package com.company.admin.platform.sys.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.admin.platform.sys.domain.entity.SysMenuRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

@Mapper
public interface SysMenuRoleDao extends BaseMapper<SysMenuRole> {
    
    @Delete("DELETE FROM sys_menu_role WHERE menu_id = #{menuId}")
    void deleteByMenuId(@Param("menuId") String menuId);
    
    @Delete("DELETE FROM sys_menu_role WHERE role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") String roleId);
    
    void insertBatch(@Param("list") List<SysMenuRole> list);
}
