package id.endang.hellozk.model;

import java.util.Date;

public class Customer {

	private String accNbr;
	private String accName;
	private String birthday;
	private String address;
	private String email;
	private String phone;
	private String createdDate;

	public Customer() {
		// TODO Auto-generated constructor stub
	}

	
	public Customer(String accNbr, String accName, String birthday,
			String address, String email, String phone) {
		super();
		this.accNbr = accNbr;
		this.accName = accName;
		this.birthday = birthday;
		this.address = address;
		this.email = email;
		this.phone = phone;
		
	}


	public String getAccNbr() {
		return accNbr;
	}

	public void setAccNbr(String accNbr) {
		this.accNbr = accNbr;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getBirthday() {
		return birthday;
	}


	public String getCreatedDate() {
		return createdDate;
	}


	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}


	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}


}
