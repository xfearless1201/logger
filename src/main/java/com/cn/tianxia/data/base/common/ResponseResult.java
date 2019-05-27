package com.cn.tianxia.data.base.common;

/**
 * @ClassName ResponseResult
 * @Description 封装返回结果集
 * @author Hardy
 * @Date 2019年5月27日 下午3:45:28
 * @version 1.0.0
 */
public class ResponseResult {

    private int status;

    private String code;

    private String msg;

    private String token;

    private Object data;

    public ResponseResult(int status, String code, String msg) {
        super();
        this.status = status;
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(int status, String code, String msg, String token) {
        super();
        this.status = status;
        this.code = code;
        this.msg = msg;
        this.token = token;
    }

    public ResponseResult(int status, String code, String msg, String token, Object data) {
        super();
        this.status = status;
        this.code = code;
        this.msg = msg;
        this.token = token;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResponseResult success(String msg) {
        return new ResponseResult(ResponseCode.SUCCESS_STATUS, ResponseCode.SUCCESS_CODE, msg);
    }
    
    public static ResponseResult success(String msg, String token) {
        return new ResponseResult(ResponseCode.SUCCESS_STATUS, ResponseCode.SUCCESS_CODE, msg, token);
    }

    public static ResponseResult success(String msg, String token, Object data) {
        return new ResponseResult(ResponseCode.SUCCESS_STATUS, ResponseCode.SUCCESS_CODE, msg, token, data);
    }
    
    public static ResponseResult fail(String msg) {
        return new ResponseResult(ResponseCode.FAIL_STATUS, ResponseCode.FAIL_CODE, msg);
    }

    public static ResponseResult fail(String msg, String token) {
        return new ResponseResult(ResponseCode.FAIL_STATUS, ResponseCode.FAIL_CODE, msg, token);
    }

    public static ResponseResult error(String msg, String token) {
        return new ResponseResult(ResponseCode.ERROR_STATUS, ResponseCode.ERROR_CODE, msg, token);
    }
}
