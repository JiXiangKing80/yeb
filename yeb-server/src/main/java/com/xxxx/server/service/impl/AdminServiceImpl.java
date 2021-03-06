package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.config.security.JwtTokenUtil;
import com.xxxx.server.mapper.AdminMapper;
import com.xxxx.server.mapper.AdminRoleMapper;
import com.xxxx.server.mapper.RoleMapper;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 吉祥
 * @since 2021-10-21
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;

    /**
     * 登陆之后返回token
     * @param username
     * @param password
     * @param request
     * @return
     */
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        //校验验证码
        String captch = (String) request.getSession().getAttribute("captcha");
        if (code == null || !code.equalsIgnoreCase(captch)){
            return RespBean.error("验证码输入错误");
        }else {
            //移除验证码
            request.getSession().removeAttribute("captcha");
        }
        //验证用户
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null || !passwordEncoder.matches(password,userDetails.getPassword())){
            return RespBean.error("用户名或者密码不正确");
        }
        if (userDetails.isEnabled() == false){
            return RespBean.error("账号已被禁用,请联系管理员");
        }
        //更新security登录的用户信息
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        //生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String,String> result = new HashMap<>();
        result.put("token",token);
        result.put("tokenHead",tokenHead);
        return RespBean.success("登陆成功",result);
    }

    /**
     * 获取当前登录用户信息
     * @param username
     * @return
     */
    @Override
    public Admin getAminInfoByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username).eq("enabled",true));
    }

    /**
     * 根据用户id获取用户角色
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRolesByAdminId(Integer adminId) {
        return roleMapper.getRolesByAdminId(adminId);
    }

    /**
     * 获取所有操作员(当前登录用户除外)
     * @param keyWords
     * @return
     */
    @Override
    public List<Admin> getAllAdmins(String keyWords) {
        //获取当前用户id
        Integer adminId = ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return adminMapper.getAllAdmins(adminId,keyWords);
    }

    /**
     * 删除管理员
     * @param id
     * @return
     */
    @Override
    @Transactional
    public RespBean deleteAdminById(Integer id) {
        try{
            //先删除用户信息
            adminMapper.deleteById(id);
            //再删除用户对应角色
            adminRoleMapper.deleteAdminRoles(id);
            return RespBean.success("删除成功",null);
        }catch (Exception e){
            return RespBean.error("删除失败");
        }
    }

    /**
     * 更新管理员角色
     * @param aid
     * @param rids
     * @return
     */
    @Override
    @Transactional
    public RespBean updateRoles(Integer aid, Integer[] rids) {
        //先去删除用户角色
        adminRoleMapper.deleteAdminRoles(aid);
        //再重新添加用户角色
        Integer resultNum = adminRoleMapper.insertAdminRoles(aid,rids);
        if (resultNum == rids.length){
            return RespBean.success("更新成功",null);
        }
        return RespBean.error("更新失败");
    }

    /**
     * 更新用户密码
     * @param oldPass
     * @param newPass
     * @param adminId
     * @return
     */
    @Override
    public RespBean updatePass(String oldPass, String newPass, Integer adminId) {
        //获取用户信息
        Admin admin = adminMapper.selectById(adminId);
        //验证老密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPass,admin.getPassword())){
            admin.setPassword(encoder.encode(newPass));
            int result = adminMapper.updateById(admin);
            if (result == 1){
                return RespBean.success("更新成功",null);
            }
        }
        return RespBean.error("更新失败");
    }

    /**
     * 更新用户头像
     * @param faceUrl
     * @param adminId
     * @param authentication
     * @return
     */
    @Override
    public RespBean updateUserFace(String faceUrl, Integer adminId, Authentication authentication) {
        //获取当前用户
        Admin admin = adminMapper.selectById(adminId);
        //设置新头像
        admin.setUserFace(faceUrl);
        //更新用户
        int i = adminMapper.updateById(admin);
        if (i==1){
            //更新成功，重新设置security上下文对象
//            Admin principal = (Admin) authentication.getPrincipal();
//            principal.setUserFace(faceUrl);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin,null,authentication.getAuthorities()));
            return RespBean.success("更新成功",faceUrl);
        }
        return RespBean.error("更新失败");
    }
}
