package com.lmp.app.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.lmp.app.entity.CategoryNode;
import com.lmp.app.entity.CategoryTree;
import com.lmp.app.entity.SearchRequest;
import com.lmp.app.entity.SearchResponse;
import com.lmp.app.service.AutoCompleteService;
import com.lmp.app.service.CategorizationService;
import com.lmp.app.service.ItemService;
import com.lmp.db.pojo.ItemEntity;

@RestController
@RequestMapping("/item")
public class UPCInventoryController extends BaseController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ItemService itemService;

  @Autowired
  private AutoCompleteService acService;
  @Autowired
  private CategorizationService ctService;

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

  @GetMapping("/suggest/{q}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> lookupByText(@PathVariable("q") String q) {
    logger.debug("searching for query: {}", q);
    List<String> list = acService.suggest(q);
    SearchResponse<String> response = new SearchResponse<>();
    response.setResults(list);
    return new ResponseEntity<SearchResponse>(response, HttpStatus.OK);
  }

  @PostMapping("/admin/build-auto-complete-collection")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> lookupByText() {
    
    acService.buildAutoCompleteCollection();
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping("/categories")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> getCategories(@RequestParam(value = "name", required = false) String name) {
    Map<String, CategoryNode> map = ctService.buildProductCategorization();
    logger.info("getting categories for {}", name);
    CategoryNode node = map.get(Strings.isNullOrEmpty(name) ? "root" : name.toLowerCase().trim());
    SearchResponse<CategoryNode> response = new SearchResponse<>();
    response.setResults(Lists.asList(node, new CategoryNode[]{}));
    return new ResponseEntity<SearchResponse>(response, HttpStatus.OK);
  }
}
