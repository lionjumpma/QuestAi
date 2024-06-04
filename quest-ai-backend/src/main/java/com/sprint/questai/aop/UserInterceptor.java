/**
 * @Author: mayixiang
 * @Date: 2024-05-29
 */

package com.sprint.questai.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * UserInterceptor 是一个 Spring 的拦截器组件，用于在请求处理之前和之后进行特定的操作。
 * 该拦截器设置和清除 userId，模拟用户登录和登出操作。
 */
@Component
@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    /**
     * 构造函数，初始化拦截器实例。
     * 可以在此进行一些初始化操作，例如日志记录。
     */
    public UserInterceptor() {
        super();
        //log.info("UserInterceptor init"); // 初始化时的日志记录
    }

    /**
     * 在处理请求之前调用的方法。
     * 在这里设置一个固定的 userId 到 session 中，模拟用户登录。
     *
     * @param request 当前的 HTTP 请求
     * @param response 当前的 HTTP 响应
     * @param handler 被调用的处理器
     * @return 如果返回 true 则继续处理请求，false 则终止请求
     * @throws Exception 可能抛出的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("设置固定的 userId");
        request.getSession().setAttribute("userId", "12345"); // 设置固定的 userId
        return true; // 返回 true 表示继续处理请求
    }

    /**
     * 在处理请求之后调用的方法，但在视图渲染之前。
     * 这里可以进行一些日志记录或修改 ModelAndView。
     *
     * @param request 当前的 HTTP 请求
     * @param response 当前的 HTTP 响应
     * @param handler 被调用的处理器
     * @param modelAndView 视图模型对象
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, org.springframework.web.servlet.ModelAndView modelAndView) throws Exception {
        log.info("postHandle"); // 请求处理后的日志记录
    }

    /**
     * 在整个请求完成之后调用的方法，通常用于清理资源。
     * 这里清除 session 中的 userId，模拟用户登出。
     *
     * @param request 当前的 HTTP 请求
     * @param response 当前的 HTTP 响应
     * @param handler 被调用的处理器
     * @param ex 处理过程中产生的异常
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("remove userId");
        request.getSession().removeAttribute("userId"); // 清除 session 中的 userId
    }
}
