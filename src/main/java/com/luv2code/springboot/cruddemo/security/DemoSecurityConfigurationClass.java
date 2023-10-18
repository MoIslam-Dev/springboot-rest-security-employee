package com.luv2code.springboot.cruddemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.logging.Logger;

@Configuration
public class DemoSecurityConfigurationClass {
    // Define a PasswordEncoder bean to handle password hashing
 /*   @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * Creates a UserDetailsManager bean for managing user details, using the provided DataSource.
     *
     * @param //dataSource The DataSource for user details storage.
     * @return A UserDetailsManager instance configured to work with the given DataSource.
     */
    //this wil get the users from the database
/*@Bean
    public   UserDetailsManager userDetailsManager(DataSource dataSource) throws SQLException {
    JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

    userDetailsManager.setUsersByUsernameQuery(
            "SELECT user_id, pw, active FROM members WHERE user_id = ?");
    userDetailsManager.setAuthoritiesByUsernameQuery(
            "SELECT user_id, role FROM roles WHERE user_id = ?");

    // If you want to specify a custom authorities-by-username query, you can do so here:
    // userDetailsManager.setAuthoritiesByUsernameQuery("...");

    return userDetailsManager;
}*/

    @Bean
    public InMemoryUserDetailsManager userDetailsManager(){
        UserDetails islam =
                User.builder()
                        .username("islam")
                        .password("{noop}islam")
                        .roles("Employee")
                        .build();
        UserDetails younes =
                User.builder()
                        .username("younes")
                        .password("{noop}younes")
                        .roles("Employee","Manager")
                        .build();
        UserDetails yasser =
                User.builder()
                        .username("yasser")
                        .password("{noop}yasser")
                        .roles("Employee","Manager","Admin")
                        .build();
        return new InMemoryUserDetailsManager(islam,younes,yasser);
    }
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http
              .authorizeHttpRequests(configurer ->
              configurer
                      .requestMatchers(HttpMethod.GET,"/api/employees").hasRole("Employee")
                      .requestMatchers(HttpMethod.GET,"/api/employees/**").hasRole("Employee")
                      .requestMatchers(HttpMethod.POST,"/api/employees").hasRole("Manager")
                      .requestMatchers(HttpMethod.PUT,"/api/employees").hasRole("Manager")
                      .requestMatchers(HttpMethod.DELETE,"/api/employees/**").hasRole("Admin")
                      .requestMatchers(HttpMethod.DELETE,"/api/employees").hasRole("Admin")

              );
//basic http authentification
   http.httpBasic()
           .and()
           .csrf().disable();
       return http.build();
   }


}
