package com.richuff.service.Impl;

import com.richuff.anno.Component;
import com.richuff.anno.Scope;
import com.richuff.service.OrderService;

@Component("orderService")
@Scope("singleton")
public class OrderServiceImpl implements OrderService {


    @Override
    public String order() {
        return "this is a order";
    }
}
