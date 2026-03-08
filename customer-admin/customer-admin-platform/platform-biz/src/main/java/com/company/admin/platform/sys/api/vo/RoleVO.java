package com.company.admin.platform.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Role VO")
public class RoleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Role ID")
    private String roleId;

    @Schema(description = "Role Code")
    private String roleCode;

    @Schema(description = "Role Name")
    private String roleName;

    @Schema(description = "Role Description")
    private String roleDesc;

    @Schema(description = "Status (0-Disabled, 1-Enabled)")
    private Integer roleStatus;

    @Schema(description = "Created Time")
    private LocalDateTime createdTime;

    @Schema(description = "Menu ID List")
    private List<String> menuIds;
}
