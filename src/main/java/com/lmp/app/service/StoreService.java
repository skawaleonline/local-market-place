package com.lmp.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.lmp.db.pojo.Store;
import com.lmp.db.repository.StoreRepository;

@Service
public class StoreService {

  @Autowired
  private StoreRepository repo;

  @Cacheable("stores-around")
  public GeoResults<Store> getStoresAround(double lat, double lng, int radius) {
    GeoResults<Store> stores = repo.findByLocationNear(new Point(lng, lat), new Distance(radius, Metrics.MILES));
    return stores;
  }

  public Store getStoreById(String id) {
    Optional<Store> store = repo.findById(id);
    return store.isPresent() ? store.get() : null;
  }
}
