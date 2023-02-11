package com.jun.fruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.fruit.common.CustomException;
import com.jun.fruit.dto.SetmealDto;
import com.jun.fruit.entity.Setmeal;
import com.jun.fruit.entity.SetmealDish;
import com.jun.fruit.mapper.SetmealMapper;
import com.jun.fruit.service.SetmealDishService;
import com.jun.fruit.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Bojack
 * @date : Created in 0:03 2023.02.08
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 保存新建套餐,套餐信息和菜品信息一起保存
     *
     * @param setmealDto
     */
    @Override
    public void saveAndDish(SetmealDto setmealDto) {
        //b保存套餐信息
        this.save(setmealDto);
        //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<Object> collect = setmealDishes.stream().map((item) -> {
            Long setmealId = setmealDto.getId();
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐
     *
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in [1,2] and staus=1;
//        查询删除套餐中有没有禁售的，有则不执行抛异常
        LambdaQueryWrapper<Setmeal> setmealLam = new LambdaQueryWrapper<>();
        setmealLam.in(Setmeal::getId, ids);
        setmealLam.eq(Setmeal::getStatus, 1);
        int count = this.count(setmealLam);
        if (count > 0) {
            throw new CustomException("该套餐正在售卖，不可删除");
        }
        //删除setmeal
        this.removeByIds(ids);
        //删除关联的菜品（setmeal_dish)
        LambdaQueryWrapper<SetmealDish> setmealDishLam = new LambdaQueryWrapper<>();
        setmealDishLam.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(setmealDishLam);


    }

    /**
     * 根据id查询套餐信息
     *
     * @param id
     */
    @Override
    public SetmealDto getwithDishByid(Long id) {
        //查询套餐基本信息
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        //查询套餐菜品信息
        LambdaQueryWrapper<SetmealDish> setmealDishLam = new LambdaQueryWrapper<>();
        setmealDishLam.eq(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> list = setmealDishService.list(setmealDishLam);

        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }
}
