package com.xy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.reggie.entity.DishFlavor;
import com.xy.reggie.mapper.DishFlavorMapper;
import com.xy.reggie.service.DishFavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFavorService {
}
