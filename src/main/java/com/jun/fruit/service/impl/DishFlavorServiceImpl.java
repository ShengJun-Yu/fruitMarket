package com.jun.fruit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.fruit.entity.DishFlavor;
import com.jun.fruit.mapper.DishFlavorMapper;
import com.jun.fruit.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @author : Bojack
 * @date : Created in 17:20 2023.02.08
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}

