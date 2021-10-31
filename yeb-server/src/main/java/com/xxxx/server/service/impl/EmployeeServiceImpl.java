package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.EmployeeMapper;
import com.xxxx.server.pojo.Employee;
import com.xxxx.server.pojo.RespPageBean;
import com.xxxx.server.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 吉祥
 * @since 2021-10-21
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * 分页查询所有员工
     * @param employee
     * @param beginDateScope
     * @param currentPage
     * @param size
     * @return
     */
    @Override
    public RespPageBean getAllEmployeeByPage(Employee employee, LocalDate[] beginDateScope, Integer currentPage, Integer size) {
        Page<Employee> page = new Page<>(currentPage,size);
        IPage<Employee> result = employeeMapper.getAllEmployeeByPage(page,employee,beginDateScope);
        RespPageBean respPageBean = new RespPageBean(result.getTotal(), result.getRecords());
        return respPageBean;
    }
}
