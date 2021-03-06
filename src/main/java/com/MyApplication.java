package com;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.nieyue.interceptor.SessionControllerInterceptor;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@SpringBootApplication
@Configuration
//@EnableRedisHttpSession
//@Import({DynamicDataSourceRegister.class})
@ServletComponentScan
@EnableSwagger2
public class MyApplication extends WebMvcConfigurerAdapter implements ApplicationListener<ApplicationReadyEvent> {
	@Resource
	StringRedisTemplate stringRedisTemplate;
	@Resource
	SessionControllerInterceptor sessionControllerInterceptor;
	
	public static void main(String[] args) {
		SpringApplication.run(MyApplication.class,args);
		
	}
	 @Bean
	 public Docket createRestApi(){
		 return new Docket(DocumentationType.SPRING_WEB)
				 .apiInfo(
						 new ApiInfoBuilder()
						 .title("书城应用接口文档")
						 .description("书城应用接口文档1.0版本")
						 .version("1.0")
						 .build())
				 .select()
				 .apis(RequestHandlerSelectors.basePackage("com.nieyue.controller"))
				 .paths(PathSelectors.any())
				 .build();
	 }
	/* @Override
	  public void addCorsMappings(CorsRegistry registry) {
	    registry.addMapping("/**")
	    .allowedOrigins("*")
	    .allowedMethods("POST","GET", "OPTIONS", "DELETE")
	    .allowedHeaders("Origin, X-Requested-With, Content-Type, Accept")
	    .allowCredentials(true) 
	    ;
	  } */
	/*@Bean
    public FilterRegistrationBean indexFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new CORSFilter());
        registration.addUrlPatterns("/*");
        System.out.println(22222333);
        return registration;
    }*/
	  /** 
	     * 配置拦截器 
	     * @author lance 
	     * @param registry 
	     */  
	    public void addInterceptors(InterceptorRegistry registry) {  
	        registry.addInterceptor(sessionControllerInterceptor).addPathPatterns("/**");  
	    } 
	@Bean
	public EmbeddedServletContainerCustomizer  containerCustomizer() {

		return new EmbeddedServletContainerCustomizer() {
	        @Override
	        public void customize(ConfigurableEmbeddedServletContainer container) {
	        	ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
	            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
	            ErrorPage error406Page = new ErrorPage(HttpStatus.NOT_ACCEPTABLE, "/404.html");
	            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/404.html");
	            
	            container.addErrorPages( error401Page);
	            container.addErrorPages( error404Page);
	            container.addErrorPages( error406Page);
	            container.addErrorPages( error500Page);
	        }
	    };
	}
	@Value("${myPugin.projectName}")
	String projectName;
	/**
	 * 容器初始化
	 * @param event
	 */
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		//初始化合伙人收益增量
		/*BoundValueOperations<String, String> bvo=stringRedisTemplate.boundValueOps(projectName+"ScaleIncrement");
		if(bvo.size()<=0){
			bvo.set("0");
		}*/

		
		
	}
	}
