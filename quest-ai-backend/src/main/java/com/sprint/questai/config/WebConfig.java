/**
 * @Author: mayixiang
 * @Date: 2024-05-29
 */

package com.sprint.questai.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sprint.questai.aop.UserInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {


    @Override
    public  void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.add(mappingJackson2HttpMessageConverter());
    }

    // 解决序列化空对象问题
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        // 关键代码
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter(mapper);
        return converter;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("拦截器注册");
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/**");
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有的API都允许跨域
                //allow all
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的请求方式
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true); // 是否允许发送Cookie
    }
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}

