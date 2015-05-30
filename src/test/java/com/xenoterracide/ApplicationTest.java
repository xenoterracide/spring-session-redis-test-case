package com.xenoterracide;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Base64;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

	@RunWith( SpringJUnit4ClassRunner.class )
	@WebAppConfiguration
	@SpringApplicationConfiguration( classes = { MockServletContext.class, Application.class } )
	public class ApplicationTest {

		private MockMvc mockMvc = null;
		private MockHttpServletRequestBuilder requestBuilder;
		@Autowired private WebApplicationContext context;
		@Autowired private FilterChainProxy springSecurityFilterChain;

		@Before
		public void setup() {
			mockMvc = MockMvcBuilders.webAppContextSetup( context )
					.addFilter( springSecurityFilterChain )
					.build();

			requestBuilder = get( "/" )
					.header( "Authorization", "Basic " + Base64.getEncoder().encodeToString( "admin:admin".getBytes() ) );
		}

		@Test
		public void getSessionToken() throws Exception {
			this.mockMvc.perform( requestBuilder )
					.andExpect( status().is2xxSuccessful() )
					.andExpect( header().string( "X-Auth-Token", notNullValue() ) );
		}

		@Test
		public void getJessionId() throws Exception {
		 // response does not agree with an actual browser request which has a JSESSIONID
			this.mockMvc.perform( requestBuilder )
					.andExpect( status().is2xxSuccessful() )
					.andExpect( cookie().doesNotExist( "JSESSIONID" ) );
		}
	}
