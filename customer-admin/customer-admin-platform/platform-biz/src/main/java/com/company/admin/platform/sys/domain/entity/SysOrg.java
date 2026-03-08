package com.company.admin.platform.sys.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("sys_org")
public class SysOrg implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "org_id", type = IdType.ASSIGN_UUID)
    private String orgId;

    private String orgCode;

    private String orgName;

    private Integer orgLevel;

    @TableField(fill = FieldFill.INSERT)
    private String parentOrgId;

    private Integer orgStatus;

    private Integer sortOrder;

    private String remark;

    private String createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    private String updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private List<SysOrg> children;
}
