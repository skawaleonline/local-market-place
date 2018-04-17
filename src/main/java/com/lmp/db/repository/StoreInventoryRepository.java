package com.lmp.db.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.lmp.db.pojo.StoreInventoryEntity;

public interface StoreInventoryRepository
    extends MongoRepository<StoreInventoryEntity, String>{

  public Page<StoreInventoryEntity> findAllByStoreIdIn(List<String> id, Pageable page);

  public Page<StoreInventoryEntity> findAllByStoreIdInAndOnSaleTrue(List<String> id, Pageable page);

  public Page<StoreInventoryEntity> findAllByStoreIdInAndItemIdIn(List<String> storeIds, List<String> ids, Pageable page);

  public Page<StoreInventoryEntity> findAllByStoreIdInAndItemIdInAndOnSaleTrue(List<String> storeIds, List<String> ids,
      Pageable page);

}
