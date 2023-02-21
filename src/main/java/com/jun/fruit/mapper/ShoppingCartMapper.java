package com.jun.fruit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jun.fruit.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : Bojack
 * @date : Created in 21:24 2023.02.12
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
