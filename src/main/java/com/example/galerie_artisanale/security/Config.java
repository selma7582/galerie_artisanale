package com.example.galerie_artisanale.security;


import com.example.galerie_artisanale.service.impl.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
/*
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
*/

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class Config extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;


    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    private static final String[] PUBLIC_MATCHERS =  {
            "/css/**",
            "/js/**",
            "/images/**",
            "/imgs/**",
            "/webjars/**",
            "/",
            "/newUser",
            "/forgetPassword",
            "/login",
            "/fonts/**",
            "/galerie",
            "/productDetail",
            "/shoppingCart/cart",
            "/shoppingCart/view",

            "/products/remove"
    };

    @Override
    protected void configure( HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .antMatchers("/product/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().failureUrl("/login?error").defaultSuccessUrl("/")
                .loginPage("/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/?logout").deleteCookies("remember-me").permitAll()
                .and()
                .rememberMe();


    }
/*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }*/

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder);
    }






 /*   @Bean
    public HeaderHttpSessionStrategy HttpSessionStrategy(){

        return new HeaderHttpSessionStrategy();
    }*/
}
