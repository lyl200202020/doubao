package com.company.admin.platform.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@Schema(description = "Assign Menus to Role Request")
public class AssignMenusRequest {

    @Schema(description = "Menu ID List", required = true)
    private List<String> menuIds;
}
