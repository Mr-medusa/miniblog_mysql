package red.medusa.miniblog;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import red.medusa.miniblog.intercept.CacheStaticResourceInterceptor;

import javax.sql.DataSource;
import java.nio.charset.Charset;
import java.util.List;

@Configuration
public class MiniBlogConfig implements WebMvcConfigurer {

    @Autowired
    private CacheStaticResourceInterceptor cacheStaticResourceInterceptor;

    /****************************** 解决中文乱码的配置 START*************************************/
    @Bean
    public HttpMessageConverter responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }


    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }


    @Bean
    public MappingJackson2HttpMessageConverter messageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(getObjectMapper());
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
        converters.add(messageConverter());
    }
    /****************************** 解决中文乱码的配置 END*************************************/


    /******************************** 静态资源缓存 START *********************************************************/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cacheStaticResourceInterceptor)
                .addPathPatterns("/index.html", "/css/*.css", "/js/*.js", "/fonts/**", "/img/*.*")    //for mini blog
                .addPathPatterns("/clavichord/**/*.*");        //for clavichord
    }
    /******************************** 静态资源缓存 END *********************************************************/

    /**
     * 跨域过滤器
     * @return
     */
//    private CorsConfiguration buildConfig() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.addAllowedHeader("*");
//        corsConfiguration.addAllowedMethod("*");
//        return corsConfiguration;
//    }
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", buildConfig()); // 4
//        return new CorsFilter(source);
//    }
}