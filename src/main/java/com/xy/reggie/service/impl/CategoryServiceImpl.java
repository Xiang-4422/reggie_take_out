package com.xy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.reggie.common.CustomException;
import com.xy.reggie.entity.Category;
import com.xy.reggie.entity.Dish;
import com.xy.reggie.entity.Setmeal;
import com.xy.reggie.mapper.CategoryMapper;
import com.xy.reggie.service.CategoryService;
import com.xy.reggie.service.DishService;
import com.xy.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类，在删除之前检查该分类是否关联了菜品
     * @param id
     */
    @Override
    public void remove(Long id) {
        //构造一个查询对象，并添加查询条件，根据分类id查询
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);

        //查询当前分类是否关联菜品，若关联，则抛出业务异常
        int countDish = dishService.count(dishLambdaQueryWrapper);
        if(countDish > 0){
            //已关联菜品，抛出异常
            throw new CustomException("当前分类关联了菜品，不能删除");
        }

        //构造一个查询对象，并添加查询条件，根据分类id查询
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        //查询当前分类是否关联套餐，若关联，则抛出业务异常
        int countSetmeal = setmealService.count(setmealLambdaQueryWrapper);
        if(countSetmeal > 0){
            //已关联菜品，抛出异常
            throw new CustomException("当前分类 关联了套餐，不能删除");
        }

        //若上述都没有关联，则正常删除分类
        super.removeById(id);
    }
}
