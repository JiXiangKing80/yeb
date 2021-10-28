package com.xxxx.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.MenuRole;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IMenuRoleService;
import com.xxxx.server.service.IMenuService;
import com.xxxx.server.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 吉祥
 * @since 2021-10-21
 */
@RestController
@RequestMapping("/system/basic/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMenuRoleService menuRoleService;

    @ApiOperation(value = "查询所有角色")
    @GetMapping("/")
    public List<Role> getAllRole(){
        return roleService.list();
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/role")
    public RespBean addRole(@RequestBody Role role){
        if (roleService.save(role)) {
            return RespBean.success("添加角色成功",null);
        }
        return RespBean.error("添加角色失败");
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/{rid}")
    public RespBean deleteRoleById(@PathVariable("rid") Integer id){
        if (roleService.removeById(id)) {
            return RespBean.success("删除角色成功",null);
        }
        return RespBean.error("删除角色失败");
    }

    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/menus")
    public List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }

    @ApiOperation(value = "根据角色id查询菜单id")
    @GetMapping("/mid/{rid}")
    public Set<Integer> getMidByRoleId(@PathVariable Integer rid){
        List<MenuRole> menuRoles = menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid", rid));
        Set<Integer> result = new HashSet<>();
        for (MenuRole menuRole : menuRoles) {
            result.add(menuRole.getMid());
        }
        return result;
    }

    @ApiOperation(value = "更新角色菜单")
    @PutMapping("/")
    public RespBean updateMenuByRoleId(Integer rid,Integer[] mids){
        return menuRoleService.updateMenuByRoleId(rid,mids);
    }

}
