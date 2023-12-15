package com.wineshop.ecommerce.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/index.html",
                        "/web/pages/catalogue.html", "/web/pages/accessories.html",
                        "/web/pages/details-accessories.html",
                        "/web/css/**", "/web/js/**", "/web/images/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/wines", "/api/accessories", "/api/clients/online").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients", "/api/login", "api/logout").permitAll()
                .antMatchers(HttpMethod.GET, "/web/pages/checkout.html", "/web/pages/orders.html", "/api/clients/current",
                        "/api/purchase/status", "/api/purchase/client").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/purchase").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET, "/api/clients", "/api/purchase/status/admin",
                        "/web/admin/**", "/api/wines/varieties").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/wines/edit/**", "/api/purchase/status").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/wines/create", "/api/accessories/create").hasAuthority("ADMIN")
                .anyRequest().denyAll();


        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "error"));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout").deleteCookies();


        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}
