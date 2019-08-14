package com.david.message.solution.common;

public class ServiceResponse {
    private boolean success;
    private int code;
    private Object message;
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public static ServiceResponse ok() {
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setSuccess(true);
        serviceResponse.setCode(200);
        serviceResponse.setMessage(null);
        return serviceResponse;
    }
    public static ServiceResponse ok(Object message) {
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setSuccess(true);
        serviceResponse.setCode(200);
        serviceResponse.setMessage(message);
        return serviceResponse;
    }
    public static ServiceResponse error(int code, Object message) {
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setSuccess(false);
        serviceResponse.setCode(code);
        serviceResponse.setMessage(message);
        return serviceResponse;
    }

}
