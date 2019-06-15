package application.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder pwenc = getPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(pwenc.encode("geheim"))
                .roles("ADMIN","USER")
        .and()
                .withUser("test")
                .password(pwenc.encode("depp"))
                .roles("GUCKER","USER")
        .and()
            .withUser("h2")
            .password("h2")
            .roles("USER","ADMIN");
        auth
                .userDetailsService(myUserDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/sichtung","/sichtung/**").permitAll()
                    .antMatchers("/rest/**").permitAll()
                    .antMatchers("/user*").authenticated()
                    .anyRequest().hasRole("ADMIN")
                    .antMatchers("/h2-console/**").permitAll()
                .and()
                    .csrf().ignoringAntMatchers("/h2-console/**") // h2 db configs
                .and()
                    .headers().frameOptions().sameOrigin() // h2 db configs
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/sichtung")
                    .failureUrl("/login")
                    .permitAll()
                .and()
                    .logout()
                    .logoutSuccessUrl("/login")
                    .permitAll();
    }
}
