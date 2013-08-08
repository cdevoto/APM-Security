package com.compuware.identity.authentication.repo.jira;

import com.compuware.identity.authentication.repo.jira.webservice.login.Exception_Exception;
import com.compuware.identity.authentication.repo.jira.webservice.login.LoginWebservice;


public class AdminTokenManager {
	private LoginWebservice port;
	private String adminUser;
	private String adminPassword;

	private volatile JiraToken adminToken;
	
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
	
	public JiraToken getToken () throws Exception_Exception  {
		JiraToken current = adminToken;
		if (current != null) {
			return current;
		}
	    return refreshToken(current);
	}
	
	public JiraToken refreshToken(JiraToken old) throws Exception_Exception {
		JiraToken current = adminToken;
		if ((old == null && current == null) || (old != null && old.equals(current))) {
			synchronized (this) {
				current = adminToken;
				if ((old == null && current == null) || (old != null && old.equals(current))) {
					String token = port.login(adminUser, adminPassword);
					if (token == null) {
						throw new Exception_Exception("Admin login failed", null);
					}
					current = adminToken = new JiraToken(token);
				}
			}
		}	
		return current;
	}

}
