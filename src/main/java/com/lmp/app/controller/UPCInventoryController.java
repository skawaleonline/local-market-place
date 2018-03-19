package com.lmp.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lmp.app.service.ItemService;
import com.lmp.db.pojo.Item;

@RestController
public class UPCInventoryController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ItemService itemService;

  @GetMapping("/upc/{code}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> lookupForUpc(@PathVariable("code") long code) {
    logger.debug("searching for upc code {}", code);
    List<Item> item = itemService.findAllByUpc(code);
    if (item == null) {
      logger.debug("no item found for upc {}", code);
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<List<Item>>(item, HttpStatus.OK);
  }
}
