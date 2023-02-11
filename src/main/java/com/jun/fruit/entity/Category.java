package com.jun.fruit.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : Bojack
 * @date : Created in 21:29 2023.02.07
 */
@Data
public class Category {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Integer type;//类型 1 菜品分类 2 套餐分类
    private String name;//分类名称
    private int sort;//顺序

    @TableField(fill = FieldFill.INSERT) //插入时填充字段
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充字段
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT) //插入时填充字段
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充字段
    private Long updateUser;

}
