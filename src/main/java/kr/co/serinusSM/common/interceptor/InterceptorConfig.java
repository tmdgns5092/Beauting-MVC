package kr.co.serinusSM.common.interceptor;

//import com.mhlab.cb.component.interceptors.SessionCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        // Logger Interceptor
        registry.addInterceptor(new LoggerInterceptor())
                .addPathPatterns("/*");
        // Login Session Interceptor
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/*")
                .excludePathPatterns(Arrays.asList(
                        "/index",
                        "/introduce",
                        "/brand",
                        "/manual",
                        "/pricing",
                        "/event",
                        "/servicecenter",
                        "/login",
                        "/autoLoginCheck",
                        "/joinTerms",
                        "/joinManager",
                        "/loginChoice",
                        "/addShop",
                        "/singUpManager",
                        "/loginCheckAjax",
                        "/overlapCheckFromIdAjax",
                        "/managerComNUmberCheck",
                        "/overlapShopCheckFromIdAjax",
                        "/BaseService/joinMember",
                        "/BaseService/checkIsMember",
                        "/singUpManagerAjax",
                        "/createShop",
                        "/robots.txt",
                        "/jandi",
                        "/MessageService/getStatesBatchCheck",
                        "/MessageService/sendAutoMessageBatchs",
                        "/Reservation/testLogin",
                        "/requestLogin",
                        "/Mobile/loginCheckAjax",
                        "/Mobile/scheduleCall",
                        "/BaseService/joinMember",
                        "/questionSend"
                ));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
}
