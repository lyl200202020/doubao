package com.company.admin.platform.sys.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.admin.platform.sys.domain.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenu> {

    @Delete("DELETE FROM sys_role_menu WHERE role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") String roleId);

    void batchInsert(List<SysRoleMenu> roleMenus);
}
