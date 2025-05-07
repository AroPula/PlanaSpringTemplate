package com.richuff.mybatis.util;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GenericTokenHandler {
    private String openToken; //开始标识符 #{
    private String closeToken; //结束标识符 }
    private TokenHandler tokenHandler;

    /**
     * 解析sql语句工具类
     * @param sql 需要解析的sql语句
     * @return 返回处理后的sql语句
     */
    public String parse(String sql){
        //字符串为空，不需要处理
        if (StringUtils.isBlank(sql)){
            return "";
        }
        int start = sql.indexOf(openToken);
        if (start == -1){
            return sql;
        }
        return "";
    }
}
