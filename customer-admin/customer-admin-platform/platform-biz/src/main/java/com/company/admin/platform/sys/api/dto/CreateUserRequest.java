package com.company.admin.platform.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
@Schema(description = "Create User Request")
public class CreateUserRequest {

    @NotBlank(message = "User code is required")
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$", message = "User code must be 4-32 characters")
    @Schema(description = "User Code", example = "zhangsan", required = true)
    private String userCode;

    @NotBlank(message = "User name is required")
    @Schema(description = "User Name", example = "Zhang San", required = true)
    private String userName;

    @Schema(description = "Organization ID", example = "1", required = true)
    @NotBlank(message = "Organization ID is required")
    private String orgId;

    @NotNull(message = "Auth level is required")
    @Schema(description = "Auth Level (1-16)", example = "5", required = true)
    private Integer authLevel;
}
