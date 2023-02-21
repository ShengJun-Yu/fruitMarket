package com.jun.fruit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jun.fruit.common.BaseContext;
import com.jun.fruit.common.R;
import com.jun.fruit.entity.ShoppingCart;
import com.jun.fruit.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Bojack
 * @date : Created in 21:46 2023.02.12
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 购物车添加菜品或者套餐
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        //设置用户id
        Long userid = BaseContext.getId();
        //绑定下单用户
        shoppingCart.setUserId(userid);

        LambdaQueryWrapper<ShoppingCart> shoppingCartLam = new LambdaQueryWrapper<>();
        shoppingCartLam.eq(ShoppingCart::getUserId, userid);
        //判断添加的是菜品还是套餐
        if (shoppingCart.getDishId() != null) {
            //添加的为菜品
            shoppingCartLam.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            //添加的为套餐
            shoppingCartLam.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //获取购物车对象
        ShoppingCart cart = shoppingCartService.getOne(shoppingCartLam);
        if (cart != null) {
            //如果存在加一
            log.info("执行加1");
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartService.updateById(cart);
        } else {
            //如果不存在则增加一个
            log.info("执行新建");
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cart = shoppingCart;
        }
        return R.success(cart);
    }

    /**
     * 购物车回显物品
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("执行购物车回显");
        Long userid = BaseContext.getId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLam = new LambdaQueryWrapper<>();
        shoppingCartLam.eq(ShoppingCart::getUserId, userid);
        shoppingCartLam.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> cartList = shoppingCartService.list(shoppingCartLam);
        return R.success(cartList);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        Long userid = BaseContext.getId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLam = new LambdaQueryWrapper<>();
        shoppingCartLam.eq(ShoppingCart::getUserId, userid);
        if (shoppingCart.getDishId() != null) {
            //表明删除的是菜品
            shoppingCartLam.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            shoppingCartLam.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(shoppingCartLam);
        if (one.getNumber() > 1) {
            one.setNumber(one.getNumber() - 1);
            shoppingCartService.updateById(one);
            return R.success(one);
        } else {
            shoppingCartService.removeById(one);
            return R.success(new ShoppingCart());
        }

    }

    @DeleteMapping("/clean")
    public R<String> delete() {
        LambdaQueryWrapper<ShoppingCart> shoppingCartLam = new LambdaQueryWrapper<>();
        shoppingCartLam.eq(ShoppingCart::getUserId, BaseContext.getId());
        shoppingCartService.remove(shoppingCartLam);
        return R.success("删除成功");
    }
}
