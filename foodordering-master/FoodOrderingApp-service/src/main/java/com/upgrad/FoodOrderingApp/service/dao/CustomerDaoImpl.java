package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDaoImpl implements CustomerDao {


	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public CustomerEntity saveCustomer(CustomerEntity customerEntity) {
		entityManager.persist(customerEntity);
		return  customerEntity;
	}

	@Override
	public CustomerEntity getCustomerByContactNumber(String contactNumber) {
		try {
			return entityManager.createNamedQuery("customerByContactNumber", CustomerEntity.class).setParameter("contactNumber", contactNumber).getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	@Override public CustomerAuthEntity saveCustomerAuth(CustomerAuthEntity customerAuthEntity) {
		entityManager.persist(customerAuthEntity);
		return customerAuthEntity;
	}

	@Override
	public CustomerEntity updateCustomerAuthEntity(CustomerAuthEntity customerAuthEntity) {
		 entityManager.persist(customerAuthEntity);
		 return customerAuthEntity.getCustomer();
	}

	public CustomerAuthEntity getCustomerAuthByAccesstoken(String accesstoken) {
		try {
			return entityManager.createNamedQuery("customerAuthByAccesstoken", CustomerAuthEntity.class).setParameter("accesstoken", accesstoken).getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}
}
