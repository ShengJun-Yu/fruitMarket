package com.jun.fruit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jun.fruit.entity.Dish;
import com.jun.fruit.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : Bojack
 * @date : Created in 17:19 2023.02.08
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
}
