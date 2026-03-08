package com.company.admin.common.exception;

import com.company.admin.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final int code;
    private final String msg;

    public BusinessException(String msg) {
        super(msg);
        this.code = 500;
        this.msg = msg;
    }

    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public BusinessException(ErrorCode errorCode, Object... args) {
        super(String.format(errorCode.getMsg(), args));
        this.code = errorCode.getCode();
        this.msg = String.format(errorCode.getMsg(), args);
    }
}
