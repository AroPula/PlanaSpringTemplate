package com.richuff.mybatis.util;

import lombok.Data;

import java.util.List;

@Data
public class ParameterMappingTokenHandler implements TokenHandler{
    private List<ParameterMapping> parameterMappingList;

    @Override
    public void handlerToken(String content) {
        ParameterMapping parameterMapping = new ParameterMapping(content);
        parameterMappingList.add(parameterMapping);
    }
}
