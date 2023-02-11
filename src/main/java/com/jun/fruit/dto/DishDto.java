package com.jun.fruit.dto;

import com.jun.fruit.entity.Dish;
import com.jun.fruit.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Bojack
 * @date : Created in 18:09 2023.02.08
 */
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>();
    private String categoryName;
    private Integer copies;
}
