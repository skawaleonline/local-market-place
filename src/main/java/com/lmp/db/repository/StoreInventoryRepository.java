package com.lmp.db.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.lmp.db.pojo.StoreInventory;

public interface StoreInventoryRepository extends MongoRepository<StoreInventory, String> {

  public Page<StoreInventory> findAllByStoreId(String id, Pageable page);

  public Page<StoreInventory> findAllByStoreIdAndOnSale(String id, boolean onSale, Pageable page);

  public Page<StoreInventory> findAllByStoreIdAndItemIdIn(String id, List<String> ids, Pageable page);

  public Page<StoreInventory> findAllByStoreIdAndItemIdInAndOnSale(String id, List<String> ids, boolean onSale, Pageable page);

  public Page<StoreInventory> findAllByItemIdIn(List<String> ids, Pageable page);

  public Page<StoreInventory> findAllByItemIdInAndOnSale(List<String> ids, boolean onSale, Pageable page);

  public Page<StoreInventory> findAllByStoreIdInAndItemIdIn(List<String> storeIds, List<String> ids, Pageable page);

  public Page<StoreInventory> findAllByStoreIdInAndItemIdInAndOnSale(List<String> storeIds, List<String> ids, boolean onSale, Pageable page);
  
}
