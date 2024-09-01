package com.restCa.Serviceimpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.restCa.Service.PostService;

//@Service
//public class PostServiceImpl implements PostService {
//
//	String basrUrl = "https://jsonplaceholder.typicode.com/";
//
//	StringBuilder sBuilder = new StringBuilder(basrUrl);
//
//	String POST = "posts";
//
//	@Autowired
//	private RestTemplate restTemplate;
//
//	@Override
//	public List<Map<String, Object>> getPost() {
//		String url = sBuilder.append(POST).toString();
//
//		ResponseEntity<List> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, List.class);
//
//		return responseEntity.getBody();
//
//	}
//
//	@Override
//	public Map<String, Object> getById(Integer userId) {
//
//		String url = sBuilder.append(POST).append("/"+userId).toString();
//
//	 ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.GET, null, Map.class);
//
//		return exchange.getBody() ; //responseEntity.getBody();
//	}
//
//}
@Service
public class PostServiceImpl implements PostService {

	private final String baseUrl = "https://jsonplaceholder.typicode.com/";
	private final String postsEndpoint = "posts";

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<Map<String, Object>> getPost() {
		String url = baseUrl + postsEndpoint;
		try {
			ResponseEntity<List> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, List.class);
			return responseEntity.getBody();
		} catch (HttpClientErrorException e) {
			// Handle HTTP errors (4xx and 5xx)
//			System.err.println("HTTP error occurred: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
			throw new RuntimeException("Failed to fetch posts", e);
		} 
	}

	@Override
	public Map<String, Object> getById(Integer userId) {
		String url = baseUrl + postsEndpoint + "/" + userId;
		try {
			ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Map.class);
			if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
				// Handle 404 Not Found specifically
//				System.err.println("Resource with ID " + userId + " not found.");
				throw new RuntimeException("Post with ID " + userId + " not found.");
			}
			return responseEntity.getBody();
		} catch (HttpClientErrorException e) {
			// Handle HTTP errors (4xx and 5xx)
//			System.err.println("HTTP error occurred: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
			throw new RuntimeException("Failed to fetch post with ID " + userId, e);
		}
	}
	
	  public List<Map<String, Object>> getFirstTen() {
		  List<Map<String, Object>> posts = getPost();
		  if (posts.size() > 10) {
		        return posts.subList(0, 10);
		    } else {
		        return posts;
		    }
		  
//		  return  posts.subList(0, 10);
	  }
}
