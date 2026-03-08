package com.company.admin.platform.sys.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_menu")
public class SysMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "menu_id", type = IdType.ASSIGN_UUID)
    private String menuId;

    private String menuName;

    private Integer menuType;

    @TableField(fill = FieldFill.INSERT)
    private String parentMenuId;

    private String menuUrl;

    private String menuIcon;

    private String perms;

    private Integer sortOrder;

    private Integer visible;

    private String createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    private String updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    @TableLogic
    private Integer deleted;
}
