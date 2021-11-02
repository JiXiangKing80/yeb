package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.EmployeeMapper;
import com.xxxx.server.pojo.Employee;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.RespPageBean;
import com.xxxx.server.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

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

    /**
     * 获取工号
     * @return
     */
    @Override
    public String getMaxWorkId() {
        List<Map<String, Object>> maps = employeeMapper.selectMaps(new QueryWrapper<Employee>().select("max(workID)"));
        String o = (String) maps.get(0).get("max(workID)");
        String format = String.format("%08d", Integer.parseInt(o) + 1);
        return format;
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @Override
    public int addEmployee(Employee employee) {
        // 处理合同期限，保留两位小数
        LocalDate beginContract = employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
        long until = beginContract.until(endContract, ChronoUnit.DAYS);
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        String format = decimalFormat.format(until / 365.00);
        employee.setContractTerm(Double.parseDouble(format));
        //保存员工对象
        int insert = employeeMapper.insert(employee);
        return insert;
    }

    /**
     * 更新员工信息
     * @param employee
     * @return
     */
    @Override
    public RespBean updateEmployee(Employee employee) {
        //计算合同期限
        LocalDate beginContract = employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
        long days = beginContract.until(endContract, ChronoUnit.DAYS);
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        String format = decimalFormat.format(days / 365.00);
        employee.setContractTerm(Double.parseDouble(format));
        int i = employeeMapper.updateById(employee);
        if (1 == i){
            return RespBean.success("更新成功",null);
        }
        return RespBean.error("更新失败");
    }

    @Override
    public List<Employee> getEmployee(Integer id) {
        return employeeMapper.getEmployee(id);
    }
}
