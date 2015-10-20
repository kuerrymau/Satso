package com.satso.assessment;

public final class ChangePasswordRequest {
	private String newPassword;
	private String username;
	private String password;

	public ChangePasswordRequest(String newPassword, String username, String password) {
		this.newPassword = newPassword;
		this.username = username;
		this.password = password;
	}

	public String getNewPassword(){
		return newPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
