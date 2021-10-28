package com.xxxx.server.controller;


import com.xxxx.server.pojo.Joblevel;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IJoblevelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
@RequestMapping("/system/basic/joblevel")
public class JoblevelController {

    @Autowired
    private IJoblevelService joblevelService;

    @ApiOperation(value = "查询所有职称")
    @GetMapping("/")
    public List<Joblevel> getAllJobleve(){
        List<Joblevel> joblevels = joblevelService.list();
        return joblevels;
    }

    @ApiOperation(value = "添加职称")
    @PostMapping("/")
    public RespBean addJobleve(@RequestBody Joblevel joblevel){
        if (joblevelService.save(joblevel)) {
            return RespBean.success("添加职称成功",null);
        }
        return RespBean.error("添加职称失败");
    }

    @ApiOperation(value = "更新职称")
    @PutMapping("/")
    public RespBean updateJobleveById(@RequestBody Joblevel joblevel){
        if (joblevelService.updateById(joblevel)) {
            return RespBean.success("更新职位成功",null);
        }
        return RespBean.error("更新职称信息失败");
    }

    @ApiOperation(value = "删除职称")
    @DeleteMapping("/{id}")
    public RespBean deleteJobleveById(@PathVariable("id") Integer id){
        if (joblevelService.removeById(id)) {
            return RespBean.success("删除职称成功",null);
        }
        return RespBean.error("删除职称失败");
    }

    @ApiOperation(value = "批量删除职称")
    @DeleteMapping("/")
    public RespBean deleteJoblevesByIds(Integer[] ids){
        if (joblevelService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("批量删除职称成功",null);
        }
        return RespBean.error("批量删除职称失败");
    }


}
