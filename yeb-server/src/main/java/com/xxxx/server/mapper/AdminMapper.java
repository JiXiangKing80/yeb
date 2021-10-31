package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 吉祥
 * @since 2021-10-21
 */
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 获取所有操作员(当前登录用户除外)
     * @param adminId
     * @param keyWords
     * @return
     */
    List<Admin> getAllAdmins(@Param("adminId") Integer adminId, @Param("keyWords") String keyWords);

}
