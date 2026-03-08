package com.company.admin.platform.sys.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.admin.platform.sys.domain.entity.SysOrg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SysOrgDao extends BaseMapper<SysOrg> {
    
    @Select("SELECT * FROM sys_org WHERE deleted = 0 ORDER BY org_level, sort_order")
    List<SysOrg> findAll();
    
    @Select("SELECT * FROM sys_org WHERE org_id = #{orgId} AND deleted = 0")
    SysOrg findByOrgId(@Param("orgId") String orgId);
    
    @Select("SELECT * FROM sys_org WHERE parent_org_id = #{parentOrgId} AND deleted = 0 ORDER BY sort_order")
    List<SysOrg> findByParentOrgId(@Param("parentOrgId") String parentOrgId);
    
    @Select("SELECT COUNT(*) FROM sys_org WHERE parent_org_id = #{orgId} AND deleted = 0")
    int countByParentOrgId(@Param("orgId") String orgId);
    
    @Select("SELECT COUNT(*) FROM sys_user WHERE org_id = #{orgId} AND deleted = 0")
    int countUsersByOrgId(@Param("orgId") String orgId);
    
    @Update("UPDATE sys_org SET org_status = #{status} WHERE org_id = #{orgId}")
    int updateStatus(@Param("orgId") String orgId, @Param("status") Integer status);
    
    @Select("SELECT * FROM sys_org WHERE org_code = #{orgCode} AND deleted = 0 LIMIT 1")
    SysOrg findByOrgCode(@Param("orgCode") String orgCode);
    
    @Select("SELECT COUNT(*) FROM sys_org WHERE org_code = #{orgCode} AND deleted = 0 AND org_id != #{orgId}")
    int countByOrgCodeExcludeId(@Param("orgId") String orgId, @Param("orgCode") String orgCode);
}
