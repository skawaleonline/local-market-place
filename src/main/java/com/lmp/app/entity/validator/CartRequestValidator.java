package com.lmp.app.entity.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.lmp.app.entity.SearchRequest;

@Component
public class CartRequestValidator implements Validator {

  public boolean supports(Class clazz) {
    return SearchRequest.class.equals(clazz);
  }

  public void validate(Object obj, Errors e) {
    
  }

}
