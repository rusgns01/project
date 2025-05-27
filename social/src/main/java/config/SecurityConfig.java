package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
//                .oauth2Login(oauth2 -> oauth2
//                        .authorizationEndpoint(endpoint ->
//                                endpoint.authorizationRequestRepository(new CookieOAuth2AuthorizationRequestRepository())))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/home",
                                        "/oauth/**", "/login/oauth/**", "oauth2/**",
                                        "/oauth2/authorization/**",
                                        "/login/oauth2/code/**", "/error").permitAll()

                        .anyRequest().authenticated()
                )
                .formLogin(auth -> auth
                        .loginPage("/login")
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")  // 로그인 페이지를 지정할 수 있음
                        .defaultSuccessUrl("/home", true)
                )
                .csrf(auth -> auth.disable())

                .build();
    }
}
