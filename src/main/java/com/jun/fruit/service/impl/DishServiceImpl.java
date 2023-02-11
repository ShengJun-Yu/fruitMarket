package com.jun.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.fruit.dto.DishDto;
import com.jun.fruit.entity.Dish;
import com.jun.fruit.entity.DishFlavor;
import com.jun.fruit.mapper.DishMapper;
import com.jun.fruit.service.DishFlavorService;
import com.jun.fruit.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Bojack
 * @date : Created in 0:00 2023.02.08
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品信息，同时保存对应的口味数据
     *
     * @param dishDto
     */
    @Transactional//因为要操控两个表，开启事务注解
    @Override
    public void savewithFlavor(DishDto dishDto) {
//        保存菜品
        this.save(dishDto);
//        获取菜品id
        Long dishId = dishDto.getId();
//        获取口味集合
        List<DishFlavor> dishFlavors = dishDto.getFlavors();
//        遍历口味集合，将菜品id加入口味中，使得每个口味都对应有多个菜品
        for (DishFlavor d : dishFlavors) {
            d.setDishId(dishId);
        }
//        dishFlavors = dishFlavors.stream().map((item) -> {
//            item.setDishId(dishId);
//            return item;
//        }).collect(Collectors.toList());

//        将带菜品id的口味集合保存到口味表中
        dishFlavorService.saveBatch(dishFlavors);
    }

    /**
     * * 根据id查询菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getDishAndFlavor(Long id) {
//        先查出dish
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> dishFlavorlqw = new LambdaQueryWrapper<>();
        dishFlavorlqw.eq(DishFlavor::getDishId, dish.getId());

        List<DishFlavor> list = dishFlavorService.list(dishFlavorlqw);
        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     * 保存修改后的菜品信息
     * @param dishDto
     */
    @Override
    @Transactional//因为要操控两个表，开启事务注解
    public void updataFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加当前提交过来的口味数据---dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
//        flavors = flavors.stream().map((item) -> {
//            item.setDishId(dishDto.getId());
//            return item;
//        }).collect(Collectors.toList());


//        遍历口味集合，将菜品id加入口味中，使得每个口味都对应有多个菜品
        for (DishFlavor d :
                flavors) {
            Long id = dishDto.getId();
            d.setDishId(id);
        }
        dishFlavorService.saveBatch(flavors);
    }
}
