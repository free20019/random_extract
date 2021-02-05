package com.twkf.config;

import com.twkf.filter.LoginHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @author: xianlehuang
 * @Description:
 * @date: 2020/4/29 - 11:31
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns(
                "/","/error","/vehicleRepair/login","/vehicleRepair/index","/vehicleRepair/logout"
                ,"/asserts/**","/webjars/**"
                ,"/doc.html/**","/swagger-resources/**","/v2/**"
                ,"/css/**","/fonts/**","/img/**","/js/**","/*.*"
        );
    }

}
