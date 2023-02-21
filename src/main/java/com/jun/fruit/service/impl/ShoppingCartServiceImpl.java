package com.jun.fruit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.fruit.entity.ShoppingCart;
import com.jun.fruit.mapper.ShoppingCartMapper;
import com.jun.fruit.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author : Bojack
 * @date : Created in 21:42 2023.02.12
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
