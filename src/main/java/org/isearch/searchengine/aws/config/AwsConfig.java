package org.isearch.searchengine.aws.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
@Setter
@Getter
public class AwsConfig {
    private String accessKey;
    private String secretKey;
    private String region;
    private Rekognition rekognition = new Rekognition();

    @Setter
    @Getter
    public static class Rekognition {
        private int minConfidence;
        private int maxLabels;
    }
}
