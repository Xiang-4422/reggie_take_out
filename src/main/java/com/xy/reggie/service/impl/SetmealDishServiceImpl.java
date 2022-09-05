package com.xy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.reggie.entity.SetmealDish;
import com.xy.reggie.mapper.SetmealDishMapper;
import com.xy.reggie.mapper.SetmealMapper;
import com.xy.reggie.service.SetmealDishService;
import com.xy.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
