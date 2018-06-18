package com.cloud.paas.exception;

import com.cloud.paas.util.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 全局异常处理，当异常被@ControllerAdvice时不会走到这个处理类，没被处理时会走到这里
 * 例如 @valid入参校验失败的是不会走ControllerAdvice的，但会走这个处理器
 *
 * 定义全局异常处理
 * @RestControllerAdvice 是@controlleradvice 与@ResponseBody 的组合注解
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result constraintViolationException(ConstraintViolationException exception) {
        return new Result(0,"404",exception.getMessage(),1,1,null);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result IllegalArgumentException(IllegalArgumentException exception) {
        return new Result(0,"404",exception.getMessage(),1,1,null);
    }

    @ExceptionHandler(value = { NoHandlerFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result noHandlerFoundException(Exception exception) {
        return new Result(0,"404",exception.getMessage(),1,1,null);
    }

    @ExceptionHandler(value = { BusinessException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result unknownException(BusinessException businessException) {
        businessException.getResult().setSuccess(0);
        return businessException.getResult();
    }

    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result unknownException(Exception exception) {
        return new Result(0,"500",exception.getMessage(),1,1,null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleIllegalParamException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        String tips = "参数不合法";
        if (errors.size() > 0) {
            tips = errors.get(0).getDefaultMessage();
        }
        Result result = new Result(0,"500",tips,1,1,null);
        return result;
    }
}

