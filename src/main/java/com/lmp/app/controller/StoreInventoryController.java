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

  @GetMapping("/store-inventory")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> lookupByStoreId(@RequestParam("storeId") String storeId,
      @RequestParam("size") int size, @RequestParam("page") int page ) {
    logger.info("searching for store id {}", storeId);
    BaseResponse response = service.getAllInventoryForStore(SearchRequest.createSISearch(storeId, page, size));
    if (response == null) {
      logger.info("searching for store id {}", storeId);
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
  }
}
