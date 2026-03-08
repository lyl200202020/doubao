package com.company.admin.platform.sys.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.admin.platform.sys.domain.entity.SysUser;
import com.company.admin.platform.sys.domain.repository.SysUserRepository;
import com.company.admin.platform.sys.infrastructure.dao.SysUserDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysUserRepositoryImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserRepository {
    
    @Override
    public SysUser findById(String userId) {
        return this.getById(userId);
    }
    
    @Override
    public SysUser findByUserCode(String userCode) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserCode, userCode);
        return this.getOne(wrapper);
    }
    
    @Override
    public List<SysUser> findByOrgId(String orgId) {
        return baseMapper.findByOrgId(orgId);
    }
    
    @Override
    public List<SysUser> findByIds(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUser::getUserId, userIds);
        return this.list(wrapper);
    }
    
    @Override
    public boolean save(SysUser user) {
        return this.save(user);
    }
    
    @Override
    public boolean update(SysUser user) {
        return this.updateById(user);
    }
    
    @Override
    public boolean deleteById(String userId) {
        return this.removeById(userId);
    }
    
    @Override
    public long count() {
        return this.count();
    }
}
