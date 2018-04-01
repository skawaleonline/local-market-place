package com.lmp.solr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.SimpleQuery;

import com.lmp.solr.entity.ItemDoc;

public interface SolrCustomRepository {

  Page<ItemDoc> search(SimpleQuery query);
}
