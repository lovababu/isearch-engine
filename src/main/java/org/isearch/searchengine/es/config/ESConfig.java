package org.isearch.searchengine.es.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "es")
@Setter
@Getter
public class ESConfig {

    private String hostName;
    private int hostPort;
    private String indexName;
}
