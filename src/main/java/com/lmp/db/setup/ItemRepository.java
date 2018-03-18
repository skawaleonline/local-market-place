package com.lmp.db.setup;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, String> {

    public Item findByUpc(long upc);
    public List<Item> findAllByCategory(String category);

}