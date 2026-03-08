package com.company.admin.common.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<T> list;
    private long total;
    private long page;
    private long pageSize;

    public static <T> PageResult<T> of(List<T> list, long total, long page, long pageSize) {
        PageResult<T> result = new PageResult<>();
        result.setList(list);
        result.setTotal(total);
        result.setPage(page);
        result.setPageSize(pageSize);
        return result;
    }
}
