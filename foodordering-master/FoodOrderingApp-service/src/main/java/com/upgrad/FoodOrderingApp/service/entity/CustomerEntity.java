package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity @Table(name = "CUSTOMER")
@NamedQueries({
		@NamedQuery(name = "customerByContactNumber", query = "select c from CustomerEntity c where c.contactNumber =:contactNumber")
})
public class CustomerEntity implements Serializable {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;

	@Column(name = "firstname", nullable = false) private String firstName;

	@Column(name = "uuid", nullable = false)
	private String uuid;

	@Column(name = "lastname", nullable = false) private String lastname;

	@Column(name = "email", nullable = false) private String emailAddress;

	@Column(name = "password", nullable = false) private String password;

	@Column(name = "contact_number", nullable = false) private String contactNumber;

	@Column(name = "salt", nullable = false) private String salt;

	public CustomerEntity() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}



