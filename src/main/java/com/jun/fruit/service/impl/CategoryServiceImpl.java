package com.jun.fruit.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.fruit.common.CustomException;
import com.jun.fruit.entity.Category;
import com.jun.fruit.entity.Dish;
import com.jun.fruit.entity.Setmeal;
import com.jun.fruit.mapper.CategoryMapper;

import com.jun.fruit.service.CategoryService;
import com.jun.fruit.service.DishService;
import com.jun.fruit.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : Bojack
 * @date : Created in 21:41 2023.02.07
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Dish> dish = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        dish.eq(Dish::getCategoryId, id);
        int dishcount = dishService.count(dish);
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if (dishcount > 0) {
            //要删除的分类中关联了其他菜品抛出一个业务异常
           throw  new CustomException("当前分类下关联了菜品，不能删除");

        }
        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmeal = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        setmeal.eq(Setmeal::getCategoryId, id);
        int setmealcount = setmealService.count(setmeal);
        if (setmealcount > 0) {
            //要删除的分类中关联了其他套餐
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //正常删除分类
        super.removeById(id);

    }
}
