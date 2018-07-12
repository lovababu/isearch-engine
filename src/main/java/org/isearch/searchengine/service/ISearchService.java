package org.isearch.searchengine.service;

import org.isearch.searchengine.model.Product;

import java.util.List;

public interface ISearchService {

    List<Product> fetchProducts(byte[] imageBytes);
}
