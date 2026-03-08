package com.company.admin.platform.sys.application.service;

import com.company.admin.common.exception.BusinessException;
import com.company.admin.platform.sys.api.dto.AssignMenusRequest;
import com.company.admin.platform.sys.api.dto.CreateMenuRequest;
import com.company.admin.platform.sys.api.dto.UpdateMenuRequest;
import com.company.admin.platform.sys.api.vo.MenuVO;
import com.company.admin.platform.sys.domain.entity.SysMenu;
import com.company.admin.platform.sys.domain.entity.SysMenuRole;
import com.company.admin.platform.sys.infrastructure.dao.SysMenuDao;
import com.company.admin.platform.sys.infrastructure.dao.SysMenuRoleDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuApplicationService {

    private final SysMenuDao sysMenuDao;
    private final SysMenuRoleDao sysMenuRoleDao;

    /**
     * Get menu tree
     */
    public List<MenuVO> getMenuTree() {
        List<SysMenu> allMenus = sysMenuDao.findAll();
        return buildMenuTree(allMenus, null);
    }

    /**
     * Get menu by ID
     */
    public MenuVO getMenuById(String menuId) {
        SysMenu menu = sysMenuDao.findByMenuId(menuId);
        if (menu == null) {
            throw new BusinessException("Menu not found");
        }
        return convertToVO(menu);
    }

    /**
     * Create menu
     */
    @Transactional(rollbackFor = Exception.class)
    public void createMenu(CreateMenuRequest request, String currentUser) {
        SysMenu menu = new SysMenu();
        menu.setMenuName(request.getMenuName());
        menu.setMenuType(request.getMenuType());
        menu.setMenuUrl(request.getMenuUrl());
        menu.setMenuIcon(request.getMenuIcon());
        menu.setPerms(request.getPerms());
        menu.setSortOrder(request.getSortOrder());
        menu.setVisible(request.getVisible());
        menu.setCreatedBy(currentUser);
        menu.setCreatedTime(LocalDateTime.now());
        menu.setDeleted(0);

        // Set parent menu
        if (request.getParentMenuId() != null && !request.getParentMenuId().isEmpty()) {
            SysMenu parentMenu = sysMenuDao.findByMenuId(request.getParentMenuId());
            if (parentMenu == null) {
                throw new BusinessException("Parent menu not found");
            }
            menu.setParentMenuId(request.getParentMenuId());
        } else {
            menu.setParentMenuId(null);
        }

        sysMenuDao.insert(menu);
        log.info("Menu created: {}", menu.getMenuName());
    }

    /**
     * Update menu
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(String menuId, UpdateMenuRequest request, String currentUser) {
        SysMenu menu = sysMenuDao.findByMenuId(menuId);
        if (menu == null) {
            throw new BusinessException("Menu not found");
        }

        menu.setMenuName(request.getMenuName());
        menu.setMenuType(request.getMenuType());
        menu.setMenuUrl(request.getMenuUrl());
        menu.setMenuIcon(request.getMenuIcon());
        menu.setPerms(request.getPerms());
        menu.setSortOrder(request.getSortOrder());
        menu.setVisible(request.getVisible());
        menu.setUpdatedBy(currentUser);
        menu.setUpdatedTime(LocalDateTime.now());

        sysMenuDao.updateById(menu);
        log.info("Menu updated: {}", menu.getMenuName());
    }

    /**
     * Delete menu
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(String menuId) {
        SysMenu menu = sysMenuDao.findByMenuId(menuId);
        if (menu == null) {
            throw new BusinessException("Menu not found");
        }

        // Check if has children
        int childrenCount = sysMenuDao.countByParentMenuId(menuId);
        if (childrenCount > 0) {
            throw new BusinessException("Cannot delete menu with children");
        }

        // Delete menu-role associations
        sysMenuDao.deleteMenuRoles(menuId);

        sysMenuDao.deleteById(menuId);
        log.info("Menu deleted: {}", menu.getMenuName());
    }

    /**
     * Assign menus to role
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignMenusToRole(String roleId, AssignMenusRequest request) {
        // Delete existing associations
        sysMenuRoleDao.deleteByRoleId(roleId);

        // Create new associations
        if (request.getMenuIds() != null && !request.getMenuIds().isEmpty()) {
            List<SysMenuRole> menuRoles = request.getMenuIds().stream()
                    .map(menuId -> {
                        SysMenuRole mr = new SysMenuRole();
                        mr.setMenuId(menuId);
                        mr.setRoleId(roleId);
                        mr.setCreatedTime(LocalDateTime.now());
                        return mr;
                    })
                    .collect(Collectors.toList());
            
            // Insert batch (need to implement in DAO)
            for (SysMenuRole mr : menuRoles) {
                sysMenuRoleDao.insert(mr);
            }
        }
        
        log.info("Menus assigned to role: {}", roleId);
    }

    /**
     * Build menu tree
     */
    private List<MenuVO> buildMenuTree(List<SysMenu> allMenus, String parentId) {
        return allMenus.stream()
                .filter(menu -> {
                    if (parentId == null) {
                        return menu.getParentMenuId() == null || "0".equals(menu.getParentMenuId());
                    }
                    return parentId.equals(menu.getParentMenuId());
                })
                .map(menu -> {
                    MenuVO vo = convertToVO(menu);
                    vo.setChildren(buildMenuTree(allMenus, menu.getMenuId()));
                    return vo;
                })
                .collect(Collectors.toList());
    }

    /**
     * Convert entity to VO
     */
    private MenuVO convertToVO(SysMenu menu) {
        MenuVO vo = new MenuVO();
        vo.setMenuId(menu.getMenuId());
        vo.setMenuName(menu.getMenuName());
        vo.setMenuType(menu.getMenuType());
        vo.setParentMenuId(menu.getParentMenuId());
        vo.setMenuUrl(menu.getMenuUrl());
        vo.setMenuIcon(menu.getMenuIcon());
        vo.setPerms(menu.getPerms());
        vo.setSortOrder(menu.getSortOrder());
        vo.setVisible(menu.getVisible());
        vo.setCreatedTime(menu.getCreatedTime());
        return vo;
    }
}
