package com.xy.reggie.dto;

import com.xy.reggie.entity.Setmeal;
import com.xy.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
