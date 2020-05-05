package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

public interface CustomerDao {

	CustomerEntity saveCustomer(CustomerEntity customerEntity);

	CustomerAuthEntity getCustomerAuthByAccesstoken(String accesstoken);
	CustomerEntity getCustomerByContactNumber(String contactNumber);
	CustomerAuthEntity saveCustomerAuth(CustomerAuthEntity customerAuthEntity);

	CustomerEntity updateCustomerAuthEntity(CustomerAuthEntity customerAuthEntity);

}
