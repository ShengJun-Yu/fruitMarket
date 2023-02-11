package com.jun.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jun.fruit.dto.SetmealDto;
import com.jun.fruit.entity.Setmeal;

import java.util.List;

/**
 * @author : Bojack
 * @date : Created in 0:02 2023.02.08
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 保存新建套餐,套餐信息和菜品信息一起保存
     * @param setmealDto
     */
    void saveAndDish(SetmealDto setmealDto);

    /**
     * 删除套餐
     * @param ids
     */
    void removeWithDish(List<Long> ids);

    /**
     * 根据id查询套餐信息
     * @param id
     */
    SetmealDto getwithDishByid(Long id);
}
