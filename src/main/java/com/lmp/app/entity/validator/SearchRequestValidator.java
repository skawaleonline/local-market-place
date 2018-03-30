package com.lmp.app.entity.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.google.common.base.Strings;
import com.lmp.app.entity.FilterField;
import com.lmp.app.entity.RequestFilter;
import com.lmp.app.entity.SearchRequest;

@Component
public class SearchRequestValidator implements Validator {

  public boolean supports(Class clazz) {
    return SearchRequest.class.equals(clazz);
  }

  public void validate(Object obj, Errors e) {
    SearchRequest sRequest = (SearchRequest) obj;
    validateQueryAndStoreId(sRequest, e);
    validateFilters(sRequest.getFilters(), e);
  }

  /**
   * either one of query or storeId is required
   * 
   * @param sr
   */
  private void validateQueryAndStoreId(SearchRequest sr, Errors e) {
    if (Strings.isNullOrEmpty(sr.getQuery()) && Strings.isNullOrEmpty(sr.getStoreId())) {
      e.reject("query_storeid.required", "either query or storeId is required");
    }
  }

  private void validateFilters(List<RequestFilter> filters, Errors e) {
    if (filters != null && !filters.isEmpty()) {
      ValidationUtils.rejectIfEmptyOrWhitespace(e, "filters", "field.required");
      for (RequestFilter filter : filters) {
        if (filter == null || filter.getName() == null || filter.getValue() == null) {
          e.reject("field.invalid", "empty or null filter");
        } else {
          if (filter.getName().equalsIgnoreCase(FilterField.ON_SALE.getValue())) {
            if (!(filter.getValue().equalsIgnoreCase("true") || filter.getValue().equalsIgnoreCase("false"))) {
              e.reject("field.invalid", "filter " + filter.getName() + " can have either true or false");
            }
          } else if (filter.getName().equalsIgnoreCase(FilterField.BRAND.getValue())) {
          } else {
            e.reject("field.invalid", "invalid filter: " + filter.getName());
          }
        }
      }
    }
  }
}
