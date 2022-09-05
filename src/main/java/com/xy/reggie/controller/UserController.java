package com.xy.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xy.reggie.common.R;
import com.xy.reggie.entity.User;
import com.xy.reggie.service.UserService;
import com.xy.reggie.utils.SMSUtils;
import com.xy.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.javassist.tools.rmi.StubGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 * 用户管理
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送短信登录验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //生成随机的四位验证码，并保存用于稍后的登录验证
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            session.setAttribute(phone, code);
            //调用阿里云的短信服务发送短信
            SMSUtils.sendMessage("阿里云短信测试" ,"SMS_154950909", phone, code);

            return R.success("短信验证码发送成功");
        }
        return R.error("短信验证码发送失败");
    }

    /**
     * 移动端用户登录,使用阿里云短信验证
     * @param map
     * @param session
     * @return
     */
/*    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        //获取手机号
        String phone = map.get("phone").toString();
        //获取用户提交的验证码
        String code = map.get("code").toString();
        //从Session中获取保存的验证码
        String codeInSession = (String) session.getAttribute(phone);
        //验证码比对
        if(codeInSession != null && codeInSession.equals(code)){
            //获取用户数据
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            //判断手机号是否已经注册，若未注册，则进行注册
            if(user == null){ //新用户
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            //登录成功再session中保存用户id(放行页面)
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }*/

    /**
     * 无验证登录
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R<String> login(@RequestBody Map map, HttpSession session){
        //获取手机号
        String phone = map.get("phone").toString();
        //获取用户数据
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = userService.getOne(queryWrapper);
        //保存用户id在session中
        session.setAttribute("user", user.getId());
        return R.success("登录成功");
    }
}
