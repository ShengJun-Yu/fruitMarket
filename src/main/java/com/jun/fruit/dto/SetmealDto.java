package com.jun.fruit.dto;

import com.jun.fruit.entity.Setmeal;
import com.jun.fruit.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
