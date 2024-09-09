package com.restCa.controlller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restCa.Model.PostModel;
import com.restCa.Service.PostService;

@RestController
@RequestMapping("/api")
public class PostController {

	@Autowired
	private PostService postService;

	@GetMapping("/getposts")
	ResponseEntity<List<Map<String, Object>>> fetchAllPost() {
		List<Map<String, Object>> post = postService.getPost();

		return new ResponseEntity<>(post, HttpStatus.FOUND);
	}

	@GetMapping("/getposts/{userId}")
	ResponseEntity<Map<String, Object>> fetchPostById(@PathVariable Integer userId) {
		Map<String, Object> post = postService.getById(userId);

		return new ResponseEntity<>(post, HttpStatus.OK);
	}

	@GetMapping("/firstten")
	public List<Map<String, Object>> getFirstTenPosts() {
		return postService.getFirstTen();
	}

	@PostMapping("/{id}/save")
	public ResponseEntity<PostModel> savePostToDB(@PathVariable("id") Integer postId) {
		PostModel savedPost = postService.saveToDBFromApibyId(postId);
		return ResponseEntity.ok(savedPost);
	}

//	@PostMapping("/savetodb")
//	public ResponseEntity<PostModel> savePostToDB(@PathVariable("id") Integer postId) {
//		PostModel savedPost = postService.saveToDBFromApibyId(postId);
//		return ResponseEntity.ok(savedPost);
//	}
	@PostMapping("/saveall")
	public ResponseEntity<List<PostModel>> saveAllPostsToDB() {
		List<PostModel> savedPosts = postService.saveToDBFromApi();
		return ResponseEntity.ok(savedPosts);
	}

}
