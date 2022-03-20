package com.codetest.dto;

import java.io.Serializable;

//security-related
/**
* Copy from https://dzone.com/articles/spring-boot-security-json-web-tokenjwt-hello-world
* @author Raymond
*
*/
public class JwtResponse implements Serializable {

	private static final long serialVersionUID = 6465795452957036571L;
	private final String jwttoken;

	public JwtResponse(String jwttoken) {
		this.jwttoken = jwttoken;
	}

	public String getToken() {
		return this.jwttoken;
	}
}
