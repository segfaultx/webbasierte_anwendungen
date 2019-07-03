package application.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security config class
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    MyBasicAuthenticationPoint authenticationPoint;

    /**
     *
     * @return
     */
    @Bean
    PasswordEncoder getPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * config method, setting in memory users and registers userdetailsservice for db user authentication
     * @param auth
     * @throws Exception
     */
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

    /**
     * config method to set access rights on different uris
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    // Sichtungspage
                    .antMatchers("/sichtung","/sichtung/**").permitAll()
                    // Rest API
                    .antMatchers(HttpMethod.GET,"/rest/**")
                        .permitAll()
                    .antMatchers("/rest/**")
                        .authenticated()
                            .anyRequest().hasRole("MEMBER")
                // Usermanagement
                    .antMatchers("/user*").authenticated()
                            .anyRequest().hasRole("ADMIN")
                    .antMatchers("/h2-console/**").permitAll()
                .and()
                    .csrf().ignoringAntMatchers("/h2-console/**","/rest/**","/rest/sichtungen/**") // h2 db configs
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
                    .permitAll()
                .and()
                    .httpBasic()
                    .authenticationEntryPoint(authenticationPoint);
    }
}
