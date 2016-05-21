package cn.xyz.chaos.validator.test.entity;

import java.util.Date;

public class UserDTO {

	private String	logUsername;
	private String	logPassword;
	private String	logPassword2;
	private String	name;
	private int		gender;
	private Date	birthday;
	private String	mobile;
	private String	phone;

	public UserDTO(String logUsername, String logPassword, String logPassword2, String name, int gender, Date birthday,
			String mobile, String phone) {
		super();
		this.logUsername = logUsername;
		this.logPassword = logPassword;
		this.logPassword2 = logPassword2;
		this.name = name;
		this.gender = gender;
		this.birthday = birthday;
		this.mobile = mobile;
		this.phone = phone;
	}

	public String getLogUsername() {
		return logUsername;
	}

	public void setLogUsername(String logUsername) {
		this.logUsername = logUsername;
	}

	public String getLogPassword() {
		return logPassword;
	}

	public void setLogPassword(String logPassword) {
		this.logPassword = logPassword;
	}

	public String getLogPassword2() {
		return logPassword2;
	}

	public void setLogPassword2(String logPassword2) {
		this.logPassword2 = logPassword2;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

}
