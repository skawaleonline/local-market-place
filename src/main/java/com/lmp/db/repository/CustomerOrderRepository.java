package com.lmp.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lmp.db.pojo.CustomerOrderEntity;

public interface CustomerOrderRepository extends MongoRepository<CustomerOrderEntity, String> {

}
