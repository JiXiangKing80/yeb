package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Employee;
import com.xxxx.server.pojo.RespPageBean;

import java.time.LocalDate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 吉祥
 * @since 2021-10-21
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 分页查询所有员工
     * @param employee
     * @param beginDateScope
     * @param currentPage
     * @param size
     * @return
     */
    RespPageBean getAllEmployeeByPage(Employee employee, LocalDate[] beginDateScope, Integer currentPage, Integer size);
}
