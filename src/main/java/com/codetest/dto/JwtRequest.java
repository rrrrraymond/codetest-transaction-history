package com.codetest.dto;

import java.io.Serializable;

//security-related
/**
* Copy from https://dzone.com/articles/spring-boot-security-json-web-tokenjwt-hello-world
* @author Raymond
*
*/
public class JwtRequest implements Serializable {

	private static final long serialVersionUID = -5698631144529462776L;
	private String username;
	private String password;

//need default constructor for JSON Parsing
	public JwtRequest() {

	}

	public JwtRequest(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
