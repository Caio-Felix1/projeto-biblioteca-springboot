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
						.requestMatchers(HttpMethod.POST, "/autores").permitAll()
						.requestMatchers(HttpMethod.PUT, "/autores/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/autores/**").permitAll()

						.requestMatchers(HttpMethod.GET, "/categorias/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/categorias").permitAll()
						.requestMatchers(HttpMethod.PUT, "/categorias/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/categorias/**").permitAll()

						.requestMatchers(HttpMethod.GET, "/editoras/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/editoras").permitAll()
						.requestMatchers(HttpMethod.PUT, "/editoras/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/editoras/**").permitAll()

						.requestMatchers(HttpMethod.GET, "/edicoes/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/edicoes").permitAll()
						.requestMatchers(HttpMethod.PUT, "/edicoes/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/edicoes/**").permitAll()

						.requestMatchers(HttpMethod.GET, "/idiomas/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/idiomas").permitAll()
						.requestMatchers(HttpMethod.PUT, "/idiomas/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/idiomas/**").permitAll()

						.requestMatchers(HttpMethod.GET, "/titulos/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/titulos/**").permitAll()
						.requestMatchers(HttpMethod.PUT, "/titulos/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/titulos/**").permitAll()

						.requestMatchers(HttpMethod.GET, "/exemplares/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/exemplares").permitAll()
						.requestMatchers(HttpMethod.PUT, "/exemplares/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/exemplares/**").permitAll()

						.requestMatchers(HttpMethod.POST, "livros/gerenciamento").permitAll()
						
						.requestMatchers(HttpMethod.GET, "/usuarios/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll()
						.requestMatchers(HttpMethod.PUT, "/usuarios/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/usuarios/**").permitAll()
						
						.requestMatchers(HttpMethod.GET, "/enderecos/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/enderecos/**").permitAll()
						.requestMatchers(HttpMethod.PUT, "/enderecos/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/enderecos/**").permitAll()
						
						.requestMatchers(HttpMethod.GET, "/estados/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/estados/**").permitAll()
						.requestMatchers(HttpMethod.PUT, "/estados/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/estados/**").permitAll()
						
						.requestMatchers(HttpMethod.GET, "/paises/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/paises/**").permitAll()
						.requestMatchers(HttpMethod.PUT, "/paises/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/paises/**").permitAll()

						
						.requestMatchers(HttpMethod.GET, "/emprestimos/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/emprestimos/**").permitAll()
						.requestMatchers(HttpMethod.PUT, "/emprestimos/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/emprestimos/**").permitAll()
						
						.requestMatchers(HttpMethod.GET, "/multas/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/multas/**").permitAll()
						.requestMatchers(HttpMethod.PUT, "/multas/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/multas/**").permitAll()
						
						.requestMatchers("/imagens/**").permitAll()



						.requestMatchers(HttpMethod.POST, "/email").permitAll()

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
