package com.jun.fruit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jun.fruit.common.BaseContext;
import com.jun.fruit.common.R;
import com.jun.fruit.dto.DishDto;
import com.jun.fruit.entity.Category;
import com.jun.fruit.entity.Dish;
import com.jun.fruit.entity.DishFlavor;
import com.jun.fruit.service.CategoryService;
import com.jun.fruit.service.DishFlavorService;
import com.jun.fruit.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Bojack
 * @date : Created in 16:58 2023.02.08
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品信息，同时保存对应的口味数据
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody DishDto dishDto) {
        //通过mybatisplus公共字段填充
        Long employeeID = (Long) request.getSession().getAttribute("employeeID");
        //将id储存到线程中的单独域中
        BaseContext.setId(employeeID);

        log.info(dishDto.toString());
        //自定义的带口味的菜品表
        dishService.savewithFlavor(dishDto);
        return R.success("新增加菜品成功");
    }

    /**
     * 执行分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageDish(int page, int pageSize, String name) {

        Page<Dish> dishPage = new Page<>(page, pageSize);
//        通过构造器查找到了带categoryId的dishPage,但页面需要categoryName
        LambdaQueryWrapper<Dish> dishlqw = new LambdaQueryWrapper<>();
        dishlqw.like(name != null, Dish::getName, name);
        dishlqw.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage, dishlqw);
        //        创建一个新的page
        Page<DishDto> dishDtoPage = new Page<>();
//        将原来的dishPage拷贝到新的dishDtoPage中去除dishPage中的Records，因为dishPage不带categoryName
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
//        将dishPage取出Records放在List<Dish>，因为他带categoryI
        List<Dish> dishPageRecords = dishPage.getRecords();

        List<DishDto> list = dishPageRecords.stream().map((item) -> {
            DishDto dishDto = new DishDto();
//            将带属性的item拷贝到新的dishDto
            BeanUtils.copyProperties(item, dishDto);
//            取出categoryId
            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
//        将dishDtoPage取出空的Records放在List<DishDto>，
//        List<DishDto> dishDtoPageRecords=new ArrayList<>();
//        dishDtoPageRecords = dishDtoPage.getRecords();
////        遍历dishPageRecords取出categoryId
//        for (Dish dish : dishPageRecords) {
//            DishDto dishDto = new DishDto();
////        将带属性的dish拷贝到新的dishDto
//            BeanUtils.copyProperties(dish, dishDto);
////            取出categoryId
//            Long categoryId = dish.getCategoryId();
////            通过categoryId在数据库查到category
//            Category category = categoryService.getById(categoryId);
//            if (category!=null){
////                通过category获取到categoryName
//                String categoryName = category.getName();
//                //            将获取的category存入dishDto
//                dishDto.setCategoryName(categoryName);
////            向dishDtoPage取出空的Records集合List<DishDto>中添加带categoryName的Records
//                dishDtoPageRecords.add(dishDto);
//            }
//
//        }
//        添加dishDtoPage的Records，此时的Records带ategoryName
//        dishDtoPage.setRecords(dishDtoPageRecords);
        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询口味和菜品
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto andFlavor = dishService.getDishAndFlavor(id);
        return R.success(andFlavor);
    }

    /**
     * v
     * 保存修改后的菜品信息
     *
     * @return
     */
    @PutMapping
    public R<String> updata(@RequestBody DishDto dishDto) {
        dishService.updataFlavor(dishDto);
        return R.success("修改成功");
    }

    /**
     * 根据CategoryId查询菜品分类下的菜品名字,添加功能要完成user页面的首页显示，返回DishDto数据
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> dishlqw = new LambdaQueryWrapper<>();
        dishlqw.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        dishlqw.eq(Dish::getStatus, 1);
        //添加排序条件
        dishlqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getCreateTime);
        List<Dish> list = dishService.list(dishlqw);

        List<DishDto> dishDtoList = new ArrayList<>();

//        BeanUtils.copyProperties(list, dishDtoList);

        for (Dish d : list) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(d,dishDto);
            //添加套餐名字
            Long categoryId = d.getCategoryId();
            Category category = categoryService.getById(categoryId);
            dishDto.setCategoryName(category.getName());
//            添加口味信息
            Long dishid = d.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLam = new LambdaQueryWrapper<>();
            dishFlavorLam.eq(dishid != null, DishFlavor::getDishId, dishid);
            List<DishFlavor> dishFlavor = dishFlavorService.list(dishFlavorLam);
            dishDto.setFlavors(dishFlavor);
            dishDtoList.add(dishDto);
        }
        return R.success(dishDtoList);
    }
}
