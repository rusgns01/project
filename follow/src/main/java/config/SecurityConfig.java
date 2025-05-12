package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .authorizeHttpRequests(requests -> requests
                        .anyRequest().permitAll()
                )

                .formLogin(form -> form
                        .defaultSuccessUrl("/index", true)  // 로그인 성공 후 리디렉션될 페이지
                        .permitAll()
                )

                .build();
    }
}
