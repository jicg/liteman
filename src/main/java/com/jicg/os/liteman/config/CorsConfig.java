package com.jicg.os.liteman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author jicg on 2020/12/29
 */

@Configuration
class CorsConfig implements WebMvcConfigurer {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        //添加跨域的cors配置
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").   //可以被跨域的路径,/**表示无限制,
                        allowedOrigins(ALLOWED_ORIGINS). //允许跨域的域名，如果值为*,则表示允许任何域名使用
                        allowedMethods("*"). //允许任何方法，值可以为： "GET", "POST" ...
                        allowedHeaders("*"). //允许任何请求头
                        allowCredentials(true). //允许带cookie信息
                        exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L); //maxAge(3600):表示3600秒内，不需要再发送预检验请求，是结果可以缓存的时长
            }
        };
    }


    //指定允许跨域的多个域
    private static final String[] ALLOWED_ORIGINS = new String[]{
            "http://127.0.0.1:3000","http://localhost:3000"
    };

}
