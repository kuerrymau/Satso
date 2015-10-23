package com.satso.assessment;

public class ChangePasswordRequest extends LoginRequest{
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

	public String getPassword() {
		return password;
	}
}
