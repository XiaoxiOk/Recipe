package com.xi.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.strategy.SaStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Autowired
    public void rewriteSaStrategy() {
        // 重写Sa-Token的注解处理器，增加注解合并功能
        SaStrategy.me.getAnnotation = (element, annotationClass) -> {
            return AnnotatedElementUtils.getMergedAnnotation(element, annotationClass);
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，定义详细认证规则
        registry.addInterceptor(
                new SaInterceptor(handler -> {
            // 指定一条 match 规则
//            SaRouter
//                    // 排除掉的 path 列表，可以写多个
//                    .notMatch(new ArrayList<String>(Arrays.asList(
//                            "/user/doLogin","/admin/doLogin",
//                            "/swagger-resources/**","/webjars/**","/v2/**","/swagger-ui.html/**","/doc.html/**")))
//
//                    .check(r -> StpUtil.checkLogin());        // 要执行的校验动作，可以写完整的 lambda 表达式

            // 根据路由划分模块，不同模块不同鉴权
       //     SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
            //    SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));

        }).isAnnotation(true)).addPathPatterns("/**")
        //开放knife4接口文档、登录接口
        .excludePathPatterns("/**/doc.*",
                "/**/swagger-ui.*",
                "/**/swagger-resources",
                "/**/webjars/**",
                "/**/v2/api-docs/**",
                "/admin/doLogin",
                "/user/doLogin");
    }
}
