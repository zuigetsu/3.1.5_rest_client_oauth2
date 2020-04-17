package client.security;

import client.model.User;
import client.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    private UserDetailsServiceImpl userDetailsServiceImpl;

    public WebSecurityConfig(MyAuthenticationSuccessHandler myAuthenticationSuccessHandler, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.myAuthenticationSuccessHandler = myAuthenticationSuccessHandler;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login").anonymous()
                .antMatchers("/oauth").authenticated()
                .antMatchers("/admin**").hasRole("ADMIN")
                .antMatchers("/").anonymous()
                .antMatchers("/**").hasAnyRole("ADMIN", "USER")
                .and()
                .formLogin().successHandler(myAuthenticationSuccessHandler)
                .and()
                .logout().logoutSuccessUrl("/").permitAll()
                .and()
                .csrf().disable();
    }

    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public PrincipalExtractor principalExtractor() {
        return map -> {
            User user = new User();
            user.setUsername((String) map.get("name"));
            user.setPassword("123");
            return user;
        };
    }
}