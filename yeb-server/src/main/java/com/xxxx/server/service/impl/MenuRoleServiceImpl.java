package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.MenuRoleMapper;
import com.xxxx.server.pojo.MenuRole;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IMenuRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 吉祥
 * @since 2021-10-21
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    @Override
    @Transactional //开启事务
    public RespBean updateMenuByRoleId(Integer rid, Integer[] mids) {
        //先删除
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));
        if (mids==null || mids.length==0){
            //如果目录信息为空直接删除完返回即可
            return RespBean.success("更新角色信息成功",null);
        }
        //再更新
        Integer num = menuRoleMapper.updateMenuByRoleId(rid,mids);
        if (mids.length==num){
            return RespBean.success("更新角色信息成功",null);
        }
        return RespBean.error("更新角色信息失败");
    }
}
