package com.company.admin.platform.sys.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_user_role")
public class SysUserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "role_id")
    private String roleId;
}
