package com.company.admin.platform.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "Create Organization Request")
public class CreateOrgRequest {

    @NotBlank(message = "Organization code is required")
    @Schema(description = "Organization Code", example = "BJ_BRANCH", required = true)
    private String orgCode;

    @NotBlank(message = "Organization name is required")
    @Schema(description = "Organization Name", example = "Beijing Branch", required = true)
    private String orgName;

    @Schema(description = "Parent Organization ID", example = "1")
    private String parentOrgId;

    @Schema(description = "Sort Order", example = "1")
    private Integer sortOrder = 0;

    @Schema(description = "Remark", example = "Beijing Branch Office")
    private String remark;
}
