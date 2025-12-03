package com.example.restapp.GestorFinanciero.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserDetailsService jwtUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    public static final String ADMIN = "ADMIN";
    public static final String USUARIO = "USUARIO";
    

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",  
                "http://miapp-frontend-bucket.s3-website-us-east-1.amazonaws.com"  
        ));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {}) 
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/login").permitAll()
                        
                        .requestMatchers(HttpMethod.POST, "/usuarios/registro").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios/xp").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/usuarios/asignarNivel").authenticated()
                        .requestMatchers(HttpMethod.GET, "/usuarios/usuario/nivel").hasAuthority(USUARIO)
                        .requestMatchers(HttpMethod.GET, "/usuarios/**").hasAuthority(ADMIN)
                        

                       

                        .requestMatchers(HttpMethod.POST, "/presupuestos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/presupuestos/mis-presupuestos").hasAuthority(USUARIO)


                        .requestMatchers(HttpMethod.POST, "/reportes/crear-reporte").hasAuthority(USUARIO)

                        .requestMatchers(HttpMethod.GET, "/transacciones/**").hasAuthority(USUARIO)
                        .requestMatchers(HttpMethod.GET, "/transacciones//transacciones/presupuesto/{idPresupuesto}").hasAuthority(USUARIO)
                        .requestMatchers(HttpMethod.POST, "/transacciones/**").hasAuthority(USUARIO)
                        .requestMatchers(HttpMethod.DELETE, "/transacciones/transacciones/{id}").hasAuthority(USUARIO)
                        

                        .requestMatchers(HttpMethod.GET, "/metas/transacciones/meta/{idMeta}").hasAuthority(USUARIO)
                        .requestMatchers(HttpMethod.POST, "/metas/*/metas/**").hasAuthority(USUARIO)
                        .requestMatchers(HttpMethod.GET, "/metas/**").hasAuthority(USUARIO)
                        .requestMatchers(HttpMethod.PUT, "/meta/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/meta/misMetas/").authenticated()

                        .requestMatchers(HttpMethod.GET, "/categorias/metas/").authenticated()
                        .requestMatchers(HttpMethod.GET, "/categorias/tipo-metas/").authenticated()
                        .requestMatchers(HttpMethod.GET, "/categorias/estado-metas/").authenticated()
                        

                        .requestMatchers(HttpMethod.GET, "/niveles/**").authenticated()
                        
                        .requestMatchers(HttpMethod.GET, "/trofeos/mi-lista-trofeos").hasAuthority(USUARIO)
                        .requestMatchers(HttpMethod.GET, "/trofeos/usuario/ultimo-trofeo").hasAuthority(USUARIO)
                        .requestMatchers(HttpMethod.GET, "/trofeos/**").hasAuthority(ADMIN)


                        .requestMatchers("/usuarios/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
