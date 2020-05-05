package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.upgrad.FoodOrderingApp.api.model.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurant")
public class ResturantController {

	@Autowired
	private RestaurantService restaurantServices;

	@Autowired
	private ItemService itemService;

	@RequestMapping(method = RequestMethod.GET, path = "/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<RestaurantDetailsResponse> getRestaurantDetails(@PathVariable("restaurant_id") String restaurantId) throws
			RestaurantNotFoundException {

		RestaurantEntity restEntity = restaurantServices.restaurantByUUID(restaurantId);

      //It might be possible that getCategories will return empty list
		List<CategoryEntity> restCategoryEntityList = restEntity.getCategories();


		RestaurantDetailsResponseAddressState restDetailsResponseAddressState = new RestaurantDetailsResponseAddressState().id(
				UUID.fromString(restEntity.getAddress().getState().getUuid())).stateName(restEntity.getAddress().getState().getStateName());
		RestaurantDetailsResponseAddress restDetailsResponseAddress = new RestaurantDetailsResponseAddress().id(UUID.fromString(restEntity.getAddress().getUuid())).flatBuildingName(restEntity.getAddress().getFlatBuilNo()).city(restEntity.getAddress().getCity()).
				locality(restEntity.getAddress().getLocality()).pincode(restEntity.getAddress().getPincode()).state(restDetailsResponseAddressState);
		RestaurantDetailsResponse restDetailsResponse = new RestaurantDetailsResponse().id(UUID.fromString(restEntity.getUuid())).restaurantName(restEntity.getRestaurantName()).
				averagePrice(restEntity.getAvgPrice()).customerRating(BigDecimal.valueOf(restEntity.getCustomerRating())).numberCustomersRated(restEntity.getNumberCustomersRated()).
				photoURL(restEntity.getPhotoUrl()).address(restDetailsResponseAddress);




		for (CategoryEntity categEntity : restCategoryEntityList) {

			CategoryList categList = new CategoryList().id(UUID.fromString(categEntity.getUuid())).categoryName(categEntity.getCategoryName());

			List<ItemEntity> categItemEntities = itemService.getItemsByCategoryAndRestaurant(restaurantId, categEntity.getUuid());

			for (ItemEntity itemEntity : categItemEntities) {

				ItemList item_list = new ItemList().id(UUID.fromString(itemEntity.getUuid())).itemName(itemEntity.getItemName()).itemType(ItemList.ItemTypeEnum.valueOf(itemEntity.getType().name())).
						price(itemEntity.getPrice());
				categList.addItemListItem(item_list);
			}
			restDetailsResponse.addCategoriesItem(categList);
		}
		return new ResponseEntity<RestaurantDetailsResponse>(restDetailsResponse, HttpStatus.OK);
	}
}
