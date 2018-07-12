package org.isearch.searchengine.service.impl;

import org.isearch.searchengine.aws.RekognitionConnector;
import org.isearch.searchengine.model.Product;
import org.isearch.searchengine.service.ISearchService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ISearchServiceImpl implements ISearchService {

    private final RekognitionConnector rekognitionConnector;

    public ISearchServiceImpl(RekognitionConnector rekognitionConnector) {
        this.rekognitionConnector = rekognitionConnector;
    }

    @Override
    public List<Product> fetchProducts(byte[] imageBytes) {
        Map<String, String> labels = this.rekognitionConnector.findLabels(imageBytes);
        System.out.println("Labels Return by Rek service: " + labels);
        //use these labels and search in ES engine.
        return dummyFeed();
    }

    private List<Product> dummyFeed() {
        Product product = Product.builder().name("Apple")
                .imageUrl("https://tinyurl.com/y7fa5ekc")
                .labels(Collections.emptyList())
                .build();
        return Collections.singletonList(product);
    }
}
