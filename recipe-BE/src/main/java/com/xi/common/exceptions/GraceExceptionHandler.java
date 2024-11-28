package com.xi.common.exceptions;


import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.util.SaResult;
import com.xi.common.grace.GraceJSONResult;
import com.xi.common.grace.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一异常拦截处理
 * 可以针对异常的类型进行捕获，然后返回json信息到前端
 */
@Slf4j
@ControllerAdvice
public class GraceExceptionHandler {


    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public GraceJSONResult handleNoHandlerFoundException(NotLoginException nle) {
        // 不同异常返回不同状态码
        String message = "";
        if (nle.getType().equals(NotLoginException.NOT_TOKEN)) {
            // "未提供Token";
            return new GraceJSONResult(ResponseStatusEnum.UN_LOGIN);
        } else if (nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
           // "未提供有效的Token,重新登录";
            return new GraceJSONResult(ResponseStatusEnum.TICKET_INVALID);
        } else if (nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            // "登录信息已过期，请重新登录";
            return new GraceJSONResult(ResponseStatusEnum.TOKEN_TIMEOUT);
        } else {
            return new GraceJSONResult(new GraceJSONResult(ResponseStatusEnum.UN_LOGIN));
        }

    }


    @ExceptionHandler(SaTokenException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GraceJSONResult handleSaTokenException(SaTokenException e) {
        log.info("sa-token抛出其他异常(请确认登录状态和角色！)");
        if(e.getMessage().equals("未能获取对应StpLogic，type=user")){
            return GraceJSONResult.errorMsg("请确认登录状态！");
        }
        return GraceJSONResult.errorMsg(e.getMessage());
    }


    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public GraceJSONResult returnMyException(MyCustomException e) {
//        e.printStackTrace();
        return GraceJSONResult.exception(e.getResponseStatusEnum());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public GraceJSONResult returnMethodArgumentNotValid(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        Map<String, String> map = getErrors(result);
        return GraceJSONResult.errorMap(map);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public GraceJSONResult returnMaxUploadSize(MaxUploadSizeExceededException e) {
//        e.printStackTrace();
        return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_MAX_SIZE_2MB_ERROR);
    }

    public Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError ff : errorList) {
            // 错误所对应的属性字段名
            String field = ff.getField();
            // 错误的信息
            String msg = ff.getDefaultMessage();
            map.put(field, msg);
        }
        return map;
    }
}
