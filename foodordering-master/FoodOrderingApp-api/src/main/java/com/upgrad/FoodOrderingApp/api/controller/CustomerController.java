package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upgrad.FoodOrderingApp.api.model.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;


@RestController @RequestMapping("/customer")
public class CustomerController {


	@Autowired private CustomerService custService;

	@RequestMapping(method = RequestMethod.POST, path = "/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<SignupCustomerResponse> signUp(@RequestBody final SignupCustomerRequest signupCustomerRequest)
			throws SignUpRestrictedException {

		//validate this request.


		if (signupCustomerRequest.getLastName().isEmpty()) {
			throw new SignUpRestrictedException("SGR -005", "Enter last name");
		}

		signupCustomerRequest.getContactNumber().length();

		CustomerEntity custEntity = new CustomerEntity();
		custEntity.setContactNumber(signupCustomerRequest.getContactNumber());
		custEntity.setEmailAddress(signupCustomerRequest.getEmailAddress());
		custEntity.setLastname(signupCustomerRequest.getLastName());
		custEntity.setFirstName(signupCustomerRequest.getFirstName());
		custEntity.setPassword(signupCustomerRequest.getPassword());
		custEntity.setSalt("salt");
		custEntity.setUuid(UUID.randomUUID().toString());

		final CustomerEntity resCustomer = custService.saveCustomer(custEntity);
		SignupCustomerResponse signupCustResponse = new SignupCustomerResponse();
		signupCustResponse.setId(resCustomer.getUuid());
		signupCustResponse.setStatus("Customer Registered");
		return new ResponseEntity<SignupCustomerResponse>(signupCustResponse, HttpStatus.CREATED);
	}


	@RequestMapping(method = RequestMethod.POST, path = "/login", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<LoginResponse> login(@RequestHeader("authorization") final String auth)
			throws AuthenticationFailedException {

		//call to service to login

		//Use https://www.base64encode.org/ to create the encoded string

		//pass that encoded string in request header.

		//decode that string and get the password and contact number(On upgrad's portal there is a seperate lecture for this, see that one).

		// After decoding it check if that contact number exist in database if not then throw exception otherwise encrypt the decoded
		// password and check with password if matched then create a random string

		// create one more table customerAuthEntity and store this string and current time inside that table and customer also.

		//set that random string in response headers and return it in response.


		System.out.println("Auth Token " + auth);

		byte[] decode = Base64.getDecoder().decode(auth.split("Basic ")[1]);


		String decodeText = new String(decode);

		if (!decodeText.contains(":")) {
			throw new AuthenticationFailedException("ATH-003",
					"Incorrect format of decoded customer name and password");
		}

		String decodeArray[] = decodeText.split(":");

		CustomerAuthEntity custAuthEntity = custService.login(decodeArray[0], decodeArray[1]);

		CustomerEntity customer = custAuthEntity.getCustomer();


		LoginResponse loginResp = new LoginResponse();
		//create the login response
		loginResp.setContactNumber(customer.getContactNumber());

		HttpHeaders headers = new HttpHeaders();
		headers.add("accessToken", custAuthEntity.getAccessToken());
		List<String> header = new ArrayList<>();
		header.add("accessToken");
		headers.setAccessControlAllowHeaders(header);

		return new ResponseEntity<LoginResponse>(loginResp, headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/logout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization") final String auth)
			throws AuthorizationFailedException {

		String accessToken = auth.split("BearerToken ")[1];

		custService.logout(accessToken);


		LogoutResponse logoutResponse = new LogoutResponse();
		return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
	}


}
