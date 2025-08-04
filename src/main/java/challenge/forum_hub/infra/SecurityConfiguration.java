package challenge.forum_hub.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .build();
    }


//        @Autowired
//        private SecurityFilter securityFilter;
//
//        @Bean
//        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//            return
//                    http.csrf(csrf -> csrf.disable())
//                            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                            .authorizeHttpRequests(req -> {
//                                req.requestMatchers("/login").permitAll();
//                                req.anyRequest().authenticated();
//                            })
//                            //prioriza o filtro que criei, senão fizer isso o Spring executa primeiro o dele e nem verifica a autenticação
//                            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
//                            .build();
//
//        }
}
