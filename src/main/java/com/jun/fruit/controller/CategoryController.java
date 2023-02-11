package com.jun.fruit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jun.fruit.common.BaseContext;
import com.jun.fruit.common.R;
import com.jun.fruit.entity.Category;
import com.jun.fruit.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : Bojack
 * @date : Created in 22:21 2023.02.07
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 添加新菜品
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> add(HttpServletRequest request, @RequestBody Category category) {
        //通过mybatisplus公共字段填充
        Long employeeID = (Long) request.getSession().getAttribute("employeeID");
        //将id储存到线程中的单独域中
        BaseContext.setId(employeeID);
        categoryService.save(category);
        return R.success("产品添加成功");
    }

    /**
     * 分页查询菜品
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageR(int page, int pageSize) {
        //分页构造器
        Page<Category> pages = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);
        //分页查询
        categoryService.page(pages, queryWrapper);
        return R.success(pages);
    }

    /**
     * 根据菜品id删除菜品,使用自定义删除方法
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id) {
//        调用自定义删除方法
        categoryService.remove(id);
        return R.success("删除成功！");
    }

    /**
     * 修改菜品分类
     *
     * @param request
     * @param category
     * @return
     */
    @PutMapping
    public R<String> updataCate(HttpServletRequest request, @RequestBody Category category) {
        //通过mybatisplus公共字段填充
        Long employeeID = (Long) request.getSession().getAttribute("employeeID");
        //将id储存到线程中的单独域中
        BaseContext.setId(employeeID);
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 根据条件动态查询分类数据
     *
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        //条件构造器
        LambdaQueryWrapper<Category> clqw = new LambdaQueryWrapper<>();
        //添加条件
        clqw.eq(category.getType() != null, Category::getType, category.getType());
//       优先 sort增序排列，UpdateTime降序排列
        clqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(clqw);
        return R.success(list);
    }


}
