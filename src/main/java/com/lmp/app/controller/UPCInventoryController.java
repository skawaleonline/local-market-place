package com.lmp.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lmp.app.entity.SearchRequest;
import com.lmp.app.entity.SearchResponse;
import com.lmp.app.service.ItemService;
import com.lmp.db.pojo.ItemEntity;

@RestController
public class UPCInventoryController extends BaseController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ItemService itemService;

  @GetMapping("/upc/{code}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> lookupByUpc(@PathVariable("code") long code) {
    logger.debug("searching for upc code {}", code);
    ItemEntity item = itemService.findByUpc(code);
    if (item == null) {
      logger.debug("no item found for upc {}", code);
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<ItemEntity>(item, HttpStatus.OK);
  }

  @GetMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> lookupByText(@RequestParam("q") String q
      , @RequestParam("page") int page, @RequestParam("size") int size) {
    logger.debug("searching for query: {}", q);
    SearchResponse<ItemEntity> response = itemService.searchByText(SearchRequest.createFor(q, page, size));
    if (response == null) {
      logger.debug("no item found for q: {}", q);
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<SearchResponse>(response, HttpStatus.OK);
  }
}
