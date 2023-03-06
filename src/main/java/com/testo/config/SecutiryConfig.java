package com.testo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.testo.security.JwtAuthenticationEntryPoint;
import com.testo.security.JwtAuthenticationFilter;
import com.testo.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecutiryConfig {
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	// private JwtAuthenticationEntryPoint unauthorizedHandler = new
	// JwtAuthenticationEntryPoint();

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	// private JwtAuthenticationFilter jwtAuthenticationFilter = new
	// JwtAuthenticationFilter();

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeHttpRequests().requestMatchers(HttpMethod.GET, "/api/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/**").permitAll().requestMatchers(HttpMethod.PUT, "/api/**")
				.permitAll().requestMatchers(HttpMethod.DELETE, "/api/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll().requestMatchers(HttpMethod.GET,
						"/api/users/checkUsernameAvailability", "/api/users/checkEmailAvailability")
				.permitAll().anyRequest().authenticated();

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

//	public void configure(AuthenticationManagerBuilder builder) throws Exception {
//		builder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
//	}
//
//	@Bean(BeanIds.AUTHENTICATION_MANAGER)
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
