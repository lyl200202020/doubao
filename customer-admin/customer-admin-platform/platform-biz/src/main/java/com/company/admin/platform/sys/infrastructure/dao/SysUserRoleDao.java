package com.company.admin.platform.sys.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.admin.platform.sys.domain.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserRoleDao extends BaseMapper<SysUserRole> {
    
    @Select("SELECT role_id FROM sys_user_role WHERE user_id = #{userId}")
    List<String> findRoleIdsByUserId(@Param("userId") String userId);
}
