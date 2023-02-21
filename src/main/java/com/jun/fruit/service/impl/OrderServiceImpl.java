package com.jun.fruit.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.fruit.common.BaseContext;
import com.jun.fruit.common.CustomException;
import com.jun.fruit.entity.*;
import com.jun.fruit.mapper.OrderMapper;
import com.jun.fruit.service.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 下订单
     *
     * @param orders
     */
    @Override
    @Transactional
    public void saveOrder(Orders orders) {
//        获取下单用户id
        Long userid = BaseContext.getId();

        //查询用户地址
//        LambdaQueryWrapper<AddressBook> addressBookLam = new LambdaQueryWrapper<>();
//        addressBookLam.eq(AddressBook::getUserId, userid);
//        AddressBook addressBook = addressBookService.getOne(addressBookLam);
        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if(addressBook == null){
            throw new CustomException("用户地址信息有误，不能下单");
        }
        //查询购物车信息
        LambdaQueryWrapper<ShoppingCart> shoppingCartLam = new LambdaQueryWrapper<>();
        shoppingCartLam.eq(ShoppingCart::getUserId, userid);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartLam);
        if (shoppingCartList == null&&shoppingCartList.size()==0){
            throw new CustomException("未添加菜品，无法下单！");
        }
        //获取用户信息
        User user = userService.getById(userid);
        //生成订单号
        long orderId = IdWorker.getId();
//        原子操作，在多线程情况下也能保持不错
        AtomicInteger amount = new AtomicInteger(0);
        //将购物车信息添加到订单明细中
        List<OrderDetail> orderDetails = shoppingCartList.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            /**
             * item.getAmount()：获取单品金额
             * multiply：乘
             * item.getNumber()：菜品数量
             */
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        orders.setUserId(userid);
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);

        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userid);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入数据，一条数据
        this.save(orders);

        //向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);

        //清空购物车数据
        shoppingCartService.remove(shoppingCartLam);

    }
}
