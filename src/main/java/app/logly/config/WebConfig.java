package app.logly.config;

import app.logly.web.interceptor.AuthenticationInterceptor;
import app.logly.web.interceptor.CommonModelInterceptor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final List<String> STATICS = List.of("/css/**", "logly.ico", "/terms");
    private static final List<String> PUBLICS = List.of("/login", "/register");

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
                .order(1)
                .excludePathPatterns(mergeLists(STATICS, PUBLICS));
        registry.addInterceptor(new CommonModelInterceptor())
                .order(2)
                .excludePathPatterns(mergeLists(STATICS, PUBLICS));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST");
    }

    @SafeVarargs
    private static List<String> mergeLists(List<String>... lists) {
        List<String> result = new ArrayList<>();
        for (List<String> list : lists) {
            result.addAll(list);
        }
        return result;
    }
}
