package com.xy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.reggie.dto.DishDto;
import com.xy.reggie.entity.Dish;
import com.xy.reggie.entity.DishFlavor;
import com.xy.reggie.mapper.DishMapper;
import com.xy.reggie.service.DishFavorService;
import com.xy.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DishServicImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFavorService dishFavorService;

    /**
     * 新增菜品，并保存对应的口味数据
     * @param dishDto
     */
    @Transactional //因为要操作两个表，所以要开启事物
    public void saveWithFlavor(DishDto dishDto) {
        //1、保存菜品信息到菜品表dish
        this.save(dishDto);

        //2、保存菜品口味数据到dishFlover表
        //要对菜品口味取出进行处理，添加菜品id
        Long dishId = dishDto.getId(); //菜品id
        List<DishFlavor> flavors = dishDto.getFlavors(); //菜品口味
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return  item;
        }).collect(Collectors.toList());
        dishFavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息
        Dish dish = this.getById(id);

        //查询菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFavorService.list(queryWrapper);

        //转存数据
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    /**
     * 更新菜品信息
     * @param dishDto
     */
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新菜品信息
        this.updateById(dishDto);

        //清理当前菜品的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFavorService.remove(queryWrapper);

        //插入菜品口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return  item;
        }).collect(Collectors.toList());

        dishFavorService.saveBatch(flavors);


    }
}
