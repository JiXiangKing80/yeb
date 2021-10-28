package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Department;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 吉祥
 * @since 2021-10-21
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 查询所有部门
     * @param i
     * @return
     */
    List<Department> getAllDepartments(Integer i);

    /**
     * 添加部门
     * @param department
     */
    void addDepartment(Department department);

    /**
     * 删除部门
     * @param department
     */
    void deletePartment(Department department);
}
