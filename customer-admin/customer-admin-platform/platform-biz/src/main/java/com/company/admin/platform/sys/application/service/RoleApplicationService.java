package com.company.admin.platform.sys.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.admin.common.exception.BusinessException;
import com.company.admin.common.result.PageResult;
import com.company.admin.platform.sys.api.dto.CreateRoleRequest;
import com.company.admin.platform.sys.api.dto.UpdateRoleRequest;
import com.company.admin.platform.sys.api.vo.RoleVO;
import com.company.admin.platform.sys.domain.entity.SysMenu;
import com.company.admin.platform.sys.domain.entity.SysRoleMenu;
import com.company.admin.platform.sys.domain.entity.SysRole;
import com.company.admin.platform.sys.infrastructure.dao.SysMenuDao;
import com.company.admin.platform.sys.infrastructure.dao.SysRoleDao;
import com.company.admin.platform.sys.infrastructure.dao.SysRoleMenuDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleApplicationService {

    private final SysRoleDao sysRoleDao;
    private final SysMenuDao sysMenuDao;
    private final SysRoleMenuDao sysRoleMenuDao;

    public PageResult<RoleVO> pageRoles(Integer roleStatus, String roleName, Integer pageNum, Integer pageSize) {
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        Page<SysRole> rolePage = sysRoleDao.pageByCondition(page, roleStatus, roleName);
        
        List<RoleVO> roleVOList = rolePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return PageResult.of(roleVOList, rolePage.getTotal(), rolePage.getCurrent(), rolePage.getSize());
    }

    public List<RoleVO> getAllRoles() {
        List<SysRole> roles = sysRoleDao.findAll();
        return roles.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    public RoleVO getRoleById(String roleId) {
        SysRole role = sysRoleDao.findByRoleId(roleId);
        if (role == null) {
            throw new BusinessException("Role not found");
        }
        RoleVO vo = convertToVO(role);
        
        List<SysMenu> menus = sysMenuDao.findByRoleId(roleId);
        vo.setMenuIds(menus.stream().map(SysMenu::getMenuId).collect(Collectors.toList()));
        
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createRole(CreateRoleRequest request, String currentUser) {
        SysRole role = new SysRole();
        role.setRoleCode(request.getRoleCode());
        role.setRoleName(request.getRoleName());
        role.setRoleDesc(request.getRoleDesc());
        role.setRoleStatus(1);
        role.setCreatedBy(currentUser);
        role.setCreatedTime(LocalDateTime.now());
        role.setDeleted(0);

        sysRoleDao.insert(role);
        log.info("Role created: {}", role.getRoleCode());
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRole(String roleId, UpdateRoleRequest request, String currentUser) {
        SysRole role = sysRoleDao.findByRoleId(roleId);
        if (role == null) {
            throw new BusinessException("Role not found");
        }

        role.setRoleCode(request.getRoleCode());
        role.setRoleName(request.getRoleName());
        role.setRoleDesc(request.getRoleDesc());
        role.setUpdatedBy(currentUser);
        role.setUpdatedTime(LocalDateTime.now());

        sysRoleDao.updateById(role);
        log.info("Role updated: {}", role.getRoleCode());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(String roleId) {
        SysRole role = sysRoleDao.findByRoleId(roleId);
        if (role == null) {
            throw new BusinessException("Role not found");
        }

        int usersCount = sysRoleDao.countUsersByRoleId(roleId);
        if (usersCount > 0) {
            throw new BusinessException("Cannot delete role with users");
        }

        sysRoleDao.deleteById(roleId);
        log.info("Role deleted: {}", role.getRoleCode());
    }

    @Transactional(rollbackFor = Exception.class)
    public void toggleRoleStatus(String roleId) {
        SysRole role = sysRoleDao.findByRoleId(roleId);
        if (role == null) {
            throw new BusinessException("Role not found");
        }

        int newStatus = role.getRoleStatus() == 1 ? 0 : 1;
        role.setRoleStatus(newStatus);
        role.setUpdatedTime(LocalDateTime.now());

        sysRoleDao.updateById(role);
        log.info("Role status toggled: {} -> {}", role.getRoleCode(), newStatus);
    }

    @Transactional(rollbackFor = Exception.class)
    public void assignMenus(String roleId, List<String> menuIds) {
        SysRole role = sysRoleDao.findByRoleId(roleId);
        if (role == null) {
            throw new BusinessException("Role not found");
        }

        sysRoleMenuDao.deleteByRoleId(roleId);

        if (menuIds != null && !menuIds.isEmpty()) {
            List<SysRoleMenu> roleMenus = menuIds.stream()
                    .map(menuId -> {
                        SysRoleMenu roleMenu = new SysRoleMenu();
                        roleMenu.setRoleId(roleId);
                        roleMenu.setMenuId(menuId);
                        return roleMenu;
                    })
                    .collect(Collectors.toList());

            sysRoleMenuDao.batchInsert(roleMenus);
        }

        log.info("Role menus assigned: {} -> {}", role.getRoleCode(), menuIds);
    }

    public List<String> getRoleMenus(String roleId) {
        SysRole role = sysRoleDao.findByRoleId(roleId);
        if (role == null) {
            throw new BusinessException("Role not found");
        }

        List<SysMenu> menus = sysMenuDao.findByRoleId(roleId);
        return menus.stream()
                .map(SysMenu::getMenuId)
                .collect(Collectors.toList());
    }

    private RoleVO convertToVO(SysRole role) {
        RoleVO vo = new RoleVO();
        vo.setRoleId(role.getRoleId());
        vo.setRoleCode(role.getRoleCode());
        vo.setRoleName(role.getRoleName());
        vo.setRoleDesc(role.getRoleDesc());
        vo.setRoleStatus(role.getRoleStatus());
        vo.setCreatedTime(role.getCreatedTime());
        return vo;
    }
}
