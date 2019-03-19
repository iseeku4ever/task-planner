package com.application.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.application.service.impl.UserDetailServiceImpl;
/**
 * 
 * @author jokumar
 * @since 2018-03-25
 * 
 * Spring Security Configurations
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Resource
	DataSource dataSource;
	
	@Resource
	UserDetailServiceImpl userDetailServiceImpl;
	
	@Override
	public void configure(AuthenticationManagerBuilder auth)
            throws Exception {
			//Authentication using UserDetailService. It queries the data in DB and authenticates
			auth.userDetailsService(userDetailServiceImpl);
      	}	
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
		//Authorize the h2-console for admin and other endpoints using USER and ADMIN role
        http.authorizeRequests().antMatchers("/*h2-console**").access("hasRole('ROLE_ADMIN')")
        		.antMatchers("/","/*task*/**").access("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
        		.and().
        		formLogin().loginPage("/login").failureUrl("/login?error")
        		.usernameParameter("username").passwordParameter("password")
        		.and()
        		.logout().logoutSuccessUrl("/login?logout");
        http.headers().frameOptions().disable();
		http.httpBasic();
		http.csrf().disable();
		
		
    }

}
