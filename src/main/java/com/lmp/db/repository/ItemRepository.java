package com.lmp.db.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lmp.db.pojo.Item;

public interface ItemRepository extends MongoRepository<Item, String> {

    public Item findByUpc(long upc);
    public List<Item> findAllByUpc(long upc);
    public List<Item> findAllByCategories(String category);
}