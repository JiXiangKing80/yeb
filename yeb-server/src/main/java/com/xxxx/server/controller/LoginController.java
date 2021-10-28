package com.xxxx.server.controller;

import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.AdminLoginParam;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

/**
 * @description: 登录
 * @author: 吉祥
 * @created: 2021/10/22 10:34
 */
@RestController
@Api(tags = "LoginController")
public class LoginController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "登陆之后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request){
        return adminService.login(adminLoginParam.getUsername(),adminLoginParam.getPassword(),adminLoginParam.getCode(),request);
    }

    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/admin/info")
    public RespBean getAdminInfo(Principal principal){
        if (principal == null){
            return RespBean.error("请先登录");
        }
        String username = principal.getName();
        Admin admin = adminService.getAminInfoByUserName(username);
        admin.setPassword(null);
        List<Role> rolesByAdminId = adminService.getRolesByAdminId(admin.getId());
        admin.setRoles(rolesByAdminId);
        return RespBean.success(admin);
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/admin/logout")
    public RespBean logout(){
        return RespBean.success("退出成功",null);
    }

}

