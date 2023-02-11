package com.jun.fruit.filter;

import com.alibaba.fastjson.JSON;
import com.jun.fruit.common.BaseContext;
import com.jun.fruit.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : Bojack
 * @date : Created in 17:33 2023.02.06
 * 登入检测，使用过滤器阻止非法登入
 */
@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截器开启 {}", requestURI);
        //定义不需要处理的请求路径
        String[] uris = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };
        //2、判断本次请求是否需要处理
        boolean check = check(uris, requestURI);
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        //4-1、判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employeeID") != null) {
            log.info("用户已登录，本次请求{}不需要处理，用户id为：{}", requestURI, request.getSession().getAttribute("employeeID"));
            //通过mybatisplus公共字段填充
//        获得当前登录用户的id
            Long employeeID = (Long) request.getSession().getAttribute("employeeID");
            //将id储存到线程中的单独域中
            BaseContext.setId(employeeID);

            filterChain.doFilter(request, response);
            return;
        }
        //4-2、判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录，本次请求{}不需要处理，用户id为：{}", requestURI, request.getSession().getAttribute("user"));
            //通过mybatisplus公共字段填充
//        获得当前登录用户的id
            Long userID = (Long) request.getSession().getAttribute("user");
            //将id储存到线程中的单独域中
            BaseContext.setId(userID);
            filterChain.doFilter(request, response);
            return;
        }
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        log.info("未登入本次请求{}已经拦截", requestURI);
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 遍历需要不需要拦截的目录，有返回true
     *
     * @param uris
     * @param requestURI
     * @return
     */
    public static boolean check(String[] uris, String requestURI) {
        for (String uri : uris) {
            if (PATH_MATCHER.match(uri, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
