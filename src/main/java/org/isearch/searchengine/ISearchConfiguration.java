package org.isearch.searchengine;

import org.isearch.searchengine.aws.config.AwsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@Configuration
@EnableConfigurationProperties(value = AwsConfig.class)
public class ISearchConfiguration {

    @Autowired
    private AwsConfig awsConfig;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("100Mb");
        return factory.createMultipartConfig();
    }



}
