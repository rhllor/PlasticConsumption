package com.github.rhllor.pc.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final RequestMatcher _publicUrls = new OrRequestMatcher(
        new AntPathRequestMatcher("/login")
    );
    private static final RequestMatcher _protectedUrls = new NegatedRequestMatcher(_publicUrls);

    @Autowired
    private TokenAuthenticationProvider _authenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(_authenticationProvider);
    }

    @Override 
    public void configure(WebSecurity web) {
        web.ignoring().requestMatchers(_publicUrls);
        web.ignoring().antMatchers("/v3/api-docs/**", 
                                   "/swagger-ui/**", 
                                   "/swagger-ui.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .exceptionHandling()
            .defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), _protectedUrls).and()
            .authenticationProvider(_authenticationProvider)
            .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
            .authorizeRequests()
            .anyRequest()
            .authenticated().and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .logout().disable();

    }

    @Bean
    TokenAuthenticationFilter restAuthenticationFilter() throws Exception {
        TokenAuthenticationFilter filter = new TokenAuthenticationFilter(_protectedUrls);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    
    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(new NoRedirectStrategy());
        return successHandler;
    }

    @Bean
    FilterRegistrationBean<TokenAuthenticationFilter> disableAutoRegistration(TokenAuthenticationFilter filter) {
        FilterRegistrationBean<TokenAuthenticationFilter> registration = new FilterRegistrationBean<TokenAuthenticationFilter>(filter);
        registration.setEnabled(false);
        return registration;
    }
    
    @Bean
    AuthenticationEntryPoint forbiddenEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
    }
}
