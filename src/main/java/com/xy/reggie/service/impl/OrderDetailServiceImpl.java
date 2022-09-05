package com.xy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.reggie.entity.OrderDetail;
import com.xy.reggie.mapper.OrderDetailsMapper;
import com.xy.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailsMapper, OrderDetail> implements OrderDetailService {
}
