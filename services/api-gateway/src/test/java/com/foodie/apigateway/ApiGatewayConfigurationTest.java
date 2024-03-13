package com.foodie.apigateway;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApiGatewayConfigurationTest {

    @Mock
    private RouteLocatorBuilder builder;

    @Mock
    private RouteLocatorBuilder.Builder routes;

    @InjectMocks
    private ApiGatewayConfiguration apiGatewayConfiguration;

    @Test
    public void testGatewayRoutes() {
        when(builder.routes()).thenReturn(routes);
        when(routes.route(any())).thenReturn(routes);
        when(routes.build()).thenReturn(mock(RouteLocator.class));

        RouteLocator result = apiGatewayConfiguration.gatewayRoutes(builder);

        assertNotNull(result);
        verify(builder).routes();
        verify(routes, times(4)).route(any());
        verify(routes).build();
    }
}