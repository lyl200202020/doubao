package com.company.admin.platform.sys.application.service;

import com.company.admin.common.constant.SecurityConstants;
import com.company.admin.platform.sys.application.command.LoginCommand;
import com.company.admin.platform.sys.domain.entity.SysMenu;
import com.company.admin.platform.sys.domain.entity.SysOrg;
import com.company.admin.platform.sys.domain.entity.SysUser;
import com.company.admin.platform.sys.domain.service.AuthDomainService;
import com.company.admin.platform.sys.api.vo.LoginVO;
import com.company.admin.platform.sys.infrastructure.dao.SysMenuDao;
import com.company.admin.platform.sys.infrastructure.dao.SysOrgDao;
import com.company.admin.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthApplicationService {
    
    private final AuthDomainService authDomainService;
    private final StringRedisTemplate stringRedisTemplate;
    private final SysOrgDao sysOrgDao;
    private final SysMenuDao sysMenuDao;
    private final JwtUtil jwtUtil;
    
    public LoginVO login(LoginCommand command) {
        log.info("Login start: userCode={}", command.getUserCode());
        SysUser user = authDomainService.login(command.getUserCode(), command.getPassword());
        log.info("User found: userId={}, orgId={}", user.getUserId(), user.getOrgId());
        
        String token = jwtUtil.generateToken(
                user.getUserId(),
                user.getUserCode(),
                user.getRealName(),
                user.getOrgId(),
                user.getAuthLevel()
        );
        
        stringRedisTemplate.opsForValue().set(
                SecurityConstants.CURRENT_USER + ":" + user.getUserId(),
                token,
                SecurityConstants.TOKEN_EXPIRE_TIME,
                TimeUnit.MILLISECONDS
        );
        
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserId(user.getUserId());
        loginVO.setUserCode(user.getUserCode());
        loginVO.setUserName(user.getRealName());
        loginVO.setOrgId(user.getOrgId());
        loginVO.setAuthLevel(user.getAuthLevel());
        
        if (user.getOrgId() != null) {
            SysOrg org = sysOrgDao.findByOrgId(user.getOrgId());
            log.info("Query org for user: orgId={}, org={}", user.getOrgId(), org);
            if (org != null) {
                loginVO.setOrgName(org.getOrgName());
            }
        }
        
        List<String> permissions = sysMenuDao.findPermissionsByUserId(user.getUserId());
        log.info("Query permissions for user: userId={}, permissions={}", user.getUserId(), permissions);
        loginVO.setPermissions(permissions);
        
        List<SysMenu> menus = sysMenuDao.findByUserId(user.getUserId());
        List<LoginVO.MenuVO> menuVOList = buildMenuTree(menus);
        loginVO.setMenus(menuVOList);
        
        log.info("User login: userCode={}, token={}", user.getUserCode(), token.substring(0, 20) + "...");
        return loginVO;
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
    
    public void logout(String userId, String token) {
        stringRedisTemplate.delete(SecurityConstants.CURRENT_USER + ":" + userId);
        stringRedisTemplate.opsForValue().set(
                SecurityConstants.TOKEN_BLACKLIST_PREFIX + token,
                "1",
                SecurityConstants.TOKEN_EXPIRE_TIME,
                TimeUnit.MILLISECONDS
        );
        log.info("User logout: userId={}", userId);
    }
    
    public LoginVO getCurrentUser(String userId, String username, String orgId, Integer authLevel) {
        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(userId);
        loginVO.setUserCode(username);
        loginVO.setUserName(username);
        loginVO.setOrgId(orgId);
        loginVO.setAuthLevel(authLevel);
        
        List<String> permissions = sysMenuDao.findPermissionsByUserId(userId);
        loginVO.setPermissions(permissions);
        
        List<SysMenu> menus = sysMenuDao.findByUserId(userId);
        List<LoginVO.MenuVO> menuVOList = buildMenuTree(menus);
        loginVO.setMenus(menuVOList);
        
        return loginVO;
    }
    
    public LoginVO refresh(String token) {
        Claims claims = jwtUtil.parseToken(token);
        String userId = claims.get(JwtUtil.CLAIM_USER_ID, String.class);
        String username = claims.get(JwtUtil.CLAIM_USER_NAME, String.class);
        String orgId = claims.get(JwtUtil.CLAIM_ORG_ID, String.class);
        Integer authLevel = claims.get(JwtUtil.CLAIM_AUTH_LEVEL, Integer.class);
        if (authLevel == null) {
            authLevel = 0;
        }
        
        String newToken = jwtUtil.generateToken(userId, username, username, orgId, authLevel);
        
        stringRedisTemplate.opsForValue().set(
                SecurityConstants.CURRENT_USER + ":" + userId,
                newToken,
                SecurityConstants.TOKEN_EXPIRE_TIME,
                TimeUnit.MILLISECONDS
        );
        
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(newToken);
        loginVO.setUserId(userId);
        loginVO.setUserCode(username);
        loginVO.setUserName(username);
        loginVO.setOrgId(orgId);
        loginVO.setAuthLevel(authLevel);
        
        List<String> permissions = sysMenuDao.findPermissionsByUserId(userId);
        loginVO.setPermissions(permissions);
        
        List<SysMenu> menus = sysMenuDao.findByUserId(userId);
        List<LoginVO.MenuVO> menuVOList = buildMenuTree(menus);
        loginVO.setMenus(menuVOList);
        
        log.info("User refresh token: userCode={}", username);
        return loginVO;
    }
}
