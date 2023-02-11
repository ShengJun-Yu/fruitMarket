package com.jun.fruit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.fruit.entity.User;
import com.jun.fruit.mapper.UserMapper;
import com.jun.fruit.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author : Bojack
 * @date : Created in 16:23 2023.02.10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
