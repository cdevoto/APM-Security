package com.compuware.identity.authentication.repo.jira;

import com.compuware.identity.authentication.repo.jira.webservice.login.Exception_Exception;
import com.compuware.identity.authentication.repo.jira.webservice.login.LoginWebservice;


public class AdminTokenManager {
	private LoginWebservice port;
	private String adminUser;
	private String adminPassword;

	private volatile LoginServiceToken adminToken;
	
	public AdminTokenManager (LoginWebservice port, String adminUser, String adminPassword) {
		if (port == null) {
			throw new NullPointerException("Expected a port");
		}
		if (adminUser == null) {
			throw new NullPointerException("Expected an adminUser");
		}
		if (adminPassword == null) {
			throw new NullPointerException("Expected an adminPassword");
		}
		this.port = port;
		this.adminUser = adminUser;
		this.adminPassword = adminPassword;
	}
	
	public LoginServiceToken getToken () throws Exception_Exception  {		
		if (adminToken != null) {
			
			// Refresh every minute
								
			return adminToken;
		}
		
	    return refreshToken();
	}
	
	public LoginServiceToken refreshToken() throws Exception_Exception {
		
		String token = port.login(adminUser, adminPassword);
		adminToken = new LoginServiceToken(token);
		return adminToken;
		
	}

}
