package com.lmp.app.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lmp.db.pojo.Store;
import com.lmp.db.repository.StoreRepository;

@RestController
public class StoreController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private StoreRepository storeRepo;

  //lat=37.356752&lng=-121.999249&size=5
  @GetMapping("/store/nearby")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> lookupStoreNearBy(@RequestParam("lat") double lat
      , @RequestParam("lng") double lng, @RequestParam("radius") double radius) {
    logger.info("searching for stores nearby lat {} & lng {}", lat, lng);
    GeoResults<Store> stores = storeRepo.findByLocationNear(new Point(lng, lat), new Distance(radius, Metrics.MILES));
    //Circle circle = new Circle(new Point(lng, lat), new Distance(radius, Metrics.MILES));
    //GeoResults<Store> stores = storeRepo.findByLocationWithin(circle);
    if (stores == null) {
      logger.debug("no stores found nearby lat {} & lng {}", lat, lng);
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<GeoResults<Store>>(stores, HttpStatus.OK);
  }

  @GetMapping("/store/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Cacheable("stores-response")
  public ResponseEntity<?> getStore(@PathVariable("id") String id) {
    logger.info("getting store details for id {}", id);
    Optional<Store> store = storeRepo.findById(id);
    if(!store.isPresent()) {
      logger.debug("no store found for id {}", id);
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<Store>(store.get(), HttpStatus.OK);
  }
}
