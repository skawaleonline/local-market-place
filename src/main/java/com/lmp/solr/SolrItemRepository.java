package com.lmp.solr;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.SolrCrudRepository;

import com.lmp.solr.entity.ItemDoc;

public interface SolrItemRepository extends SolrCrudRepository<ItemDoc, String> {

  ItemDoc findByUpc(Integer upc);

  Page<ItemDoc> findByContent(String content, Pageable page);
  
  Page<ItemDoc> findByBrand(String content, Pageable page);
  
  Page<ItemDoc> findByCategories(String categories, Pageable page);
 }
