package org.isearch.searchengine.aws;

import org.isearch.searchengine.aws.config.AwsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@RunWith(JUnit4.class)
public class RekognitionConnectorTest {

    @Test
    public void test() throws IOException {
        AwsConfig awsConfig = new AwsConfig();
        //set accesskey and secretkey to awsconfig object.
        RekognitionConnector rekognitionConnector = new RekognitionConnector(awsConfig);

        Map<String, Float> labels= rekognitionConnector.findLabels(Files.readAllBytes(Paths.get("/Users/durgapadala/Desktop/grocery1.jpg").toAbsolutePath()));
        System.out.println(labels);
    }
}
