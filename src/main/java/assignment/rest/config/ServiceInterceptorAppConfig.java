package assignment.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import assignment.rest.securite.ServiceInterceptor;
@Component
public class ServiceInterceptorAppConfig implements WebMvcConfigurer{
	 @Autowired
	 ServiceInterceptor serviceInterceptor;

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serviceInterceptor).addPathPatterns("/**");
    }
}
