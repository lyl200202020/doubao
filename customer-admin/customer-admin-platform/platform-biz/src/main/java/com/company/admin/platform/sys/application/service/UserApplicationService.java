package com.company.admin.platform.sys.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.admin.common.exception.BusinessException;
import com.company.admin.common.util.SM3Util;
import com.company.admin.platform.sys.api.dto.CreateUserRequest;
import com.company.admin.platform.sys.api.dto.UpdateUserRequest;
import com.company.admin.platform.sys.api.vo.CurrentUserVO;
import com.company.admin.platform.sys.api.vo.LoginVO;
import com.company.admin.platform.sys.api.vo.UserVO;
import com.company.admin.platform.sys.domain.entity.SysMenu;
import com.company.admin.platform.sys.domain.entity.SysOrg;
import com.company.admin.platform.sys.domain.entity.SysRole;
import com.company.admin.platform.sys.domain.entity.SysUser;
import com.company.admin.platform.sys.infrastructure.dao.SysMenuDao;
import com.company.admin.platform.sys.infrastructure.dao.SysOrgDao;
import com.company.admin.platform.sys.infrastructure.dao.SysRoleDao;
import com.company.admin.platform.sys.infrastructure.dao.SysUserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final SysUserDao sysUserDao;
    private final SysOrgDao sysOrgDao;
    private final SysRoleDao sysRoleDao;
    private final SysMenuDao sysMenuDao;

    private static final String DEFAULT_PASSWORD = "123456";

    /**
     * Page query users
     */
    public Page<UserVO> pageUsers(String orgId, String roleId, Integer userStatus, String userName, Integer pageNum, Integer pageSize) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        Page<SysUser> userPage = sysUserDao.pageByCondition(page, orgId, roleId, userStatus, userName);
        
        List<UserVO> userVOList = userPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        Page<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        voPage.setRecords(userVOList);
        return voPage;
    }

    /**
     * Get user by ID
     */
    public UserVO getUserById(String userId) {
        SysUser user = sysUserDao.selectById(userId);
        if (user == null) {
            throw new BusinessException("User not found");
        }
        return convertToVO(user);
    }

    /**
     * Get user by userCode
     */
    public SysUser getUserByUserCode(String userCode) {
        return sysUserDao.findByUserCode(userCode);
    }

    /**
     * Create user
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUser(CreateUserRequest request, String currentUser) {
        // Check if userCode is unique
        SysUser existingUser = sysUserDao.findByUserCode(request.getUserCode());
        if (existingUser != null) {
            throw new BusinessException("UserCode already exists: " + request.getUserCode());
        }

        // Validate organization
        SysOrg org = sysOrgDao.findByOrgId(request.getOrgId());
        if (org == null) {
            throw new BusinessException("Organization not found");
        }

        SysUser user = new SysUser();
        user.setUserCode(request.getUserCode());
        user.setUsername(request.getUserCode());
        user.setRealName(request.getUserName());
        user.setPassword(SM3Util.hash(DEFAULT_PASSWORD));
        user.setOrgId(request.getOrgId());
        user.setAuthLevel(request.getAuthLevel());
        user.setUserStatus(1);
        user.setCreateTime(LocalDateTime.now());

        sysUserDao.insert(user);
        log.info("User created: {}", user.getUserCode());
    }

    /**
     * Update user
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(String userId, UpdateUserRequest request, String currentUser) {
        SysUser user = sysUserDao.selectById(userId);
        if (user == null) {
            throw new BusinessException("User not found");
        }

        // Validate organization
        SysOrg org = sysOrgDao.findByOrgId(request.getOrgId());
        if (org == null) {
            throw new BusinessException("Organization not found");
        }

        user.setRealName(request.getUserName());
        user.setOrgId(request.getOrgId());
        user.setAuthLevel(request.getAuthLevel());
        user.setUpdateTime(LocalDateTime.now());

        sysUserDao.updateById(user);
        log.info("User updated: {}", user.getUserCode());
    }

    /**
     * Delete user
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(String userId) {
        SysUser user = sysUserDao.selectById(userId);
        if (user == null) {
            throw new BusinessException("User not found");
        }

        sysUserDao.deleteById(userId);
        log.info("User deleted: {}", user.getUserCode());
    }

    /**
     * Reset user password
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String userId) {
        SysUser user = sysUserDao.selectById(userId);
        if (user == null) {
            throw new BusinessException("User not found");
        }

        String encryptedPassword = SM3Util.hash(DEFAULT_PASSWORD);
        sysUserDao.updatePassword(userId, encryptedPassword);
        log.info("User password reset: {}", user.getUserCode());
    }

    /**
     * Update user status
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(String userId, Integer status) {
        SysUser user = sysUserDao.selectById(userId);
        if (user == null) {
            throw new BusinessException("User not found");
        }

        sysUserDao.updateStatus(userId, status);
        log.info("User status updated: {} -> {}", user.getUserCode(), status);
    }

    /**
     * Get current user info
     */
    public CurrentUserVO getCurrentUser(String userId) {
        SysUser user = sysUserDao.selectById(userId);
        if (user == null) {
            return null;
        }

        CurrentUserVO vo = new CurrentUserVO();
        vo.setUserId(user.getUserId());
        vo.setUserCode(user.getUserCode());
        vo.setUserName(user.getRealName());
        vo.setOrgId(user.getOrgId());
        vo.setAuthLevel(user.getAuthLevel());

        // Load organization name
        if (user.getOrgId() != null) {
            SysOrg org = sysOrgDao.findByOrgId(user.getOrgId());
            if (org != null) {
                vo.setOrgName(org.getOrgName());
            }
        }

        // Load roles
        List<SysRole> roles = sysRoleDao.findByUserId(userId);
        vo.setRoles(roles.stream().map(SysRole::getRoleCode).collect(Collectors.toList()));

        // Load permissions
        List<String> permissions = sysMenuDao.findPermissionsByUserId(userId);
        vo.setPermissions(permissions);
        
        // Load menus
        List<SysMenu> menus = sysMenuDao.findByUserId(userId);
        List<LoginVO.MenuVO> menuVOList = buildMenuTree(menus);
        vo.setMenus(menuVOList);

        return vo;
    }
    
    private List<LoginVO.MenuVO> buildMenuTree(List<SysMenu> menus) {
        if (menus == null || menus.isEmpty()) {
            return new ArrayList<>();
        }
        
        Map<String, List<SysMenu>> menuMap = menus.stream()
                .collect(Collectors.groupingBy(m -> 
                    m.getParentMenuId() == null || "0".equals(m.getParentMenuId()) ? "root" : m.getParentMenuId()));
        
        List<SysMenu> rootMenus = menuMap.getOrDefault("root", new ArrayList<>());
        
        return rootMenus.stream()
                .map(menu -> convertToMenuVO(menu, menuMap))
                .collect(Collectors.toList());
    }
    
    private LoginVO.MenuVO convertToMenuVO(SysMenu menu, Map<String, List<SysMenu>> menuMap) {
        LoginVO.MenuVO menuVO = new LoginVO.MenuVO();
        menuVO.setMenuId(menu.getMenuId());
        menuVO.setMenuName(menu.getMenuName());
        menuVO.setMenuType(menu.getMenuType());
        menuVO.setParentMenuId(menu.getParentMenuId());
        menuVO.setMenuUrl(menu.getMenuUrl());
        menuVO.setMenuIcon(menu.getMenuIcon());
        menuVO.setPerms(menu.getPerms());
        menuVO.setSortOrder(menu.getSortOrder());
        menuVO.setVisible(menu.getVisible());
        
        List<SysMenu> children = menuMap.getOrDefault(menu.getMenuId(), new ArrayList<>());
        if (!children.isEmpty()) {
            menuVO.setChildren(children.stream()
                    .map(child -> convertToMenuVO(child, menuMap))
                    .collect(Collectors.toList()));
        }
        
        return menuVO;
    }

    /**
     * Convert entity to VO
     */
    private UserVO convertToVO(SysUser user) {
        UserVO vo = new UserVO();
        vo.setUserId(user.getUserId());
        vo.setUserCode(user.getUserCode());
        vo.setUserName(user.getRealName());
        vo.setOrgId(user.getOrgId());
        vo.setAuthLevel(user.getAuthLevel());
        vo.setUserStatus(user.getUserStatus());
        vo.setCreateTime(user.getCreateTime());

        // Load organization name
        if (user.getOrgId() != null) {
            SysOrg org = sysOrgDao.findByOrgId(user.getOrgId());
            if (org != null) {
                vo.setOrgName(org.getOrgName());
            }
        }

        return vo;
    }
}
