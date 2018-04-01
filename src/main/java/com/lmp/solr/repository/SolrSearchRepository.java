package com.lmp.solr.repository;

import org.springframework.data.solr.repository.SolrCrudRepository;

import com.lmp.solr.entity.ItemDoc;

public interface SolrSearchRepository extends SolrCrudRepository<ItemDoc, String>, SolrCustomRepository {

  
}
