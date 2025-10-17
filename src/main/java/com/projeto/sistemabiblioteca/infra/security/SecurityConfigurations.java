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


    
    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions().disable()) // remover depois de tirar o H2
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                		
                		.requestMatchers("/h2-console/**").permitAll()
                		
                		.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/registro").permitAll()

						.requestMatchers(HttpMethod.GET, "/autores/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/autores").hasRole("ADMINISTRADOR")
						.requestMatchers(HttpMethod.PUT, "/autores/**").hasRole("ADMINISTRADOR")
						.requestMatchers(HttpMethod.DELETE, "/autores/**").hasRole("ADMINISTRADOR")

						.requestMatchers(HttpMethod.GET, "/categorias/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/categorias").hasRole("ADMINISTRADOR")
						.requestMatchers(HttpMethod.PUT, "/categorias/**").hasRole("ADMINISTRADOR")
						.requestMatchers(HttpMethod.DELETE, "/categorias/**").hasRole("ADMINISTRADOR")

						.requestMatchers(HttpMethod.GET, "/editoras/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/editoras").hasRole("ADMINISTRADOR")
						.requestMatchers(HttpMethod.PUT, "/editoras/**").hasRole("ADMINISTRADOR")
						.requestMatchers(HttpMethod.DELETE, "/editoras/**").hasRole("ADMINISTRADOR")

						.requestMatchers(HttpMethod.GET, "/edicoes/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/edicoes").hasRole("ADMINISTRADOR")
						.requestMatchers(HttpMethod.PUT, "/edicoes/**").hasRole("ADMINISTRADOR")
						.requestMatchers(HttpMethod.DELETE, "/edicoes/**").hasRole("ADMINISTRADOR")

						.requestMatchers(HttpMethod.GET, "/idiomas/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/idiomas").hasRole("ADMINISTRADOR")
						.requestMatchers(HttpMethod.PUT, "/idiomas/**").hasRole("ADMINISTRADOR")
						.requestMatchers(HttpMethod.DELETE, "/idiomas/**").hasRole("ADMINISTRADOR")

						.requestMatchers(HttpMethod.POST, "livros/gerenciamento").permitAll()
						
						.requestMatchers(HttpMethod.GET, "/usuarios/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll()
						.requestMatchers(HttpMethod.PUT, "/usuarios/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/usuarios/**").permitAll()
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
