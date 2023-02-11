package com.jun.fruit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jun.fruit.common.BaseContext;
import com.jun.fruit.common.R;
import com.jun.fruit.dto.SetmealDto;
import com.jun.fruit.entity.Category;
import com.jun.fruit.entity.Setmeal;
import com.jun.fruit.entity.SetmealDish;
import com.jun.fruit.service.CategoryService;
import com.jun.fruit.service.SetmealDishService;
import com.jun.fruit.service.SetmealService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : Bojack
 * @date : Created in 11:09 2023.02.09
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 保存新建套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto, HttpServletRequest request) {
        //通过mybatisplus公共字段填充
        Long employeeID = (Long) request.getSession().getAttribute("employeeID");
        //将id储存到线程中的单独域中
        BaseContext.setId(employeeID);

        setmealService.saveAndDish(setmealDto);
        return R.success("套餐创建成功");
    }

    /**
     * 分页数据查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageR(int page, int pageSize, String name) {
        //分页构造器对象
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
//        查询构造器
        LambdaQueryWrapper<Setmeal> setmealLam = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行like模糊查询
        setmealLam.like(name != null, Setmeal::getName, name);
        //添加排序条件，根据更新时间降序排列
        setmealLam.orderByAsc(Setmeal::getUpdateTime);
//        进行查询
        setmealService.page(setmealPage, setmealLam);
        //分页构造器对象
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //对象拷贝
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
//        获取原来分页数据中的Records
        List<Setmeal> records = setmealPage.getRecords();
//        将原数据（setmealPage）中的Records添加到新的分页数据（setmealDtoPage）中，适配浏览器的响应数据
        List<SetmealDto> list = records.stream().map((item) -> {

            SetmealDto setmealDto = new SetmealDto();
//            对象拷贝
            BeanUtils.copyProperties(item, setmealDto);
//            获取菜品种类id
            Long categoryId = item.getCategoryId();
//            根据菜品种类id获取菜品种类名字
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
//            将菜品种类名字赋给setmealDto对象
            if (categoryName != null) {
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
//        将交流菜品种类名字的list集合赋值给setmealDtoPage对象
        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }

    /**
     * 更新套餐状态
     * @param status
     * @param ids
     * @param request
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> sale(@PathVariable int status, String[] ids, HttpServletRequest request) {
        //通过mybatisplus公共字段填充
        Long employeeID = (Long) request.getSession().getAttribute("employeeID");
//        //将id储存到线程中的单独域中
        BaseContext.setId(employeeID);

        for (String id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        }
        return R.success("修改成功");
//        update 表名 set 列名1 = 值1, 列名2 = 值2,... [where 条件];
//        updata setmeal set status=0 where id=ids

//
//        LambdaQueryWrapper<Setmeal> setmealLam = new LambdaQueryWrapper<>();
//
//        setmealLam.in(Setmeal::getId, ids);
//        setmealService.update(setmealLam);
    }
    /**
     * 根据id查询套餐详情，包括菜品
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getwithDishByid(id);
        return R.success(setmealDto);
    }
}
