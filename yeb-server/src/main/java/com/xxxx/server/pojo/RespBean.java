package com.xxxx.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBean {


    private long code;
    private String message;
    private Object object;


    /**
     * 成功返回结果
     * @param message
     * @param object
     * @return
     */
    public static RespBean success(String message,Object object){
        return new RespBean(200,message,object);
    }

    /**
     * 成功返回结果
     * @param object
     * @return
     */
    public static RespBean success(Object object){
        return new RespBean(200,"success",object);
    }

    /**
     * 失败返回结果
     * @param message
     * @param object
     * @return
     */
    public static RespBean error(String message,Object object){
        return new RespBean(500,message,object);
    }

    /**
     * 失败返回结果
     * @param message
     * @param object
     * @return
     */
    public static RespBean error(String message){
        return new RespBean(500,message,null);
    }



}
