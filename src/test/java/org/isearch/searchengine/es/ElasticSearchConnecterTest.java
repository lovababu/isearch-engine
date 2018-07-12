package org.isearch.searchengine.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.isearch.searchengine.es.config.ESConfig;
import org.isearch.searchengine.model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class ElasticSearchConnecterTest {

    @Test
    public void test() {
        ESConfig esConfig = new ESConfig();
        esConfig.setIndexName("products");
        RestClient restClient = RestClient.builder(
                new HttpHost("980f51ae.ngrok.io")
        )
                .setMaxRetryTimeoutMillis(30000)
                .setFailureListener(new RestClient.FailureListener() {
                    @Override
                    public void onFailure(HttpHost host) {
                        //TODO: Log the exception, and take appropriate action.
                        System.out.println("Request failed ." + host.getHostName());
                    }
                })
                .build();

        ElasticSearchConnecter es = new ElasticSearchConnecter(restClient, esConfig);
        Map<String, Float> labels = new HashMap<>();
        labels.put("Apple", 90F);
        List<Product> products = es.search(labels);
        System.out.println(products);
        assertNotNull(products);
    }
}
