package com.restCa.Serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.restCa.Bean.PostResonse;
import com.restCa.Model.PostModel;
import com.restCa.Repo.PostRepository;
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
//		 
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

	@Autowired
	private PostRepository postRepository;

	@Override
	public List<Map<String, Object>> getPost() {
		String url = baseUrl + postsEndpoint;
		try {
			ResponseEntity<List> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, List.class);
			return responseEntity.getBody();
		} catch (HttpClientErrorException e) {
			throw new RuntimeException("Failed to fetch posts", e);
		}
	}

	@Override
	public Map<String, Object> getById(Integer userId) {
		String url = baseUrl + postsEndpoint + "/" + userId;
		try {
			ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Map.class);
			if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new RuntimeException("Post with ID " + userId + " not found.");
			}
			return responseEntity.getBody();
		} catch (HttpClientErrorException e) {
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
	}

	@Override
	public List<PostModel> saveToDBFromApi() {
		List<PostModel> postModelsList = new ArrayList<>();
		List<Map<String, Object>> posts = getPost();

		for (Map<String, Object> postData : posts) {
			PostModel postModels = new PostModel();
			postModels.setId((Integer) postData.get("id"));
			postModels.setUserId((Integer) postData.get("userId"));
			postModels.setTitle((String) postData.get("title"));
			postModels.setBody((String) postData.get("body"));
			
			postModelsList.add(postRepository.save(postModels));
//			Optional<PostModel> existingPost = postRepository.findById(post.getId());
//			Optional<PostModel> existingPost = postRepository.findByUserId(post.getId());
//
//			if (existingPost.isPresent()) {
//				PostModel updatePost = existingPost.get();
//				updatePost.setTitle(post.getTitle());
//				updatePost.setBody(post.getBody());
////				updatePost.setId(post.getId());
//				postModels.add(postRepository.save(updatePost));
//			} else {
//				postModels.add(postRepository.save(post));
//			}
		}
//		return postModels;
		return postModelsList ; 
	}

	@Override
	public PostModel saveToDBFromApibyId(Integer postId) {
		// Fetch data from the third-party API
		String url = baseUrl + postsEndpoint + "/" + postId;
		try {
			// Get post data from API
			ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Map.class);
			if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new RuntimeException("Post with ID " + postId + " not found.");
			}

			Map<String, Object> postData = responseEntity.getBody();

			// Map the response data to PostModel
			PostModel post = new PostModel();
			post.setId((Integer) postData.get("id"));
			post.setUserId((Integer) postData.get("userId"));
			post.setTitle((String) postData.get("title"));
			post.setBody((String) postData.get("body"));

			// Check if the post already exists in the database
			Optional<PostModel> existingPost = postRepository.findById(postId);
			if (existingPost.isPresent()) {
				// Update the existing post
				PostModel updatePost = existingPost.get();
				updatePost.setTitle(post.getTitle());
				updatePost.setBody(post.getBody());
				return postRepository.save(updatePost);
			} else {
				// Save a new post
				return postRepository.save(post);
			}

		} catch (HttpClientErrorException e) {
			throw new RuntimeException("Failed to fetch or save post with ID " + postId, e);
		}
	}
}
