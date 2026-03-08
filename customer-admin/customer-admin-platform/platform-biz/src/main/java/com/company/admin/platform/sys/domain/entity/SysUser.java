package com.company.admin.platform.sys.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    private String userId;

    @TableField("user_code")
    private String userCode;

    @TableField("username")
    private String username;

    @TableField("real_name")
    private String realName;

    private String password;

    private String mobile;

    private String email;

    @TableField("org_id")
    private String orgId;

    @TableField("role_id")
    private String roleId;

    @TableField("auth_level")
    private Integer authLevel;

    @TableField("user_status")
    private Integer userStatus;

    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
