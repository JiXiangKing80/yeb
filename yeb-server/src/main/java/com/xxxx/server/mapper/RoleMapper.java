package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 吉祥
 * @since 2021-10-21
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户id获取用户角色
     * @param adminId
     * @return
     */
    List<Role> getRolesByAdminId(Integer adminId);

}
