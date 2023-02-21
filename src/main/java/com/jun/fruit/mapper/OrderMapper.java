package com.jun.fruit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jun.fruit.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}