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
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import java.util.Locale;


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
            "/resetPassword",
            "/login",
            "/products",
            "/fonts/**",
            "/galerie","/galerie/**",
            "/contact",
            "/productDetail",
            "/shoppingCart/cart",
            "/shoppingCart/view",
            "/products/remove",
            "/searchProduct",
            "/searchByCategory/**",
            "/searchByDimension/**",
            "/searchByShape/**"
    };

    @Override
    protected void configure( HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .antMatchers("/product/**","/provider/**").hasAnyRole("ADMIN")
                //.antMatchers("/provider/**").hasAnyRole("ADMIN")
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


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder);
    }




  /*  @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.FRANCE);
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    *//*@Override*//*
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }*/
}
