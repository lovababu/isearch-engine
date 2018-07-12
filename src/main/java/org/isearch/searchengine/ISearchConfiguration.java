package org.isearch.searchengine;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.isearch.searchengine.aws.RekognitionConnector;
import org.isearch.searchengine.aws.config.AwsConfig;
import org.isearch.searchengine.es.ElasticSearchConnecter;
import org.isearch.searchengine.es.config.ESConfig;
import org.isearch.searchengine.service.ISearchService;
import org.isearch.searchengine.service.impl.ISearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@Configuration
@EnableConfigurationProperties(value = {
            AwsConfig.class,
            ESConfig.class
        })
public class ISearchConfiguration {

    @Autowired
    private AwsConfig awsConfig;

    @Autowired
    private ESConfig esConfig;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("5Mb");
        return factory.createMultipartConfig();
    }

    @Bean
    public RekognitionConnector rekognitionConnector(AwsConfig awsConfig) {
        return new RekognitionConnector(awsConfig);
    }

    @Bean
    public ElasticSearchConnecter elasticSearchConnecter(RestClient restClient, ESConfig esConfig) {
        return new ElasticSearchConnecter(restClient, esConfig);
    }

    @Bean
    public ISearchService searchService(RekognitionConnector rekognitionConnector,
                                        ElasticSearchConnecter elasticSearchConnecter) {
        return new ISearchServiceImpl(rekognitionConnector, elasticSearchConnecter);
    }

    @Bean
    public RestClient restClient(ESConfig config) {
        RestClient restClient = RestClient.builder(
                new HttpHost(esConfig.getHostName(), esConfig.getHostPort())
        )
                //TODO: revisit, is it ok to hard code header here?
                .setDefaultHeaders(new Header[]{new BasicHeader("Content-Type", "application/json")})
                .setMaxRetryTimeoutMillis(30000)
                .setFailureListener(new RestClient.FailureListener() {
                    @Override
                    public void onFailure(HttpHost host) {
                        //TODO: Log the exception, and take appropriate action.
                        System.out.println("Request failed ." + host.getHostName());
                    }
                })
                .build();

        return restClient;
    }



}
