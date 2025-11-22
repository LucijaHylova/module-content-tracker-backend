package com.bfh.moduletracker.ai.config;


import java.util.List;

import com.bfh.moduletracker.ai.repository.UserRepository;
import com.bfh.moduletracker.ai.service.auth.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

// TODO https://medium.com/@dilankacm/spring-security-architecture-explained-with-jwt-authentication-example-spring-boot-5cc583a9aeac
// TODO https://medium.com/@tericcabrel/implement-jwt-authentication-in-a-spring-boot-3-application-5839e4fd8fac
// TODO https://medium.com/@AlexanderObregon/cookie-based-auth-in-spring-boot-without-using-sessions-d795c1d530e0

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService,
                                                           UserDetailsService userDetailsService
    ) {
        return new JwtAuthenticationFilter(jwtService, userDetailsService);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http

                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.GET, "/", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/ai/**", "/modules/all", "/content/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/authenticate").permitAll()
                        .requestMatchers(HttpMethod.POST, "test/reset", "vectorStore/load").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/ai/compare/analysis").hasAnyRole("USER_WITH_PROFILE")
                        .requestMatchers(HttpMethod.GET, "/users/all", "/users/all/details", "/users/me/details", "/users/get", "/users/get/details").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/me").hasAnyRole("ADMIN", "USER_WITH_PROFILE")
                        .requestMatchers(HttpMethod.PUT, "/users/me/updateUser").hasAnyRole("USER_WITH_PROFILE")
                        .requestMatchers(HttpMethod.PUT, "/userModules/me/addUserModule", "/userModules/me/updateUserModule").hasAnyRole("USER_WITH_PROFILE")
                        .requestMatchers(HttpMethod.DELETE, "/userModules/me/deleteUserModule").hasAnyRole("USER_WITH_PROFILE")
                        .requestMatchers(HttpMethod.DELETE, "/users/module-comparison/delete").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/logout").hasAnyRole("ADMIN", "USER_WITH_PROFILE")
                        .requestMatchers(HttpMethod.PUT, "/auth/update").hasAnyRole("ADMIN", "USER_WITH_PROFILE")
                        .requestMatchers(HttpMethod.PUT, "/auth/delete").hasAnyRole("ADMIN", "USER_WITH_PROFILE")

                        .requestMatchers(HttpMethod.POST, "/modules/import").hasAnyRole("ADMIN_IMPORT")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:3000", "http://localhost:8082"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Access-Control-Request-Method", "*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Set-Cookie"));
  //      configuration.addAllowedOriginPattern("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


}

