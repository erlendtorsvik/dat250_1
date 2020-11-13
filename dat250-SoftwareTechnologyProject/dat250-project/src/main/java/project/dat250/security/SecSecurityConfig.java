package project.dat250.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {
	
	public SecSecurityConfig() {
		super();
		}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/oauth_login", "/h2-console/*", "/",
						"/poll", "/pollVote", "/polls", "/polls/*/setVotes", "/polls/*/IoT/*", "/styles.css", "/fonts/*").permitAll() //permit all to access some sites
				.anyRequest().authenticated() //Needs authentication to access other sites
				.and()
				.oauth2Login().loginPage("/oauth_login").defaultSuccessUrl("/userAdd",true) //Login method, login screen, successful login screen
				.and()
				.logout().deleteCookies("JSESSIONID").logoutSuccessUrl("/oauth_login") //Logout method, successful logout screen
				.and()
				.headers().frameOptions().disable(); //Necessary for h2-console for debugging
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}