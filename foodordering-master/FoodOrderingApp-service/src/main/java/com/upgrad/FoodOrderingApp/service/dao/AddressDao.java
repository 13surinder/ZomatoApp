package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;

import java.util.List;

/*
 * This AddressDao interface gives the list of all the dao methods that exist in the address dao implementation class.
 * Service class will be calling the dao methods by this interface.
 */
public interface AddressDao {

    AddressEntity saveAddress(AddressEntity addressEntity);
    AddressEntity getAddressByUUID(String addressId);
    CustomerAddressEntity getCustomerByAddress(String addressId);
    CustomerAddressEntity saveCustomerAddress(CustomerAddressEntity customerAddressEntity);
    AddressEntity deleteAddress(AddressEntity addressEntity);
    List<AddressEntity> getAllAddress(CustomerEntity customer);
    StateEntity getStateByUUID(String uuid);
}
