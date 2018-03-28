package com.lmp.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lmp.app.entity.BaseResponse;
import com.lmp.app.entity.SearchRequest;
import com.lmp.app.service.StoreInventoryService;

@RestController
public class StoreInventoryController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private StoreInventoryService service;

  @GetMapping("/store-inventory/store")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> lookupByStoreId(@RequestParam("storeId") String storeId,
      @RequestParam("q") String q, @RequestParam("size") int size,
      @RequestParam("page") int page) {
    logger.info("searching for store id {} & q {}", storeId, q);
    BaseResponse response = service.searchStoreInventoryFor(SearchRequest.createSISearch(storeId, q, page, size));
    // logger.info("getting store details for store id {}", storeId);

    if (response == null) {
      logger.info("searching for store id {} & q {}", storeId, q);
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
  }

  @GetMapping("/store-inventory/all")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> lookupAllStores(@RequestParam("q") String q, @RequestParam("size") int size,
      @RequestParam("page") int page) {
    logger.info("searching all stores for {}", q);
    BaseResponse response = service.searchAllStoresFor(SearchRequest.createSISearch(null, q, page, size));
    if (response == null) {
      logger.info("no results for q {} in all stores", q);
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
  }
}
