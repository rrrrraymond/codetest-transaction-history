package com.codetest.security;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//security-related
/**
* Copy from https://www.javainuse.com/spring/boot-jwt
* @author Raymond
*
*/
@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Mock authentication here
		if ("P-0123456789".equals(username)) {
			return new User("P-0123456789", "$2a$12$hhlf8GS9d8VkD.cYLVnh4Omck1oORYxuooonSkmnuunISLEVR2JPe",
					new ArrayList<>());
		} else if ("P-3323456789".equals(username)) {
			return new User("P-3323456789", "$2a$12$KWml.1YeTgx0k4LphllZmeqLjZ63srxhDn/iVb2utVgxubQBHqEei",
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}
