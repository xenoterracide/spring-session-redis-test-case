package com.xenoterracide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Application {

	@RequestMapping( "/" )
	public String greeting() {
		return "hello";
	}


	@Configuration
	@Order( SecurityProperties.ACCESS_OVERRIDE_ORDER )
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {


		@Bean
		static SessionRepository<? extends ExpiringSession> repository() {
			return new MapSessionRepository( );
		}


		@Bean
		static HttpSessionStrategy httpSessionStrategy() {
			return new HeaderHttpSessionStrategy();
		}

		@Autowired
		void globalUserDetails( final AuthenticationManagerBuilder auth ) throws Exception {
			auth.inMemoryAuthentication().withUser( "admin" ).password( "admin" ).roles( "USER", "ADMIN" );
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.httpBasic()
					.and()
					.authorizeRequests()
					.anyRequest().authenticated();
		}
	}

	public static void main( final String[] args ) {
		SpringApplication app = new SpringApplication( Application.class );
		app.setShowBanner( false );
		app.run( args );
	}
}
