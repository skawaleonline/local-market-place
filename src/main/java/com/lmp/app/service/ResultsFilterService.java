package com.lmp.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.lmp.app.entity.ResponseFilter;
import com.lmp.app.entity.SearchRequest;
import com.lmp.solr.SolrSearchService;
import com.lmp.solr.entity.ItemDoc;
import com.lmp.solr.entity.ItemField;

@Service
public class ResultsFilterService {

  @Autowired
  private SolrSearchService solrService;
  @Autowired
  private StoreService storeService;

  public ResponseFilter getFiltersFor(SearchRequest sRequest, ItemField facetField) {
    FacetPage<ItemDoc> docs = null;
    // Search for query across all the stores
    if (Strings.isNullOrEmpty(sRequest.getStoreId())) {
      List<String> storeIds = storeService.getStoresIdsAround(sRequest);
      docs = solrService.facetSearch(sRequest, storeIds, facetField);
    } else {
      // get all items in the store
      // search for query in store
      docs = solrService.facetSearch(sRequest, Lists.asList(sRequest.getStoreId(), new String[] {}), facetField);
    }
    return ResponseFilter.buildResultFilter(facetField.getValue(), docs);
  }
}
