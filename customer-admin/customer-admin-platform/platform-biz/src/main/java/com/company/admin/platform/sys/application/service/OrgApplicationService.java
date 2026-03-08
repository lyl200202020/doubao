package com.company.admin.platform.sys.application.service;

import com.company.admin.common.exception.BusinessException;
import com.company.admin.platform.sys.api.dto.CreateOrgRequest;
import com.company.admin.platform.sys.api.dto.UpdateOrgRequest;
import com.company.admin.platform.sys.api.vo.OrgVO;
import com.company.admin.platform.sys.domain.entity.SysOrg;
import com.company.admin.platform.sys.infrastructure.dao.SysOrgDao;
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
public class OrgApplicationService {

    private final SysOrgDao sysOrgDao;

    /**
     * Get organization tree
     */
    public List<OrgVO> getOrgTree() {
        List<SysOrg> allOrgs = sysOrgDao.findAll();
        return buildOrgTree(allOrgs, null);
    }

    /**
     * Get organization by ID
     */
    public OrgVO getOrgById(String orgId) {
        SysOrg org = sysOrgDao.findByOrgId(orgId);
        if (org == null) {
            throw new BusinessException("Organization not found");
        }
        return convertToVO(org);
    }

    /**
     * Create organization
     */
    @Transactional(rollbackFor = Exception.class)
    public void createOrg(CreateOrgRequest request, String currentUser) {
        // Check if org code is unique
        SysOrg existingOrg = sysOrgDao.findByOrgCode(request.getOrgCode());
        if (existingOrg != null) {
            throw new BusinessException("Organization code already exists: " + request.getOrgCode());
        }

        SysOrg org = new SysOrg();
        org.setOrgCode(request.getOrgCode());
        org.setOrgName(request.getOrgName());
        org.setSortOrder(request.getSortOrder());
        org.setRemark(request.getRemark());
        org.setCreatedBy(currentUser);
        org.setCreatedTime(LocalDateTime.now());
        org.setDeleted(0);

        // Set parent organization and level
        if (request.getParentOrgId() != null && !request.getParentOrgId().isEmpty()) {
            SysOrg parentOrg = sysOrgDao.findByOrgId(request.getParentOrgId());
            if (parentOrg == null) {
                throw new BusinessException("Parent organization not found");
            }
            if (parentOrg.getOrgStatus() == 0) {
                throw new BusinessException("Parent organization is disabled");
            }
            org.setParentOrgId(request.getParentOrgId());
            org.setOrgLevel(parentOrg.getOrgLevel() + 1);
        } else {
            org.setParentOrgId(null);
            org.setOrgLevel(0);
        }

        sysOrgDao.insert(org);
        log.info("Organization created: {}", org.getOrgCode());
    }

    /**
     * Update organization
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrg(String orgId, UpdateOrgRequest request, String currentUser) {
        SysOrg org = sysOrgDao.findByOrgId(orgId);
        if (org == null) {
            throw new BusinessException("Organization not found");
        }

        // Check if org code is unique (excluding current org)
        int count = sysOrgDao.countByOrgCodeExcludeId(orgId, request.getOrgCode());
        if (count > 0) {
            throw new BusinessException("Organization code already exists: " + request.getOrgCode());
        }

        org.setOrgCode(request.getOrgCode());
        org.setOrgName(request.getOrgName());
        org.setSortOrder(request.getSortOrder());
        org.setRemark(request.getRemark());
        org.setUpdatedBy(currentUser);
        org.setUpdatedTime(LocalDateTime.now());

        sysOrgDao.updateById(org);
        log.info("Organization updated: {}", org.getOrgCode());
    }

    /**
     * Delete organization
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrg(String orgId) {
        SysOrg org = sysOrgDao.findByOrgId(orgId);
        if (org == null) {
            throw new BusinessException("Organization not found");
        }

        // Check if has children
        int childrenCount = sysOrgDao.countByParentOrgId(orgId);
        if (childrenCount > 0) {
            throw new BusinessException("Cannot delete organization with children");
        }

        // Check if has users
        int usersCount = sysOrgDao.countUsersByOrgId(orgId);
        if (usersCount > 0) {
            throw new BusinessException("Cannot delete organization with users");
        }

        sysOrgDao.deleteById(orgId);
        log.info("Organization deleted: {}", org.getOrgCode());
    }

    /**
     * Update organization status
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrgStatus(String orgId, Integer status, String currentUser) {
        SysOrg org = sysOrgDao.findByOrgId(orgId);
        if (org == null) {
            throw new BusinessException("Organization not found");
        }

        sysOrgDao.updateStatus(orgId, status);

        // If disabling, cascade disable children
        if (status == 0) {
            cascadeDisableChildren(orgId);
        }

        log.info("Organization status updated: {} -> {}", org.getOrgCode(), status);
    }

    /**
     * Cascade disable children organizations
     */
    private void cascadeDisableChildren(String parentOrgId) {
        List<SysOrg> children = sysOrgDao.findByParentOrgId(parentOrgId);
        for (SysOrg child : children) {
            sysOrgDao.updateStatus(child.getOrgId(), 0);
            cascadeDisableChildren(child.getOrgId());
        }
    }

    /**
     * Build organization tree
     */
    private List<OrgVO> buildOrgTree(List<SysOrg> allOrgs, String parentId) {
        return allOrgs.stream()
                .filter(org -> {
                    if (parentId == null) {
                        return org.getParentOrgId() == null || "0".equals(org.getParentOrgId());
                    }
                    return parentId.equals(org.getParentOrgId());
                })
                .map(org -> {
                    OrgVO vo = convertToVO(org);
                    vo.setChildren(buildOrgTree(allOrgs, org.getOrgId()));
                    return vo;
                })
                .collect(Collectors.toList());
    }

    /**
     * Convert entity to VO
     */
    private OrgVO convertToVO(SysOrg org) {
        OrgVO vo = new OrgVO();
        vo.setOrgId(org.getOrgId());
        vo.setOrgCode(org.getOrgCode());
        vo.setOrgName(org.getOrgName());
        vo.setOrgLevel(org.getOrgLevel());
        vo.setParentOrgId(org.getParentOrgId());
        vo.setOrgStatus(org.getOrgStatus());
        vo.setSortOrder(org.getSortOrder());
        vo.setRemark(org.getRemark());
        vo.setCreatedBy(org.getCreatedBy());
        vo.setCreatedTime(org.getCreatedTime());
        vo.setUpdatedBy(org.getUpdatedBy());
        vo.setUpdatedTime(org.getUpdatedTime());
        return vo;
    }
}
