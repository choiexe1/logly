package app.logly.config;

import app.logly.web.interceptor.AuthenticationInterceptor;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private static final List<String> STATICS = List.of("/css/**", "logly.ico", "/terms");
    private static final List<String> PUBLICS = List.of("/login", "/register");

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
                .order(1)
                .excludePathPatterns(mergeLists(STATICS, PUBLICS))
                .addPathPatterns("/avatar/**");
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
