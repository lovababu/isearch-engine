package org.isearch.searchengine.controller;

import org.isearch.searchengine.model.Product;
import org.isearch.searchengine.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class ISearchController {

    @Autowired
    private ISearchService iSearchService;

    @PostMapping(value = "/search")
    public Mono<ResponseEntity<List<Product>>> search(@RequestParam("image") MultipartFile file) throws Exception {
        ;
        return Mono.just(new ResponseEntity<>(iSearchService.fetchProducts(file.getBytes()), HttpStatus.ACCEPTED));
    }
}
