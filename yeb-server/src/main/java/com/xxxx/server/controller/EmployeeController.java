package com.xxxx.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.xxxx.server.pojo.*;
import com.xxxx.server.service.*;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Update;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    @ApiOperation(value = "导出所有员工信息")
    @GetMapping(value = "/export",produces = "application/octet-stream")
    public void exportEmployee(HttpServletResponse response){
        //获取所有同员信息
        List<Employee> list = employeeService.getEmployee(null);
        //导出excel
        ExportParams exportParams = new ExportParams("员工信息表","员工表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, Employee.class, list);
        ServletOutputStream out = null;
        try {
            //流形式输出
            response.setHeader("content-type", "application/octet-stream");
            // 防止中文乱码
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("员工表.xls", "UTF-8"));
            out = response.getOutputStream();
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @ApiOperation(value = "导入员工数据")
    @PostMapping("/import")
    public RespBean importEmployee(@RequestParam("file") MultipartFile file){
        ImportParams params = new ImportParams();
        //去掉标题
        params.setTitleRows(1);
        List<Nation> nationList= nationService.list();
        List<PoliticsStatus> politicsStatusList = politicsStatusService.list();
        List<Department> departmentList = departmentService.list();
        List<Joblevel> joblevelList = joblevelService.list();
        List<Position> positionList = positionService.list();
        try {
            List<Employee> list = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            list.forEach(employee -> {
                //设置民族id
                Integer nationId = nationList.get(nationList.indexOf(employee.getNation())).getId();
                employee.setNationId(nationId);
                //设置政治面貌id
                Integer politicId = politicsStatusList.get(politicsStatusList.indexOf(employee.getPoliticsStatus())).getId();
                employee.setPoliticId(politicId);
                //设置部门id
                Integer deaprtmentId = departmentList.get(departmentList.indexOf(employee.getDepartment())).getId();
                employee.setDepartmentId(deaprtmentId);
                //设置职称id
                Integer joblevelId = joblevelList.get(joblevelList.indexOf(employee.getJoblevel())).getId();
                employee.setJobLevelId(joblevelId);
                //设置职位id
                Integer postionId = positionList.get(positionList.indexOf(employee.getPosition())).getId();
                employee.setPosId(postionId);
            });
            employeeService.saveBatch(list);
            return RespBean.success("导入成功",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }
}
