package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 吉祥
 * @since 2021-10-21
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 登陆之后返回token
     *
     * @param username
     * @param password
     * @param request
     * @return
     */
    RespBean login(String username, String password,String code,HttpServletRequest request);

    /**
     * 获取当前登录用户信息
     * @param username
     * @return
     */
    Admin getAminInfoByUserName(String username);

    /**
     * 根据用户id获取用户角色
     * @param adminId
     * @return
     */
    List<Role> getRolesByAdminId(Integer adminId);
}
