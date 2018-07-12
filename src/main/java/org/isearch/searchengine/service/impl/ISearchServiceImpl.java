package org.isearch.searchengine.service.impl;

import org.isearch.searchengine.aws.RekognitionConnector;
import org.isearch.searchengine.es.ElasticSearchConnecter;
import org.isearch.searchengine.model.Product;
import org.isearch.searchengine.service.ISearchService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ISearchServiceImpl implements ISearchService {

    private final RekognitionConnector rekognitionConnector;
    private final ElasticSearchConnecter elasticSearchConnecter;

    public ISearchServiceImpl(RekognitionConnector rekognitionConnector, ElasticSearchConnecter elasticSearchConnecter) {
        this.rekognitionConnector = rekognitionConnector;
        this.elasticSearchConnecter = elasticSearchConnecter;
    }

    @Override
    public List<Product> fetchProducts(byte[] imageBytes) {
        Map<String, Float> labels = this.rekognitionConnector.findLabels(imageBytes);

        //use these labels and search in ES engine.
        return elasticSearchConnecter.search(labels);
    }

    private List<Product> dummyFeed() {
        Product product = Product.builder().name("Apple")
                .imageUrl("https://tinyurl.com/y7fa5ekc")
                .labels(Collections.emptyList())
                .build();
        return Collections.singletonList(product);
    }
}
