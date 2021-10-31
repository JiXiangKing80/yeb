package com.xxxx.server.controller;


import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.AdminRole;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IAdminService;
import com.xxxx.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 吉祥
 * @since 2021-10-21
 */
@RestController
@RequestMapping("/system/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private IRoleService roleService;

    @ApiOperation(value = "获取所有操作员(当前登录用户除外)")
    @GetMapping("/")
    public List<Admin> getAllAdmins(String keywords){
        return adminService.getAllAdmins(keywords);
    }

    @ApiOperation(value = "更新管理员")
    @PutMapping("/")
    public RespBean updateAdmin(@RequestBody Admin admin){
        if (adminService.updateById(admin)) {
            return RespBean.success("更新管理员信息成功",null);
        }
        return RespBean.error("更新管理员信息失败");
    }

    @ApiOperation(value = "删除管理员")
    @DeleteMapping("/")
    public RespBean deleteAdminById(Integer id){
        return adminService.deleteAdminById(id);
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation(value = "更新管理员角色")
    @PutMapping("/role")
    public RespBean updateRoles(Integer adminId,Integer[] rids){
        return adminService.updateRoles(adminId,rids);
    }

}
