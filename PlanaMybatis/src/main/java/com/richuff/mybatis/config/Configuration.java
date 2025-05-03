package com.richuff.mybatis.config;

import com.richuff.mybatis.type.MappedStatement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {
    /**
     * 数据源
     */
    private DataSource dataSource;

    /**
     * mapper.xml文件的内容
     */
    private Map<String, MappedStatement> mappedStatementMap = new ConcurrentHashMap<>();
}
