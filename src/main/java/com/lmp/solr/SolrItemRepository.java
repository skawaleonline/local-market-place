package com.lmp.solr;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import com.lmp.solr.entity.ItemDoc;

public interface SolrItemRepository extends SolrCrudRepository<ItemDoc, String> {

  ItemDoc findByUpc(Integer upc);

  Page<ItemDoc> findByContent(String content, Pageable page);
  
  Page<ItemDoc> findByBrand(String content, Pageable page);
  
  Page<ItemDoc> findByCategories(String categories, Pageable page);

  @Query("(content:*?0*) AND (stores:*?0*)")
  Page<ItemDoc> findByContentAndStores(String content, String storeIds, Pageable page);

  @Query("brand:*?0* AND stores:*?0*")
  Page<ItemDoc> findByBrandAndStores(String brand, String storeIds, Pageable page);

  @Query("cat:*?0* AND stores:*?0*")
  Page<ItemDoc> findByCategoriesAndStores(String categories, String storeIds, Pageable page);

}
