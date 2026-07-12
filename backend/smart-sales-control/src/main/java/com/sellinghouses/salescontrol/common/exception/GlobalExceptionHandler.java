package com.sellinghouses.salescontrol.common.exception;

import com.sellinghouses.salescontrol.common.result.Result;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletResponse response) {
        response.setStatus(e.getErrorCode().getCode());
        return Result.fail(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletResponse response) {
        response.setStatus(ErrorCode.BAD_REQUEST.getCode());
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage() == null ? ErrorCode.BAD_REQUEST.getMessage() : error.getDefaultMessage())
                .orElse(ErrorCode.BAD_REQUEST.getMessage());
        return Result.fail(ErrorCode.BAD_REQUEST, message);
    }

    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e, HttpServletResponse response) {
        response.setStatus(ErrorCode.BAD_REQUEST.getCode());
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage() == null ? ErrorCode.BAD_REQUEST.getMessage() : error.getDefaultMessage())
                .orElse(ErrorCode.BAD_REQUEST.getMessage());
        return Result.fail(ErrorCode.BAD_REQUEST, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e, HttpServletResponse response) {
        response.setStatus(ErrorCode.BAD_REQUEST.getCode());
        String message = e.getConstraintViolations().stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse(ErrorCode.BAD_REQUEST.getMessage());
        return Result.fail(ErrorCode.BAD_REQUEST, message);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletResponse response) {
        response.setStatus(ErrorCode.BAD_REQUEST.getCode());
        return Result.fail(ErrorCode.BAD_REQUEST, "上传文件过大，单张图片不能超过5MB");
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e, HttpServletResponse response) {
        response.setStatus(ErrorCode.SYSTEM_ERROR.getCode());
        log.error("系统异常", e);
        return Result.fail(ErrorCode.SYSTEM_ERROR, ErrorCode.SYSTEM_ERROR.getMessage());
    }
}
