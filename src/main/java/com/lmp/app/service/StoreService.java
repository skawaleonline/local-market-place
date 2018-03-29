package com.lmp.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.lmp.app.entity.SearchRequest;
import com.lmp.db.pojo.Store;
import com.lmp.db.repository.StoreRepository;

@Service
public class StoreService {

  @Autowired
  private StoreRepository repo;

  public GeoResults<Store> getStoresAround(double lat, double lng, int radius) {
    GeoResults<Store> stores = repo.findByLocationNear(new Point(lng, lat), new Distance(radius, Metrics.MILES));
    return stores;
  }

  @Cacheable("stores-around")
  public List<Store> getStoresAround(SearchRequest sRequest) {
    List<Store> stores = new ArrayList<>();
    if(sRequest.getLat() != 0 && sRequest.getLng() != 0) {
      GeoResults<Store> list = getStoresAround(sRequest.getLat(), sRequest.getLng(), sRequest.getRadius());
      for(GeoResult<Store> store : list.getContent()) {
        stores.add(store.getContent());
      }
    }
    return stores;
  }
  public Store getStoreById(String id) {
    Optional<Store> store = repo.findById(id);
    return store.isPresent() ? store.get() : null;
  }
}
