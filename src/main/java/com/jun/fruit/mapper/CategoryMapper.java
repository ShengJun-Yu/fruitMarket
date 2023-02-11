package com.jun.fruit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jun.fruit.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : Bojack
 * @date : Created in 22:11 2023.02.07
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
