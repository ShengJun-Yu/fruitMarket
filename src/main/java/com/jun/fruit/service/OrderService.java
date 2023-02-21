package com.jun.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jun.fruit.entity.Orders;
import org.springframework.core.annotation.Order;


public interface OrderService extends IService<Orders> {


    /**
     * 用户下单
     * @param orders
     */
    void saveOrder(Orders orders);
}
