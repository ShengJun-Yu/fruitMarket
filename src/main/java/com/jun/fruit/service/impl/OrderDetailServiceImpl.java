package com.jun.fruit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.fruit.entity.OrderDetail;
import com.jun.fruit.mapper.OrderDetailMapper;
import com.jun.fruit.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}