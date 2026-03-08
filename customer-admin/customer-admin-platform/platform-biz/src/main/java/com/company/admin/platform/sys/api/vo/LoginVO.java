package com.company.admin.platform.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Login Response")
public class LoginVO {

    @Schema(description = "Access Token")
    private String token;

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

    @Schema(description = "Menus")
    private List<MenuVO> menus;

    @Schema(description = "Permissions")
    private List<String> permissions;

    @Data
    @Schema(description = "Menu VO")
    public static class MenuVO {
        @Schema(description = "Menu ID")
        private String menuId;

        @Schema(description = "Menu Name")
        private String menuName;

        @Schema(description = "Menu Type: 1-Directory, 2-Menu, 3-Button")
        private Integer menuType;

        @Schema(description = "Parent Menu ID")
        private String parentMenuId;

        @Schema(description = "Menu URL")
        private String menuUrl;

        @Schema(description = "Menu Icon")
        private String menuIcon;

        @Schema(description = "Permission")
        private String perms;

        @Schema(description = "Sort Order")
        private Integer sortOrder;

        @Schema(description = "Visible: 0-Hidden, 1-Visible")
        private Integer visible;

        @Schema(description = "Children Menus")
        private List<MenuVO> children;
    }
}
