package org.isearch.searchengine.es;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.isearch.searchengine.es.config.ESConfig;
import org.isearch.searchengine.model.Product;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticSearchConnecter {

    private final RestClient client;
    private final ESConfig esConfig;
    private final ObjectMapper objectMapper;
    public ElasticSearchConnecter(RestClient restClient, ESConfig esConfig) {
        this.client = restClient;
        this.esConfig = esConfig;
        this.objectMapper = new ObjectMapper();
    }

    public List<Product> search(Map<String, Float> labels) {
        List<Product> products = null;
        try {
            Response response = this.client.performRequest(
                    HttpMethod.GET.name(),
                    String.format("/%s/_doc/_search", esConfig.getIndexName()),
                    queryMap(),
                    new StringEntity(buildQuery(labels), ContentType.APPLICATION_JSON)
            );

            if (response.getStatusLine().getStatusCode() == 200) {
                JsonNode jsonNode;
                products = new ArrayList<>();
                try {
                    if (response.getEntity() != null && response.getEntity().getContent() != null) {
                        jsonNode = objectMapper.readTree(response.getEntity().getContent());
                        if (jsonNode != null && jsonNode.get("hits") != null && jsonNode.get("hits").get("hits") != null) {
                            jsonNode = jsonNode.get("hits").get("hits");
                            if (jsonNode.isArray()) {
                                for (final JsonNode objNode : jsonNode) {
                                    JsonNode source = objNode.get("_source");
                                    //TODO: hacky, has to refactor.
                                    Product product = Product.builder().name(source.get("name").textValue().replaceAll("\"", ""))
                                            .imageUrl(source.get("imageUrl").textValue().replaceAll("\"", ""))
                                            .price(source.get("price") != null ? source.get("price").textValue().replaceAll("\"", "") : "$0.00")
                                            .labels(setLabels(source.get("labels")))
                                            .build();
                                    products.add(product);
                                }
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            System.out.println(response.getStatusLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    private List<String> setLabels(JsonNode labels) {
        List<String> labelArray = new ArrayList<>();
        if (labels != null && labels.isArray()) {
            for (JsonNode lab : labels) {
                labelArray.add(lab.textValue());
            }
        }

        return labelArray;
    }

    private String buildQuery(Map<String,Float> map) {
        String query = "{\"query\": {\"match\": {\"labels\":\"%s\"}}}";
        StringBuffer labels = new StringBuffer();
        map.forEach((k, v) -> {
            if (v > 85F) {
                labels.append(k).append(" ");
            }
        });
        return String.format(query, labels);
    }

    private Map<String, String> queryMap() {
        Map<String, String> map = new HashMap<>();
        map.put("filter_path", "hits.hits._source");
        return map;
    }
}
