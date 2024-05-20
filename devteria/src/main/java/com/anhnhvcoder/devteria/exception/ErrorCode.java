package com.anhnhvcoder.devteria.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized Exception"),
    INVALID_KEY(1001, "Invalid Key"),
    USER_EXISTED(1002, "User already existed"),
    USERNAME_INVALID(1003, "Username is invalid"),
    PASSWORD_INVALID(1004, "Password is invalid"),
    USER_NOT_FOUND(1005, "User not found"),
    UNAUTHENTICATED(1006, "Unauthenticated"),
;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;
    private final String message;
}
