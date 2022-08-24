package com.example.rentaperson.config;

import com.example.rentaperson.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService myUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/user/register").permitAll()
                .antMatchers("/api/v1/user/viewAll").hasAuthority("ADMIN")
                .antMatchers("/api/v1/skill/viewAllSkills").hasAuthority("ADMIN")
                .antMatchers("/api/v1/appointment/viewAll").hasAuthority("ADMIN")
                .antMatchers("/api/v1/appointment/post").hasAuthority("USER")
                .antMatchers("/api/v1/appointment/confirm/{id}").hasAuthority("PERSON")
                .antMatchers("/api/v1/appointment/update").hasAuthority("ADMIN")
                .antMatchers("/api/v1/appointment/delete").hasAuthority("ADMIN")
                .antMatchers("/api/v1/review/viewAll").hasAuthority("ADMIN")
                .antMatchers("/api/v1/review//addReview/{username}").hasAuthority("USER")
                .antMatchers("/api/v1/review/update").hasAuthority("ADMIN")
                .antMatchers("/api/v1/review/delete/{id}").hasAuthority("ADMIN")


                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

}
