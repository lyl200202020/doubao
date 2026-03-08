package com.company.admin.platform.sys.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.admin.platform.sys.domain.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

@Mapper
public interface SysMenuDao extends BaseMapper<SysMenu> {
    
    @Select("SELECT * FROM sys_menu WHERE deleted = 0 ORDER BY sort_order")
    List<SysMenu> findAll();
    
    @Select("SELECT * FROM sys_menu WHERE menu_id = #{menuId} AND deleted = 0")
    SysMenu findByMenuId(@Param("menuId") String menuId);
    
    @Select("SELECT * FROM sys_menu WHERE parent_menu_id = #{parentMenuId} AND deleted = 0 ORDER BY sort_order")
    List<SysMenu> findByParentMenuId(@Param("parentMenuId") String parentMenuId);
    
    @Select("SELECT COUNT(*) FROM sys_menu WHERE parent_menu_id = #{menuId} AND deleted = 0")
    int countByParentMenuId(@Param("menuId") String menuId);
    
    @Select("SELECT m.* FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.menu_id = rm.menu_id " +
            "WHERE rm.role_id = #{roleId} AND m.deleted = 0 " +
            "ORDER BY m.sort_order")
    List<SysMenu> findByRoleId(@Param("roleId") String roleId);
    
    @Select("SELECT DISTINCT m.* FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.menu_id = rm.menu_id " +
            "INNER JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.deleted = 0 " +
            "ORDER BY m.sort_order")
    List<SysMenu> findByUserId(@Param("userId") String userId);
    
    @Select("SELECT DISTINCT m.perms FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.menu_id = rm.menu_id " +
            "INNER JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.deleted = 0 AND m.perms IS NOT NULL AND m.perms != ''")
    List<String> findPermissionsByUserId(@Param("userId") String userId);
    
    @Delete("DELETE FROM sys_role_menu WHERE menu_id = #{menuId}")
    void deleteMenuRoles(@Param("menuId") String menuId);
}
