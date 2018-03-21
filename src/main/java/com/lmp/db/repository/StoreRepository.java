package com.lmp.db.repository;


import java.util.List;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.lmp.db.pojo.Store;

public interface StoreRepository extends MongoRepository<Store, String> {

  public Store findByName(String name);

  public List<Store> findAllByFranchise(String franchise);

  public GeoResults<Store> findByLocationWithin(Circle circle);

  public GeoResults<Store> findByLocationNear(Point point, Distance distance);
}