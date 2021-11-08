package com.xxxx.server.controller;


import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Salary;
import com.xxxx.server.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 吉祥
 * @since 2021-10-21
 */
@RestController
@RequestMapping("/salary/sob/")
public class SalaryController {

    @Autowired
    private ISalaryService salaryService;

    @ApiOperation(value = "查询所有工资套账")
    @GetMapping("/")
    public List<Salary> getAllSalaries(){
        return salaryService.list();
    }

    @ApiOperation(value = "删除工资套账")
    @DeleteMapping("/{id}")
    public RespBean deleteSalary(@PathVariable Integer id){
        if (salaryService.removeById(id)) {
            return RespBean.success("删除成功",null);
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "更改工资套账")
    @PutMapping("/")
    public RespBean updateSalary(@RequestBody Salary salary){
        if (salaryService.updateById(salary)) {
            return RespBean.success("更新成功",null);
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "增加工资套账")
    @PostMapping("/")
    public RespBean addSalary(@RequestBody Salary salary){
        salary.setCreateDate(LocalDateTime.now());
        if (salaryService.save(salary)) {
            return RespBean.success("添加成功",null);
        }
        return RespBean.error("添加失败");
    }


}
