package com.company.admin.platform.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "User Info Response")
public class UserInfoVO {

    @Schema(description = "User ID")
    private String userId;

    @Schema(description = "Username")
    private String username;

    @Schema(description = "Real Name")
    private String realName;

    @Schema(description = "Organization ID")
    private String orgId;

    @Schema(description = "Organization Name")
    private String orgName;

    @Schema(description = "Auth Level")
    private Integer authLevel;

    @Schema(description = "User Status")
    private Integer userStatus;

    @Schema(description = "Role IDs")
    private List<String> roleIds;

    @Schema(description = "Role Names")
    private List<String> roleNames;
}
