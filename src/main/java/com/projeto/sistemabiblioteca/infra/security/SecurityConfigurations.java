package com.projeto.sistemabiblioteca.infra.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    
	
    SecurityFilter securityFilter;
	
	public SecurityConfigurations(SecurityFilter securityFilter) {
		this.securityFilter = securityFilter;
	}

	// REMOVER APÓS RETIRAR O H2 CONSOLE.
	// Serve para ignorar o filtro quando utilizaro h2.
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web
	      .ignoring()
	      .requestMatchers("/h2-console/**");
	}
    
    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions().disable()) // remover depois de tirar o H2
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                		.requestMatchers("/h2-console/**").permitAll() // remover depois de tirar o H2
                		.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/registro").permitAll()

						.requestMatchers(HttpMethod.GET, "/autores/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/autores").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/autores/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/autores/**").hasRole("ADMIN")

						.requestMatchers(HttpMethod.GET, "/categorias/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/categorias").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/categorias/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/categorias/**").hasRole("ADMIN")

						.requestMatchers(HttpMethod.GET, "/editoras/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/editoras").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/editoras/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/editoras/**").hasRole("ADMIN")
						
						.requestMatchers(HttpMethod.POST, "livros/gerenciamento").permitAll()
                )
                .addFilterAfter(securityFilter, CorsFilter.class)
                .build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
    	CorsConfiguration configuration = new CorsConfiguration();
    	configuration.setAllowedOrigins(List.of("http://localhost:4200"));
    	configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    	configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    	configuration.setExposedHeaders(List.of("Authorization"));
    	
    	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	source.registerCorsConfiguration("/**", configuration);
    	return source;
    }
    
    @Bean
    public AuthenticationManager AuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
