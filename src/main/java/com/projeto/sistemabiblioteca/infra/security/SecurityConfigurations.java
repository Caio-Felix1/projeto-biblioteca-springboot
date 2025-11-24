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
                        
						.requestMatchers(HttpMethod.GET, "/edicoes/ativos").permitAll()
						.requestMatchers(HttpMethod.GET, "/edicoes/buscar-por-autor").permitAll()
						.requestMatchers(HttpMethod.GET, "/edicoes/buscar-por-titulo").permitAll()
						.requestMatchers(HttpMethod.GET, "/edicoes/buscar-por-categoria/*").permitAll()
						.requestMatchers(HttpMethod.GET, "/edicoes/buscar-por-editora/*").permitAll()
						.requestMatchers(HttpMethod.GET, "/edicoes/*").permitAll()
						.requestMatchers(HttpMethod.GET, "/edicoes/filtrar").permitAll()

						.requestMatchers(HttpMethod.GET, "/idiomas/ativos").permitAll()
						
						.requestMatchers(HttpMethod.GET, "/estados/ativos").permitAll()
						.requestMatchers(HttpMethod.GET, "/estados/buscar-por-pais/*").permitAll()
						
						.requestMatchers(HttpMethod.GET, "/paises/ativos").permitAll()
						
						.requestMatchers(HttpMethod.GET, "/imagens/**").permitAll()

						.requestMatchers(HttpMethod.POST, "/email").permitAll()
						
						.anyRequest().authenticated()

                )
                .addFilterAfter(securityFilter, CorsFilter.class)
                .build();
    }

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		//permitindo acesso da api por qualquer fonte assim evitamos erro de cors (cors é um erro que so ocorre com reqs do navegador)
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.addAllowedOriginPattern("*"); // LIBERA TUDO
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.setAllowCredentials(true);

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
