package com.lmp.app.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lmp.app.entity.BaseResponse;
import com.lmp.app.entity.ResultFilter;
import com.lmp.app.entity.SearchRequest;
import com.lmp.app.entity.validator.SearchRequestValidator;
import com.lmp.app.service.ResultsFilterService;
import com.lmp.app.service.StoreInventoryService;
import com.lmp.app.utils.ValidationErrorBuilder;
import com.lmp.solr.entity.ItemField;

@RestController
public class StoreInventoryController extends BaseController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private StoreInventoryService service;
  @Autowired
  private ResultsFilterService filterService;

  private SearchRequestValidator searchRequestValidator;

  @Autowired
  public StoreInventoryController(SearchRequestValidator searchRequestValidator) {
      this.searchRequestValidator = searchRequestValidator;
  }

  @InitBinder("searchRequest")
  public void setupBinder(WebDataBinder binder) {
      binder.addValidators(searchRequestValidator);
  }

  @RequestMapping(value = "/store-inventory/search", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> lookupStoreInventory(@Valid @RequestBody SearchRequest searchRequest, Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
    }
    logger.info("searching for the request {}", searchRequest);
    BaseResponse response = service.search(searchRequest);
    // logger.info("getting store details for store id {}", storeId);

    if (response == null) {
      logger.info("no results for request {}", searchRequest.toString());
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
  }

  @RequestMapping(value = "/store-inventory/filters", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> lookupStoreInventoryFilters(@Valid @RequestBody SearchRequest searchRequest, Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
    }
    logger.info("searching for the request {}", searchRequest);
    ResultFilter response = filterService.getFiltersFor(searchRequest, ItemField.BRAND);
    // logger.info("getting store details for store id {}", storeId);

    if (response == null) {
      logger.info("no results for request {}", searchRequest.toString());
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<ResultFilter>(response, HttpStatus.OK);
  }
}
