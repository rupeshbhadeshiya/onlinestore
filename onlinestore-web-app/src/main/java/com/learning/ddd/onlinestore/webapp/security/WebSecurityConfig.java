package com.learning.ddd.onlinestore.webapp.security;

public class WebSecurityConfig {
	
}

//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		
//		return http.authorizeHttpRequests(request -> request.anyRequest().authenticated())
//				.httpBasic(Customizer.withDefaults())
//				.build();
//		
////		http
////			.authorizeRequests((requests) -> requests
////				.regexMatchers("/", "/home").permitAll()
////				.anyRequest().authenticated()
////			)
////			.formLogin((form) -> form
////				.loginPage("/login").permitAll()
////			)
////			.logout((logout) -> logout.permitAll());
////
////		return http.build();
//	}
//
//	@Bean
//	public UserDetailsService userDetailsService() {
//		
//		UserDetails consumer = User.withUsername("user").password("password")
//									.roles("CONSUMER").build();
//		
//		UserDetails seller = User.withUsername("admin").password("password")
//									.roles("SELLER").build();
//
//		return new InMemoryUserDetailsManager(consumer, seller);
//	}
//}
