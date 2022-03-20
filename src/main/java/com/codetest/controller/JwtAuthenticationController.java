package com.codetest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codetest.dto.JwtRequest;
import com.codetest.dto.JwtResponse;
import com.codetest.security.JwtUserDetailsService;
import com.codetest.util.JwtTokenUtil;

//security-related
/**
* Copy from https://dzone.com/articles/spring-boot-security-json-web-tokenjwt-hello-world
* @author Raymond
*
*/

@RestController
@CrossOrigin
public class JwtAuthenticationController {
//	@Autowired
//	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST, produces = "application/json")
	public JwtResponse createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return new JwtResponse(token);
		}

	private void authenticate(String username, String password) throws Exception {
		try {
//			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
			// Mock authentication here
			if ("P-0123456789".equals(username)) {
				if ("abc123".equals(password)) {
					
				} else {
					throw new BadCredentialsException("INVALID_CREDENTIALS");
				}
				
			} else if ("P-3323456789".equals(username)) {
				if ("123abc".equals(password)) {
					
				} else {
					throw new BadCredentialsException("INVALID_CREDENTIALS");
				}
			} else {
				throw new Exception("USER_NOT_FOUND");
			}
			
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
