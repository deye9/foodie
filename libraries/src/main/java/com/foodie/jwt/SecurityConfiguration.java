package com.foodie.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.foodie.models.repositories.PermissionRepository;
import com.foodie.models.users.Permission;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

        private static final String[] WHITE_LIST_URL = {
                        "/api/v1/auth/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html"
        };
        private final LogoutHandler logoutHandler;
        private final JwtAuthenticationFilter jwtAuthFilter;
        private String ApplicationPath = "/api/v1/management/**";
        private final AuthenticationProvider authenticationProvider;
        private @Autowired PermissionRepository permissionRepository;

        public String[] getAllPermissionNames() {
                List<Permission> permissions = permissionRepository.findAll();
                if (permissions.size() == 0) {
                        return new String[] { "ADMIN", "USER", "MANAGER", "RIDER", "FOODIE" };
                } else {
                        List<String> permissionNames = new ArrayList<>();
                        for (Permission permission : permissions) {
                                permissionNames.add(permission.getPermissionName());
                        }
                        return permissionNames.toArray(String[]::new);
                }
        }

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                String[] permissionNames = getAllPermissionNames();
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URL)
                                                .permitAll()
                                                .requestMatchers(ApplicationPath)
                                                .hasAnyRole(permissionNames)
                                                .requestMatchers(GET, ApplicationPath)
                                                .hasAnyAuthority(permissionNames)
                                                .requestMatchers(PUT, ApplicationPath)
                                                .hasAnyAuthority(permissionNames)
                                                .requestMatchers(POST, ApplicationPath)
                                                .hasAnyAuthority(permissionNames)
                                                .requestMatchers(DELETE, ApplicationPath)
                                                .hasAnyAuthority(permissionNames)
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
                                                .addLogoutHandler(logoutHandler)
                                                .logoutSuccessHandler((request, response,
                                                                authentication) -> SecurityContextHolder
                                                                                .clearContext()));

                return http.build();
        }
}
