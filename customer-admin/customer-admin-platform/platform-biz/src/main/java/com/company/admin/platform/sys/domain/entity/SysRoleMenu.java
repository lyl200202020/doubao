package com.company.admin.platform.sys.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField(value = "role_id")
    private String roleId;

    @TableField(value = "menu_id")
    private String menuId;
}
