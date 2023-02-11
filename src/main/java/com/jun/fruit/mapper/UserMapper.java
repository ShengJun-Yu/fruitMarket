package com.jun.fruit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jun.fruit.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : Bojack
 * @date : Created in 16:22 2023.02.10
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
