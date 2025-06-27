package com.richuff.mybatis.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BoundSql {
    //需要执行的sql语句
    private String sqlText;
    //执行sql语句参数的集合
    private List<String> parameterMappingList;
}
