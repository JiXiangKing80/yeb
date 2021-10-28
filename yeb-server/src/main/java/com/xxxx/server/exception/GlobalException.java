package com.xxxx.server.exception;

import com.xxxx.server.pojo.RespBean;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @description: 全局异常处理
 * @author: 吉祥
 * @created: 2021/10/25 17:45
 */
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(SQLException.class)
    public RespBean mySqlException(SQLException e){
        if (e instanceof SQLIntegrityConstraintViolationException){
            return RespBean.error("该数据有关联数据，操作失败");
        }
        return RespBean.error("数据库异常，操作失败");
    }

//    @ExceptionHandler(NullPointerException.class)
//    public RespBean nullPointerException(NullPointerException e){
//        return RespBean.error("登录已过期，请重新登录");
//    }
    //Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE2MzUxMjk1OTYwNTYsImV4cCI6MTYzNTE1OTU5Nn0.k27tYU9lK8JKj8U1wEyQh2a-P5hydi6MxkycLpEwEndVpqAd26mtLduIby9jwuRpUdeT_GSz35z7wt9zGp3uAQ

}

