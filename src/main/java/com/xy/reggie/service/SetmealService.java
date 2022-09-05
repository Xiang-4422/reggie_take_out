package com.xy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.reggie.dto.SetmealDto;
import com.xy.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联数据
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时删除套餐关联数据
     * @param ids
     */
    public void removeWilthDish(List<Long> ids);


}
