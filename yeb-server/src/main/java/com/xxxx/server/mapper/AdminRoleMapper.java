package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.AdminRole;
import org.springframework.data.repository.query.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 吉祥
 * @since 2021-10-21
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 删除管理员角色信息
     * @param id
     */
    void deleteAdminRoles(Integer id);

    /**
     * 添加用户角色
     * @param aid
     * @param rids
     * @return
     */
    Integer insertAdminRoles(@Param("aid") Integer aid, @Param("rids") Integer[] rids);
}
