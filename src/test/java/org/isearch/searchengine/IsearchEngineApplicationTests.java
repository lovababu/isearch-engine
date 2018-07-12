package org.isearch.searchengine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IsearchEngineApplicationTests {

	@LocalServerPort
	int randomPort;

	@Test
	public void contextLoads() throws Exception {
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.add("image", Files.readAllBytes(Paths.get("/Users/durgapadala/Desktop/sample.png").toAbsolutePath()));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(String.format("http://localhost:%s/search", randomPort),
				HttpMethod.POST, requestEntity, String.class);
		System.out.println("response status: " + response.getStatusCode());
		System.out.println("response body: " + response.getBody());
	}
}
