package com.xxxx.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 全局分页数据返回数据
 * @author: 吉祥
 * @created: 2021/10/31 15:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespPageBean {
    /**
     * 总条数
     */
    private Long total;
    /**
     * 当前数据
     */
    private List<?> data;
}

