package com.yuuuuy.eshop.handler;

import com.yuuuuy.eshop.model.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.yuuuuy.eshop.controller")
@Slf4j
public class ControllerExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception ex) {
        // 处理异常，并返回相应的响应信息
        return Result.fail(500, ex.getMessage());
    }
}
