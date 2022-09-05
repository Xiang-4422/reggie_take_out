package com.xy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
