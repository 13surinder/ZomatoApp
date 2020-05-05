package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerServiceImpl implements CustomerService {


	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private PasswordCryptographyProvider passwordCryptographyProvider;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerEntity saveCustomer(CustomerEntity customerEntity) {
		//handle the validations



	 CustomerEntity customerEntity1 =  customerDao.getCustomerByContactNumber(customerEntity.getContactNumber());

		if(customerEntity1 != null) {

		}


		valididateEmail(customerEntity.getEmailAddress());

		int contactNumberlength = customerEntity.getContactNumber().length();

//		if(customerDao.getCustomer(customerEntity.getContactNumber() != null) {
//			//throw exception
//		}


		//if validations are okay then save the customer in db;




		String[] encryptPassoword = passwordCryptographyProvider.encrypt(customerEntity.getPassword());

		customerEntity.setSalt(encryptPassoword[0]);
		customerEntity.setPassword(encryptPassoword[1]);

		return customerDao.saveCustomer(customerEntity);

	}


	private boolean valididateEmail(String email) {

		//logic for validating the email.

		if(email == null) {
			// throw exception
		}


		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
				"[a-zA-Z0-9_+&*-]+)*@" +
				"(?:[a-zA-Z0-9-]+\\.)+[a-z" +
				"A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		Matcher matcher = pat.matcher(email);
		if(!matcher.matches()) {
			return false;
		}

		return true;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public CustomerAuthEntity login(String contactNumber, String password) throws AuthenticationFailedException {

		CustomerEntity customerEntity = null;
		customerEntity = customerDao.getCustomerByContactNumber(contactNumber);

		if (customerEntity == null) {
			throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
		}

		final String encryptedPassword = PasswordCryptographyProvider.encrypt(password, customerEntity.getSalt());


		if (true) {
			JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
			CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
			customerAuthEntity.setUuid(UUID.randomUUID().toString());
			customerAuthEntity.setCustomer(customerEntity);
			ZonedDateTime now = ZonedDateTime.now();
			ZonedDateTime expiresAt = now.plusHours(8);

			customerAuthEntity.setLoginAt(ZonedDateTime.now());
			customerAuthEntity.setExpiresAt(expiresAt);
			customerAuthEntity.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));
			return customerDao.saveCustomerAuth(customerAuthEntity);
		} else {
			throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
		}
	}

	public CustomerAuthEntity authorization(String accessToken) throws AuthorizationFailedException {

		CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuthByAccesstoken(accessToken);

		if (customerAuthEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
		}

		if (customerAuthEntity.getLogoutAt() != null) {
			throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
		}

		if (ZonedDateTime.now().isAfter(customerAuthEntity.getExpiresAt())) {
			throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
		}
		return customerAuthEntity;
	}

	@Override
	public CustomerEntity getCustomer(String accessToken) throws AuthorizationFailedException {

		authorization(accessToken);
		CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuthByAccesstoken(accessToken);
		return customerAuthEntity.getCustomer();
	}

	@Override 
	public void logout(String accessToken) throws AuthorizationFailedException {


		CustomerAuthEntity customerAuthEntity = authorization(accessToken);
		customerAuthEntity.setLogoutAt(ZonedDateTime.now());

		customerDao.updateCustomerAuthEntity(customerAuthEntity);



	}
}
