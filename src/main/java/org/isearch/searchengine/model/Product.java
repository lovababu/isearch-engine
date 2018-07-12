package org.isearch.searchengine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class Product {

    private String name;
    private String imageUrl;
    private String price;
    private List<String> labels;

}
