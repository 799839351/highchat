package com.txk.highchat.utils;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice

public class GlobalDefultExceptionHandler {
    /**https://www.cnblogs.com/magicalSam/p/7198420.html
     * 处理参数异常，一般用于校验body参数
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public JsonResult handleValidationBodyException(MethodArgumentNotValidException e) {
        for (ObjectError s : e.getBindingResult().getAllErrors()) {
            return new JsonResult("error", s.getDefaultMessage(),null );
        }
        return new JsonResult("error", "未知错误", null);
    }

    /**
     * 主动throw的异常
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResult handleException(Exception e, HttpServletResponse response) {

        return new JsonResult("error", e.getMessage()+e.getStackTrace(), null);
    }
}