package com.xy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.reggie.common.CustomException;
import com.xy.reggie.dto.SetmealDto;
import com.xy.reggie.entity.Setmeal;
import com.xy.reggie.entity.SetmealDish;
import com.xy.reggie.mapper.SetmealMapper;
import com.xy.reggie.service.SetmealDishService;
import com.xy.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存套餐和菜品的关联信息
        setmealDishService.saveBatch(setmealDishes);


    }

    /**
     * 删除套餐，同时删除套餐关联数据
     * @param ids
     */
    @Transactional
    public void removeWilthDish(List<Long> ids) {
        //1、查询套餐状态，确定可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //需要构造的SQL语句：select count(*) from setmeal where id in (1,2,3) and status = 1
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1); //status = 1套餐处于起售状态

        int count = this.count(queryWrapper);
        if(count > 0){
            //若不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        //2、若可以删除，先删除套餐表中数据--setmeal表
        this.removeByIds(ids);

        //3、再删除关系表中数据--setmeal_dish表
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //需要构造的SQL语句：delete from setmeal dish where setmeal id in (1,2,3)
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(lambdaQueryWrapper);
    }
}
