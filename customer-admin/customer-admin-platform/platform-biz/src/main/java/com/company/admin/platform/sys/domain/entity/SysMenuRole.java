package com.company.admin.platform.sys.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_menu_role")
public class SysMenuRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String menuId;

    private String roleId;

    private LocalDateTime createdTime;
}
