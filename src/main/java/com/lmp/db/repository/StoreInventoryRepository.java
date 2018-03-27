package com.lmp.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.lmp.db.pojo.StoreInventory;

public interface StoreInventoryRepository extends MongoRepository<StoreInventory, String> {

  public Page<StoreInventory> findAllByStoreId(String id, Pageable page);
}
