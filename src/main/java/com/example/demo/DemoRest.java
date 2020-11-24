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
//JDBC Driver
//mport java.sql.Connection;
//mport java.sql.DriverManager;
//mport java.sql.ResultSet;
//mport java.sql.SQLException;
//mport java.sql.Statement;
// Spring JDBC
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;

@RestController
public class DemoRest {

	public static Logger logger = LoggerFactory.getLogger(DemoRest.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@GetMapping("/")
	public String index() {
		logger.info("root endpoint");
		return "Greetings from Spring Boot!";
	}

	@GetMapping("/healthz")
	public Healthz healthz() {
		logger.info("healthz endpoint");
		return new Healthz("ok");
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

	@GetMapping("/api/postgresql")
	public String postgresql(@RequestParam String name) {
		
		logger.info("postgresql endpoint");

		List<Map<String,Object>> list;
		list = jdbcTemplate.queryForList("SELECT UUID, NAME, NUMBER FROM SAMPLE WHERE NAME=?", name);
		return list.toString();

//		String pgHost = System.getenv().getOrDefault("POSTGRESQL_HOST", "localhost");
//		String pgPort = System.getenv().getOrDefault("POSTGRESQL_PORT", "5432");
//		String pgDB = System.getenv().getOrDefault("POSTGRESQL_DATABASE", "kikeyama");
//		String pgUser = System.getenv().getOrDefault("POSTGRESQL_USERNAME", "kikeyama");
//		String pgPassword = System.getenv().getOrDefault("POSTGRESQL_PASSWORD", "password");
//
//		// JDBC Url
//		String url = String.format("jdbc:postgresql://%s:%s/%s", pgHost, pgPort, pgDB);
//
//		// Declare blank connections
//		Connection conn = null;
//		Statement stmt = null;
//		ResultSet rset = null;
//
//		try{
//			// Connect to PostgreSQL
//			conn = DriverManager.getConnection(url, pgUser, pgPassword);
//
//			// Turn off auto-commit
//			conn.setAutoCommit(false);
//
//			// Execute SELECT statement
//			stmt = conn.createStatement();
//			String sql = String.format("SELECT UUID, NAME, NUMBER FROM SAMPLE WHERE NAME='%s'", name);
//			rset = stmt.executeQuery(sql);
//
//			// SELECT ResultSet
//			while(rset.next()){
//				String uuid = rset.getString(1);
//				logger.info("name=" + name + " uuid=" + uuid);
//			}
//		}
//		catch (SQLException e){
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				if(rset != null)rset.close();
//				if(stmt != null)stmt.close();
//				if(conn != null)conn.close();
//			}
//			catch (SQLException e){
//				e.printStackTrace();
//			}
//
//		}
//		return null;
	}

}
