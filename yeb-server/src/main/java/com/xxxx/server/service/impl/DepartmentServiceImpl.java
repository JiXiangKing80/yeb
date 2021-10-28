package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.DepartmentMapper;
import com.xxxx.server.pojo.Department;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 吉祥
 * @since 2021-10-21
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    DepartmentMapper departmentMapper;

    /**
     * 查询所有部门
     * @return
     */
    @Override
    public List<Department> getAllDepartments() {
        List<Department> departments = departmentMapper.getAllDepartments(-1);
        return departments;
    }

    /**
     * 添加部门
     * @param department
     * @return
     */
    @Override
    public RespBean addDepartment(Department department) {
        //设置部门可用
        department.setEnabled(true);
        //调用存储过程,保存部门信息
        departmentMapper.addDepartment(department);
        if (department.getResult() == 1){
            return RespBean.success("添加部门成功",department);
        }
        return RespBean.error("添加部门失败");
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @Override
    public RespBean deletePartment(Integer id) {
        Department department = new Department();
        department.setId(id);
        departmentMapper.deletePartment(department);
        if (department.getResult() == -2){
            return RespBean.error("删除失败，该部门子部门未删除");
        }else if (department.getResult() == -1){
            return RespBean.error("删除失败，该部门下还存在员工");
        }else if (department.getResult() == 1){
            return RespBean.success("删除成功",null);
        }
        return RespBean.error("删除失败，原因未知");
    }
}
