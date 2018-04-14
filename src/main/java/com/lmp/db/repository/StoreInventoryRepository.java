package com.lmp.db.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.lmp.db.pojo.StoreInventoryEntity;

public interface StoreInventoryRepository
    extends MongoRepository<StoreInventoryEntity, String>{

  public Page<StoreInventoryEntity> findAllByStoreId(String id, Pageable page);

  public Page<StoreInventoryEntity> findAllByStoreIdAndOnSaleTrue(String id, Pageable page);

  public Page<StoreInventoryEntity> findAllByStoreIdAndItemIdIn(String id, List<String> ids, Pageable page);

  public Page<StoreInventoryEntity> findAllByStoreIdAndItemIdInAndOnSale(String id, List<String> ids, boolean onSale,
      Pageable page);

  public Page<StoreInventoryEntity> findAllByItemIdIn(List<String> ids, Pageable page);

  public Page<StoreInventoryEntity> findAllByItemIdInAndOnSale(List<String> ids, boolean onSale, Pageable page);

  public Page<StoreInventoryEntity> findAllByStoreIdInAndItemIdIn(List<String> storeIds, List<String> ids, Pageable page);

  public Page<StoreInventoryEntity> findAllByStoreIdInAndItemIdInAndOnSale(List<String> storeIds, List<String> ids,
      boolean onSale, Pageable page);

}
