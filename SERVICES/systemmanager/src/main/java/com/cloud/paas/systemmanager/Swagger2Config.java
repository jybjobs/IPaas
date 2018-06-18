package com.cloud.paas.systemmanager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cloud.paas.systemmanager"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket testApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("test")
//                .genericModelSubstitutes(DeferredResult.class)
//                .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")// base，最终调用接口后会和paths拼接在一起
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cloud.paas.systemmanager"))
                .build()
                .apiInfo(testApiInfo());
    }

    @Bean
    public Docket demoApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("demo")
//                .genericModelSubstitutes(DeferredResult.class)
//                .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")// base，最终调用接口后会和paths拼接在一起
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cloud.paas.systemmanager"))
                .build()
                .apiInfo(demoApiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("paas-acp-api")
                .termsOfServiceUrl("http://blog.didispace.com/")
                .contact(": abner\t\t E-mail: 15366181381@189.cn")
                .version("1.0")
                .build();
    }

    private ApiInfo testApiInfo() {
        return new ApiInfoBuilder()
                .title("test-api")
                .termsOfServiceUrl("http://blog.didispace.com/")
                .contact(": abner\t\t E-mail: 15366181381@189.cn")
                .version("1.0")
                .build();
    }

    private ApiInfo demoApiInfo() {
        return new ApiInfoBuilder()
                .title("demo-api")
                .termsOfServiceUrl("http://blog.didispace.com/")
                .contact(": abner\t\t E-mail: 15366181381@189.cn")
                .version("1.0")
                .build();
    }


}

