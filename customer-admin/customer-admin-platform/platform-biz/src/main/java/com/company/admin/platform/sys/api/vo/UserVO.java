package com.company.admin.platform.sys.api.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "User VO")
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @Schema(description = "Auth Level (1-16)")
    private Integer authLevel;

    @Schema(description = "Status (0-Disabled, 1-Enabled)")
    private Integer userStatus;

    @Schema(description = "Created Time")
    private LocalDateTime createTime;
}

@Data
@Schema(description = "User Page Result")
class UserPageResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "User List")
    private List<UserVO> list;

    @Schema(description = "Total Count")
    private Long total;

    @Schema(description = "Page Number")
    private Long pageNum;

    @Schema(description = "Page Size")
    private Long pageSize;

    @Schema(description = "Total Pages")
    private Long pages;

    public static UserPageResult from(Page<UserVO> page) {
        UserPageResult result = new UserPageResult();
        result.setList(page.getRecords());
        result.setTotal(page.getTotal());
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setPages((page.getTotal() + page.getSize() - 1) / page.getSize());
        return result;
    }
}
