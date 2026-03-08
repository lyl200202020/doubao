package com.company.admin.platform.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "Update User Request")
public class UpdateUserRequest {

    @NotBlank(message = "User name is required")
    @Schema(description = "User Name", example = "Zhang San", required = true)
    private String userName;

    @NotBlank(message = "Organization ID is required")
    @Schema(description = "Organization ID", example = "1", required = true)
    private String orgId;

    @NotNull(message = "Auth level is required")
    @Schema(description = "Auth Level (1-16)", example = "5", required = true)
    private Integer authLevel;
}
