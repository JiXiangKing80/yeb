package com.xxxx.server.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxxx.server.pojo.Employee;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.RespPageBean;
import com.xxxx.server.pojo.Salary;
import com.xxxx.server.service.IEmployeeService;
import com.xxxx.server.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 员工套账管理
 * @author: 吉祥
 * @created: 2021/11/05 19:24
 */
@RestController
@RequestMapping("/salary/sobcfg/")
public class SalarySobCfgController {

    @Autowired
    private ISalaryService salaryService;
    @Autowired
    private IEmployeeService employeeService;


    @ApiOperation(value = "获取所有工资套账")
    @GetMapping("/salaries")
    public List<Salary> getAllSalaries(){
        return salaryService.list();
    }

    @ApiOperation(value = "获取所有员工套账（分页查询）")
    @GetMapping("/")
    public RespPageBean getEmployeeWithSalary(@RequestParam(defaultValue = "1") Integer currentPage,
                                              @RequestParam(defaultValue = "10") Integer size){
        return employeeService.getEmployeeWithSalary(currentPage,size);
    }

    @ApiOperation(value = "修改员工套账")
    @PutMapping("/")
    public RespBean updateSalary(Integer eid,Integer sid){
        if (employeeService.update(new UpdateWrapper<Employee>().eq("id",eid).set("salaryId",sid))) {
            return RespBean.success("更新成功",null);
        }
        return RespBean.error("更新失败");
    }

}

