package com.compuware.identity.authentication.repo.jira;

public class LoginServiceToken {
	private String token;
	private long issued;
	
	public LoginServiceToken (String token) {
		this.token = token;
		this.issued = System.nanoTime();
	}

	public String getToken() {
		return token;
	}

	public long getIssued() {
		return issued;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (issued ^ (issued >>> 32));
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {				
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoginServiceToken other = (LoginServiceToken) obj;
		if (issued != other.issued)
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "JiraToken [token=" + token + ", issued=" + issued + "]";
	}
}
