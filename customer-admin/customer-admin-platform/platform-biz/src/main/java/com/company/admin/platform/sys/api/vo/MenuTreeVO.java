package com.company.admin.platform.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Menu Tree Node")
public class MenuTreeVO {

    @Schema(description = "Menu ID")
    private String menuId;

    @Schema(description = "Parent ID")
    private String parentId;

    @Schema(description = "Menu Name")
    private String menuName;

    @Schema(description = "Menu Type (1=Directory, 2=Menu, 3=Button)")
    private Integer menuType;

    @Schema(description = "Permission Code")
    private String permission;

    @Schema(description = "Route Path")
    private String routePath;

    @Schema(description = "Component Path")
    private String component;

    @Schema(description = "Menu Icon")
    private String menuIcon;

    @Schema(description = "Sort Order")
    private Integer sortOrder;

    @Schema(description = "Children Menus")
    private List<MenuTreeVO> children;
}
