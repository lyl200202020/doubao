package com.company.admin.platform.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Organization VO")
public class OrgVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Organization ID")
    private String orgId;

    @Schema(description = "Organization Code")
    private String orgCode;

    @Schema(description = "Organization Name")
    private String orgName;

    @Schema(description = "Organization Level (0-Head, 1-Branch, 2-Sub-branch, 3-Office)")
    private Integer orgLevel;

    @Schema(description = "Parent Organization ID")
    private String parentOrgId;

    @Schema(description = "Status (0-Disabled, 1-Enabled)")
    private Integer orgStatus;

    @Schema(description = "Sort Order")
    private Integer sortOrder;

    @Schema(description = "Remark")
    private String remark;

    @Schema(description = "Created By")
    private String createdBy;

    @Schema(description = "Created Time")
    private LocalDateTime createdTime;

    @Schema(description = "Updated By")
    private String updatedBy;

    @Schema(description = "Updated Time")
    private LocalDateTime updatedTime;

    @Schema(description = "Children Organizations")
    private List<OrgVO> children;
}
