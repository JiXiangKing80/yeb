package com.xxxx.server.controller;


import com.xxxx.server.pojo.*;
import com.xxxx.server.service.*;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
@RequestMapping("/employee/basic/")
public class EmployeeController {

    @Autowired
    IEmployeeService employeeService;
    @Autowired
    IPoliticsStatusService politicsStatusService;
    @Autowired
    INationService nationService;
    @Autowired
    IPositionService positionService;
    @Autowired
    IJoblevelService joblevelService;
    @Autowired
    IDepartmentService departmentService;

    @ApiOperation(value = "分页查询所有员工")
    @GetMapping("/")
    public RespPageBean getAllEmployeeByPage(Employee employee,
                                             LocalDate[] beginDateScope,
                                             @RequestParam(defaultValue = "1") Integer currentPage,
                                             @RequestParam(defaultValue = "10") Integer size){
        System.out.println(beginDateScope);
        return employeeService.getAllEmployeeByPage(employee,beginDateScope,currentPage,size);
    }

    @ApiOperation(value = "获取所有政治面貌")
    @GetMapping("/politicsStatus")
    public List<PoliticsStatus> getAllPoliticsStatus(){
        List<PoliticsStatus> list = politicsStatusService.list();
        return list;
    }

    @ApiOperation(value = "获取所有民族")
    @GetMapping("/nations")
    public List<Nation> getAllNation(){
        return nationService.list();
    }

    @ApiOperation(value = "获取所有职位")
    @GetMapping("/positions")
    public List<Position> getAllPosition(){
        return positionService.list();
    }

    @ApiOperation(value = "获取所有职称")
    @GetMapping("/joblevels")
    public List<Joblevel> getAllJoblevel(){
        return joblevelService.list();
    }

    @ApiOperation(value = "获取所有部门")
    @GetMapping("/deps")
    public List<Department> getAllDepartent(){
        return departmentService.getAllDepartments();
    }

    @ApiOperation(value = "获取工号")
    @GetMapping("/maxWorkID")
    public RespBean getMaxWorkId(){
        return RespBean.success(null,employeeService.getMaxWorkId());
    }

    @ApiOperation(value = "添加员工")
    @PostMapping("/")
    public RespBean addEmployee(@RequestBody Employee employee){
        if (1==employeeService.addEmployee(employee)){
            return RespBean.success("添加成功",null);
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "删除员工")
    @DeleteMapping("/{id}")
    public RespBean deleteEmployee(@PathVariable Integer id){
        if (employeeService.removeById(id)) {
            return RespBean.success("删除成功",null);
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "更新员工信息")
    @PutMapping("/")
    public RespBean updateEmployee(@RequestBody Employee employee){
        return employeeService.updateEmployee(employee);
    }
}
