package org.bumishi.toolbox.model;

import java.io.Serializable;

/**
 * Restful api response model
 * Created by xieqiang on 2016/11/26.
 */
public class RestResponse<T> implements Serializable {

    private boolean success;

    private String code;

    private String msg;

    private T data;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static RestResponse ok(){
        RestResponse rep=new RestResponse();
        rep.success=true;
        return rep;
    }


    public static RestResponse ok(Object data){
        RestResponse rep=new RestResponse();
        rep.success=true;
        rep.data=data;
        return rep;
    }

    public static RestResponse fail(){
        RestResponse rep=new RestResponse();
        rep.success=false;
        return rep;
    }

    public static RestResponse fail(String msg){
        RestResponse rep=new RestResponse();
        rep.success=false;
        rep.msg=msg;
        return rep;
    }

    public static RestResponse fail(String code,String msg){
        RestResponse rep=new RestResponse();
        rep.success=false;
        rep.msg=msg;
        rep.code=code;
        return rep;
    }


}
