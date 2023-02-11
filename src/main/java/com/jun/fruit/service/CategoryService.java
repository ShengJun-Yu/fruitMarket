package com.jun.fruit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jun.fruit.entity.Category;
import org.springframework.stereotype.Service;

/**
 * @author : Bojack
 * @date : Created in 21:41 2023.02.07
 */

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
