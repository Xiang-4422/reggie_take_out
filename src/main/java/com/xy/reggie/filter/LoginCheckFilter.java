package com.xy.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.xy.reggie.common.BaseContext;
import com.xy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //Spring提供的路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //将请求和回应强转
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1、获取本次请求得URI
        String requestURI = request.getRequestURI();
        //2、判断本次请求是否需要处理
        //定义不需要处理得请求路径
        String[] urls = new String[]{
                //后端登录登出
                "/employee/login",
                "/employee/logout",
                //前后端网页的静态资源
                "/backend/**",
                "/front/**",
                //移动端的登录
                "/user/sendMsg",
                "/user/login"
        };
        boolean check = check(urls, requestURI);
        //3、若不需要处理，则直接放行
        if(check){
            filterChain.doFilter(request, response);
            return;
        }
        //4-1、判断登录状态，若已经登陆，则直接放行
        if(request.getSession().getAttribute("employee") != null){

            //将用户id保存到当前线程的ThreadLocal变量中，便于在其他的类中能够获取到用户id
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }
        //4-2、判断登录状态，若已经登陆，则直接放行
        if(request.getSession().getAttribute("user") != null){

            //将用户id保存到当前线程的ThreadLocal变量中，便于在其他的类中能够获取到用户id
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);
            return;
        }
        //5、若未登录，则返回未登录结果,通过输出流得方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI){
        for(String url : urls){
            if(PATH_MATCHER.match(url, requestURI)){
                return true;
            }
        }
        return false;
    }
}
