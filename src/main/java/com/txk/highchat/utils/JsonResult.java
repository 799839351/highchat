package com.txk.highchat.utils;

import java.io.Serializable;

/**
 * 类名称：JsonResult
 * 类描述：自定义响应数据结构
 *  这个类是提供给门户，ios，安卓，微信商城用的
 *  门户接受此类数据后需要使用本类的方法转换成对于的数据类型格式（类，或者list）
 *  其他自行处理
 *  200：表示成功
 *  500：表示错误，错误信息在msg字段中
 *  501：bean验证错误，不管多少个错误都以map形式返回
 *  502：拦截器拦截到用户token出错
 *  555：异常抛出信息
 * 创建人：谭小康
 * 创建时间：2019-03-21 11:04
 * Version 1.0.0
 */
public class JsonResult<T> implements Serializable{

    private static final long serialVersionUID = -3302055689715883506L;

    /**
     * 数据
     */
    private T data;

    /**
     * 信息
     */
    private String message;


    /**
    * 状态码
    * */
    private  String code;



    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



    public JsonResult(){
        super();
    }

    public JsonResult(String code, String message,T data){
        this.data = data;
        this.message = message;
        this.code = code;
    }


    public static JsonResult errorMsg(String msg) {
        return new JsonResult("500", msg, null);
    }

    public static JsonResult errorMap(Object data) {
        return new JsonResult("501", "error", data);
    }

    public static JsonResult errorTokenMsg(String msg) {
        return new JsonResult("502", msg, null);
    }

    public static JsonResult errorException(String msg) {
        return new JsonResult("555", msg, null);
    }
    public static  <T> JsonResult success(T data) {
        return new JsonResult("200", "OK", data);
    }


}
