package com.anhnhvcoder.devteria.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid Key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User already existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username is invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password is invalid", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1005, "User not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1007, "Access Denied", HttpStatus.FORBIDDEN),
    ROLE_NOT_FOUND(1008, "Role not found", HttpStatus.NOT_FOUND),
    INVALID_DOB(1009, "You need to be at least {min}", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1010, "Email already existed", HttpStatus.BAD_REQUEST),
;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    private  int code;
    private  String message;
    private  HttpStatus httpStatus;
}
