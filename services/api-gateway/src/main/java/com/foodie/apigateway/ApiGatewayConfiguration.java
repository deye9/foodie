package com.foodie.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

        @Bean
        RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
                return builder.routes()
                                .route(r -> r.path("/get")
                                                .uri("http://httpbin.org:80"))
                                .route(r -> r.path("/api/v1/auth/**")
                                                .uri("lb://svc-user-management"))
                                .route(r -> r.path("/api/v1/user/**")
                                                .uri("lb://svc-user-management"))
                                .route(r -> r.path("/api/v1/order/**")
                                                .filters(f -> f.rewritePath("/api/v1/order/(?<segment>.*)",
                                                                "/${segment}"))
                                                .uri("lb://order-service"))
                                .build();
        }
}
