package com.jun.fruit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jun.fruit.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : Bojack
 * @date : Created in 23:58 2023.02.07
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
