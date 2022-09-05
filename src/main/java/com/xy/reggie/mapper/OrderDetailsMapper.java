package com.xy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.reggie.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailsMapper extends BaseMapper<OrderDetail> {
}
