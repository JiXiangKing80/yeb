package com.xxxx.server.controller;

import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.AdminLoginParam;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IAdminService;
import com.xxxx.server.utils.FastDFSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;

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

    @ApiOperation(value = "修改用户信息")
    @PutMapping("/admin/info")
    public RespBean updateAdmin(@RequestBody Admin admin, Authentication authentication){
        if (adminService.updateById(admin)) {
            //更新成功，重新设置Authentication信息
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin,null,authentication.getAuthorities()));
            return RespBean.success("更新成功",null);
        }else {
            return RespBean.error("更新失败");
        }
    }

    @ApiOperation(value = "更新用户密码")
    @PutMapping("/admin/pass")
    public RespBean updatePass(@RequestBody Map<String,Object> info){
        //获取用户输入信息
        String oldPass = (String) info.get("oldPass");
        String newPass = (String) info.get("pass");
        Integer adminId = (Integer) info.get("adminId");
        //更新密码
        return adminService.updatePass(oldPass,newPass,adminId);
    }

    @ApiOperation(value = "更新用户头像")
    @PostMapping("/admin/userface")
    public RespBean updateUserFace(MultipartFile file,Integer adminId,Authentication authentication){
        //上传头像到FastDFS
        String[] filePath = FastDFSUtils.upload(file);
        if (filePath!=null && filePath.length>0){
            String faceUrl = FastDFSUtils.getTrackerUrl() + "/" + filePath[0] + "/" + filePath[1];
            return adminService.updateUserFace(faceUrl,adminId,authentication);
        }
        return RespBean.error("更新失败");
    }

}

