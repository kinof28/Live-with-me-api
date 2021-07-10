package com.Abdelwahab.Live.With.ME.configuration;

import com.Abdelwahab.Live.With.ME.filter.JWTFilter;
import com.Abdelwahab.Live.With.ME.services.AdminDetailsServiceImp;
import com.Abdelwahab.Live.With.ME.services.UserDetailsServiceImp;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
//@SpringBootConfiguration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    JWTFilter jwtFilter;
    UserDetailsServiceImp userDetailsService;
    AdminDetailsServiceImp adminDetailsService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.adminDetailsService);
        auth.userDetailsService(this.userDetailsService);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/api/v0/public/**").permitAll()
//                .antMatchers("/api/v0/client/**").hasRole("CLIENT")
//                .antMatchers("/api/v0/manager/**").hasRole("MANAGER")
//                .antMatchers("/api/v0/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated().and().exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
