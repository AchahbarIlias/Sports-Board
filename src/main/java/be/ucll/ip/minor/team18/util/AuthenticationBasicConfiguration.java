package be.ucll.ip.minor.team18.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class AuthenticationBasicConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth
                .inMemoryAuthentication()
                .withUser("user")
                .password(encoder.encode("t"))
                .roles("USER")
                .and()
                .withUser("admin")
                .password(encoder.encode("t"))
                .roles("USER", "ADMIN");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/bus/all").hasRole("USER")
                .antMatchers("/bus/search").hasRole("USER")
                .antMatchers("/team/all").hasRole("USER")
                .antMatchers("/team/search").hasRole("USER")
                .antMatchers("/bus/all/*").hasRole("USER")
                .antMatchers("/team/all/*").hasRole("USER")
                .antMatchers("/bus/showBusesWithDepartureLocation").hasRole("USER")
                .antMatchers("/bus/showBusesWithNumberOfSeatsBetween").hasRole("USER")
                .antMatchers("/bus/showBusesWithSeatsAbove").hasRole("USER")
                .antMatchers("/team/showTeamsWithMaximumAge").hasRole("USER")
                .antMatchers("/team/showTeamsWithNameThatContainsString").hasRole("USER")
                .antMatchers("/team/showTeamsWithAgeAbove").hasRole("USER")
                .antMatchers("/bus/*").hasRole("ADMIN")
                .antMatchers("/team/*").hasRole("ADMIN")
                .antMatchers("/login*").permitAll()
                .antMatchers("/home*").permitAll()
                // added for h2 access via browser
                .antMatchers("/console/**").permitAll()
                .and()
                .formLogin().loginPage("/login")
                .and()
                .logout().deleteCookies("JSESSIONID")//@author Lucas en Gerben
                .permitAll()
                .and()
                .rememberMe().key("uniqueAndSecret") //@author Lucas en Gerben
                .and()
                // added for CSFR (post add not working problem in demo for example)
                .csrf().disable()
                .httpBasic();
        http.headers().frameOptions().disable();
    }
}