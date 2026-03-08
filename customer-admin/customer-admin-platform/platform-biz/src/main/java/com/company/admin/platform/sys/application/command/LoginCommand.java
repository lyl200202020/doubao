package com.company.admin.platform.sys.application.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "登录请求")
public class LoginCommand {
    
    @NotBlank(message = "用户工号不能为空")
    @Schema(description = "用户工号")
    private String userCode;
    
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;
}
