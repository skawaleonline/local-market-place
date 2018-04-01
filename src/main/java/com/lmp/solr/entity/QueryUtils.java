package com.lmp.solr.entity;

import java.util.List;

import org.springframework.data.solr.core.query.Criteria;

public class QueryUtils {

  public static Criteria orQuery(ItemField field, List<String> searchTerm) {
    if(searchTerm == null || searchTerm.isEmpty()) {
      return null;
    }
    Criteria condition = null;
    for(String term : searchTerm) {
      if(condition == null) {
        condition = new Criteria(field.getValue()).contains(term); 
      } else {
        condition = condition.or(new Criteria(field.getValue()).contains(term));
      }
    }
    return condition;
  }

  /*
  * Split on white space and create a OR query
  */
  public static Criteria orQuery(ItemField field, String searchTerm) {
    if(searchTerm == null || searchTerm.isEmpty()) {
      return null;
    }
    Criteria condition = null;
    for(String term : searchTerm.split(" ")) {
      if(condition == null) {
        condition = new Criteria(field.getValue()).contains(term); 
      } else {
        condition = condition.or(new Criteria(field.getValue()).contains(term));
      }
    }
    return condition;
  }

  public static Criteria andQuery(ItemField field, List<String> searchTerm) {
    if(searchTerm == null || searchTerm.isEmpty()) {
      return null;
    }
    Criteria condition = null;
    for(String term : searchTerm) {
      if(condition == null) {
        condition = new Criteria(field.getValue()).contains(term); 
      } else {
        condition = condition.and(new Criteria(field.getValue()).contains(term));
      }
    }
    return condition;
  }

  /*
  * Split on white space and create an AND query
  */
  public static Criteria andQuery(ItemField field, String searchTerm) {
    if(searchTerm == null || searchTerm.isEmpty()) {
      return null;
    }
    Criteria condition = null;
    for(String term : searchTerm.split(" ")) {
      if(condition == null) {
        condition = new Criteria(field.getValue()).contains(term); 
      } else {
        condition = condition.and(new Criteria(field.getValue()).contains(term));
      }
    }
    return condition;
  }
}
