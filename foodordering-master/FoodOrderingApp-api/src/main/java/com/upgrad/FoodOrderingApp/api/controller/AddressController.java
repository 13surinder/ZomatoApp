package com.upgrad.FoodOrderingApp.api.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;
import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.http.ResponseEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;


@RestController
public class AddressController {

	@Autowired
	private CustomerService cS;

	@Autowired
	private AddressService aS;
	
	@RequestMapping(method = RequestMethod.POST, path = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<SaveAddressResponse> saveAddress(@RequestBody(required = false) final SaveAddressRequest saveAddressRequest, @RequestHeader("authorization") final String authorization) throws
			AuthorizationFailedException, SaveAddressException, AddressNotFoundException {
		String accessToken = authorization.split("Bearer ")[1];

		//validate the user first using this
		CustomerEntity cEntity = cS.getCustomer(authorization);

		//Creating AddressEntity object
		final AddressEntity aEntity = new AddressEntity();

		aEntity.setFlatBuilNo(saveAddressRequest.getFlatBuildingName());
		aEntity.setLocality(saveAddressRequest.getLocality());
		aEntity.setCity(saveAddressRequest.getCity());
		aEntity.setPincode(saveAddressRequest.getPincode());
		// aEntity.setState(aS.getStateByUUID(saveAddressRequest.getStateUuid()));
		aEntity.setUuid(UUID.randomUUID().toString());
		

		//Creating CustomerAddressEntity object
		final CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();
		customerAddressEntity.setAddress(aEntity);
		customerAddressEntity.setCustomer(cEntity);

		//Creating AddressEntity object
		final AddressEntity createdAddressEntity = aS.saveAddress(aEntity,customerAddressEntity);

		//Creating SaveAddressResponse object
		SaveAddressResponse saveAddressResponse = new SaveAddressResponse().id(createdAddressEntity.getUuid()).status("ADDRESS SUCCESSFULLY REGISTERED");

		//Returning ResponseEntity object
		return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<AddressListResponse> getAllAddresses(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
		String accessToken = authorization.split("Bearer ")[1];
		CustomerEntity cEntity = cS.getCustomer(accessToken);
		List<AddressEntity> aEntityList  = aS.getAllAddress(cEntity);
		AddressListResponse aListResponse = new AddressListResponse();
		for (AddressEntity aEntity : aEntityList){
			AddressList aList = new AddressList();
			aList.setId(UUID.fromString(aEntity.getUuid()));
			aList.setLocality(aEntity.getLocality());
			aList.setCity(aEntity.getCity());
			aList.setFlatBuildingName(aEntity.getFlatBuilNo());
			aList.setPincode(aEntity.getPincode());
			AddressListState aListState = new AddressListState();
			aListState.setId(UUID.fromString(aEntity.getState().getUuid()));
			aListState.setStateName(aEntity.getState().getStateName());
			aList.setState(aListState);
			aListResponse.addAddressesItem(aList);
		}
		//Returning ResponseEntity object
		return new ResponseEntity<AddressListResponse>(aListResponse, HttpStatus.OK);
	}


}
