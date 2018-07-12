package org.isearch.searchengine.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognitionAsync;
import com.amazonaws.services.rekognition.AmazonRekognitionAsyncClientBuilder;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import org.isearch.searchengine.aws.config.AwsConfig;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class RekognitionConnector {

    private final AwsConfig awsConfig;

    public RekognitionConnector(AwsConfig config) {
        this.awsConfig = config;
    }

    public Map<String, String> findLabels(byte[] imageBytes) {
        final Map<String, String> labels = new HashMap<>();

        AmazonRekognitionAsync amazonRekognition = AmazonRekognitionAsyncClientBuilder
                .standard()
                .withRegion(Regions.EU_WEST_1)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(this.awsConfig.getAccessKey(),
                        this.awsConfig.getSecretKey())))
                .build();

        ByteBuffer byteBuffer = ByteBuffer.wrap(imageBytes);

        DetectLabelsRequest detectLabelsRequest = new DetectLabelsRequest();
        Image image = new Image();
        image.setBytes(byteBuffer);
        detectLabelsRequest.setImage(image);
        DetectLabelsResult detectLabelsResult = amazonRekognition.detectLabels(detectLabelsRequest);
        detectLabelsResult.getLabels().forEach(l -> {
            labels.put(l.getName(), String.valueOf(l.getConfidence()));
        });
        return labels;
    }
}
