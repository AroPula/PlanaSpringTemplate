package com.richuff.mybatis.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MappedStatement {
    /**
     * 唯一标识
     */
    private String id;

    /**
     * 返回值类型
     */
    private String resultType;

    /**
     * 参数类型
     */
    private String parameterType;

    /**
     * sql语句
     */
    private String sql;
}
