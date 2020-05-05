package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(AddressEntity aEntity, CustomerAddressEntity custAddEntity) throws
			SaveAddressException {
            //validations

        if(aEntity.getPincode().isEmpty() || aEntity.getFlatBuilNo().isEmpty()) {
            throw new SaveAddressException("SAR-001", "No Fiels Can be empty");
        }

        addressDao.saveAddress(aEntity);
        saveCustomerAddress(custAddEntity);
        return  aEntity;
    }


    @Override
    public AddressEntity getAddressByUUID(String addressId, CustomerEntity customerEntity) throws
			AuthorizationFailedException, AddressNotFoundException {
     //   if(addressDao.getAddressByUUID(addressId) == null) throw new AddressNotFoundException("ANF-003", "No address by this id");
     //   else if(addressDao.getCustomerByAddress(addressId).getCustomer() != customerEntity) throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
        return addressDao.getAddressByUUID(addressId);
    }



    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAddressEntity saveCustomerAddress(CustomerAddressEntity custAddEntity) {
        return addressDao.saveCustomerAddress(custAddEntity);
    }


    @Override
    public List<AddressEntity> getAllAddress(CustomerEntity cust)  {
        return addressDao.getAllAddress(cust);
    }

    @Override
    public StateEntity getState(String stateUUID) {
        return addressDao.getStateByUUID(stateUUID);
    }


}
