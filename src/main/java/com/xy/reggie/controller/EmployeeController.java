package com.xy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xy.reggie.common.R;
import com.xy.reggie.entity.Employee;
import com.xy.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1、将密码进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2、根据用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3、若没有查询到用户，返回登录失败结果
        if(emp == null){
             return R.error("登录失败，该员工未注册");
        }
        //4、密码比对，若{不一致，则返回登录失败
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败，密码错误");
        }
        //5、查看员工状态，若为已禁用状态，返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("登录失败，该账号已被禁用");
        }
        //6、登录成功，将用户id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清除Session中保存的当前员工得id
        request.getSession().removeAttribute("employee");
        //返回成功信息
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工：{}",employee.toString());
        //设置初始密码123456，并进行md5加密后存储
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

/* 使用MyBatisPlus提供的MyMetaObjectHandler自动填充这些字段

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //获取当前登录用户得ID
        Long empId = (Long) request.getSession().getAttribute("employee");

        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);*/

        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page = {},pagesize = {},name = {}", page, pageSize, name);

        //1、构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //2、构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加一个过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加一个排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //3、执行查询
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
        log.info(employee.toString());

        Long empId = (Long) request.getSession().getAttribute("employee");

/* 使用MyBatisPlus提供的MyMetaObjectHandler自动填充这些字段

        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);*/

        employeeService.updateById(employee);

        return R.success(" 员工信息修改成功");
    }

    /**
     * 根据前端传来的id返回该id对应的员工信息，用于修改员工时的回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("查找失败");
    }
}
