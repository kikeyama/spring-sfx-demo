package com.example.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
//import org.springframework.beans.factory.annotation.Value;
import org.json.simple.JSONObject;
import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;
import org.apache.commons.lang3.RandomStringUtils;
//Logging by Logback
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class DemoRest {

	public static Logger logger = LoggerFactory.getLogger(DemoRest.class);

	@GetMapping("/")
	public String index() {
		logger.info("root endpoint");
		return "Greetings from Spring Boot!";
	}

	@GetMapping("/error")
	public String error() {
		logger.info("error endpoint");
		throw new NullPointerException("ぬるぽ");
	}

	@GetMapping("/api/demo")
	public String demo(@RequestParam String status) {

		logger.info("demo endpoint with query: status=" + status);

		if ("success".equals(status)) {
			return "Success!";
		} else if ("error".equals(status)) {
			throw new IllegalArgumentException("エラーでんがな");
		} else {
			return "ちゃんとクエリーストリング `status` 入れてや";
		}
	}

	@PostMapping("/api/post")
	public String post(@RequestBody JSONObject body) {
		logger.info("post endpoint with post: body" + body.toString());
		return body.toString();
	}

	@GetMapping("/api/flask")
	public String requestFlask(@RequestParam String name) {
		
//		@Value("#{environment.FLASK_HOST}")
//		String flaskHost;    // localhost:5050

		logger.info("flask endpoint");
		String flaskHost = System.getenv("FLASK_HOST");

		RestTemplate restTemplate = new RestTemplate();

		String flaskEndpoint = "http://" + flaskHost + "/?name=" + name;

		logger.info("call flask endpoint");
		String result = restTemplate.getForObject(flaskEndpoint, String.class);
		return result;
	}

	@GetMapping("/api/gorilla/id")
	public String requestGorilla(@RequestParam String httpStatus) {
		
		logger.info("gorilla id endpoint");
		String gorillaHost = System.getenv("GORILLA_HOST");
		String randomTraceId = RandomStringUtils.randomAlphanumeric(16).toLowerCase();

		RestTemplate restTemplate = new RestTemplate();

		String gorillaEndpoint = "http://" + gorillaHost + "/api/trace/" + randomTraceId + "?httpstatus=" + httpStatus;
		String result = restTemplate.getForObject(gorillaEndpoint, String.class);
		return result;
	}

}
