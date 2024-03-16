package com.springsecurity.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    // to cerate password encoder we need to cerate spring security
    private static final String[] WHITELIST_URL={"/hello","/register","/resendToken","/verifyRegistration","/savePassword",
    "/resetPassword"};

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(11);
        // here 11 is strength
    }


    // CorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration configuration = new CorsConfiguration();
    //     configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
    //     configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    //     configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    //     return new CorsConfigurationSource() {
    //     @Override
    //     public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
    //     return configuration;
    //         }
    //     };
    // }



    // enable WHITELIST_URL 

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // diable cors and csrf for post request
        http.cors()
            .and()
            .csrf()
            .disable()
            .authorizeHttpRequests()
            .antMatchers(WHITELIST_URL)
            .permitAll();

        return http.build();

        // http.cors().configurationSource(corsConfigurationSource())
        // .and()
        // .http.csrf().disable()
        // .and()
        // .authorizeRequests()
        // .antMatchers(WHITELIST_URL)
        // .permitAll();

    }
}




