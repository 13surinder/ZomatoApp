package com.upgrad.FoodOrderingApp.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.upgrad.FoodOrderingApp.service.common.ItemType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ItemEntity class contains all the attributes to be mapped to all the fields in item table in the database.
 * All the annotations which are used to specify all the constraints to the columns in the database must be correctly implemented.
 */
@Entity
@Table(name = "item")
public class  ItemEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID", length = 64, nullable = false)
    private String uuid;

    @Column(name = "item_name",nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Integer price;

    @Enumerated
    @Column(nullable = false)
    private ItemType type;

    @JsonIgnore
    @ManyToOne
    @JoinTable(name = "restaurant_item", joinColumns = @JoinColumn(name = "item_id"), inverseJoinColumns = @JoinColumn(
            name = "restaurant_id"))
    private RestaurantEntity restaurant;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "category_item", joinColumns = @JoinColumn(name = "item_id"), inverseJoinColumns = @JoinColumn(
            name = "category_id"))
    private List<CategoryEntity> categories = new ArrayList<>();


    public ItemEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public RestaurantEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantEntity restaurant) {
        this.restaurant = restaurant;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }
}
