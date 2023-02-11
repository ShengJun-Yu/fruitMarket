package com.jun.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jun.fruit.dto.DishDto;
import com.jun.fruit.entity.Dish;

/**
 * @author : Bojack
 * @date : Created in 23:59 2023.02.07
 */
public interface DishService extends IService<Dish> {
    /**
     *     * 新增菜品信息，同时保存对应的口味数据
     * @param dishDto
     */
    void savewithFlavor(DishDto dishDto);

    /**
     * * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    DishDto getDishAndFlavor(Long id);

    /**
     * 保存修改后的菜品信息
     * @param dishDto
     */
    void updataFlavor(DishDto dishDto);
}
