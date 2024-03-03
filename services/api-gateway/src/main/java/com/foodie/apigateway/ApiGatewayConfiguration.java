package com.foodie.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {
    
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/get")
                        .uri("http://httpbin.org:80"))
                .route(r -> r.path("/api/v1/foodie/**")
                        .filters(f -> f.rewritePath("/api/v1/foodie/(?<segment>.*)", "/${segment}"))
                        .uri("lb://foodie-service"))
                .route(r -> r.path("/api/v1/auth/**")
                        .filters(f -> f.rewritePath("/api/v1/auth/(?<segment>.*)", "/${segment}"))
                        .uri("lb://auth-service"))
                .route(r -> r.path("/api/v1/order/**")
                        .filters(f -> f.rewritePath("/api/v1/order/(?<segment>.*)", "/${segment}"))
                        .uri("lb://order-service"))
                .route(r -> r.path("/api/v1/payment/**")
                        .filters(f -> f.rewritePath("/api/v1/payment/(?<segment>.*)", "/${segment}"))
                        .uri("lb://payment-service"))
                .route(r -> r.path("/api/v1/user/**")
                        .filters(f -> f.rewritePath("/api/v1/user/(?<segment>.*)", "/${segment}"))
                        .uri("lb://user-service"))
                .build();
    }
}
