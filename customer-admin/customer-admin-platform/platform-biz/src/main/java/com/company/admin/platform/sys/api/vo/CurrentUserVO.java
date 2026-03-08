package com.company.admin.platform.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Current User Info")
public class CurrentUserVO {

    @Schema(description = "User ID")
    private String userId;

    @Schema(description = "User Code")
    private String userCode;

    @Schema(description = "User Name")
    private String userName;

    @Schema(description = "Organization ID")
    private String orgId;

    @Schema(description = "Organization Name")
    private String orgName;

    @Schema(description = "Auth Level")
    private Integer authLevel;

    @Schema(description = "Roles")
    private List<String> roles;

    @Schema(description = "Menus")
    private List<LoginVO.MenuVO> menus;

    @Schema(description = "Permissions")
    private List<String> permissions;
}
