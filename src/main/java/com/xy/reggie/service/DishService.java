package com.xy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.reggie.dto.DishDto;
import com.xy.reggie.entity.Dish;


public interface DishService extends IService<Dish> {

    //新增菜品，并插入菜品对应的口味数据，需要操作dish和dishFavor两种张表
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品和菜品口味信息
    public void updateWithFlavor(DishDto dishDto);
}
