package com.company.admin.platform.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Menu VO")
public class MenuVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Menu ID")
    private String menuId;

    @Schema(description = "Menu Name")
    private String menuName;

    @Schema(description = "Menu Type (1-Directory, 2-Menu, 3-Button)")
    private Integer menuType;

    @Schema(description = "Parent Menu ID")
    private String parentMenuId;

    @Schema(description = "Menu URL")
    private String menuUrl;

    @Schema(description = "Menu Icon")
    private String menuIcon;

    @Schema(description = "Permission Identifier")
    private String perms;

    @Schema(description = "Sort Order")
    private Integer sortOrder;

    @Schema(description = "Visible (0-Hidden, 1-Visible)")
    private Integer visible;

    @Schema(description = "Created Time")
    private LocalDateTime createdTime;

    @Schema(description = "Children Menus")
    private List<MenuVO> children;
}
