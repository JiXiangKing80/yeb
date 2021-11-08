package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Employee;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.RespPageBean;

import java.time.LocalDate;
import java.util.List;

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

    /**
     * 获取工号
     * @return
     */
    String getMaxWorkId();

    /**
     * 添加员工
     * @param employee
     * @return
     */
    int addEmployee(Employee employee);

    /**
     * 更新员工信息
     * @param employee
     * @return
     */
    RespBean updateEmployee(Employee employee);

    /**
     * 获取员工(通用)
     * @param id
     * @return
     */
    List<Employee> getEmployee(Integer id);

    /**
     * 获取所有员工套账（分页查询）
     * @param currentPage
     * @param size
     * @return
     */
    RespPageBean getEmployeeWithSalary(Integer currentPage, Integer size);
}
