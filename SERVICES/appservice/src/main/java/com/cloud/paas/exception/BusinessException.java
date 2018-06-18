package com.cloud.paas.exception;

import com.cloud.paas.util.result.Result;

public class BusinessException extends RuntimeException {

    private Result Result;

    private BusinessException() {
    }

    public BusinessException(Result result) {
        this.setResult(result);
    }

    public Result getResult() {
        return Result;
    }

    public void setResult(Result result) {
        Result = result;
    }
}