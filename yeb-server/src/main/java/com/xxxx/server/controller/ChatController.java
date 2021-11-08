package com.xxxx.server.controller;

import com.xxxx.server.pojo.Admin;
import com.xxxx.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: webSocket聊天
 * @author: 吉祥
 * @created: 2021/11/07 20:09
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "获取所有聊天对象")
    @RequestMapping("/admin")
    public List<Admin> getAllAdmins(String keyWords){
        return adminService.getAllAdmins(keyWords);
    }

}

