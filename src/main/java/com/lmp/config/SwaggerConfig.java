//package com.lmp.config;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//    @Bean
//    public Docket apiDocket() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                .paths(regex("/*"))
//                .build();
//    }
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Employee API Example")
//                .description("A implementation of an API Gateway for Keyhole Labs Microservice Reference.")
//                .contact(new Contact("Keyhole Software", "keyholesoftware.com", "asktheteam@keyholesoftware.com"))
//                .version("2.0")
//                .build();
//    }
//}