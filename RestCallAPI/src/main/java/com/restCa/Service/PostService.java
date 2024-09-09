package com.restCa.Service;

import java.util.List;
import java.util.Map;

import com.restCa.Bean.PostResonse;
import com.restCa.Model.PostModel;

public interface PostService {

	List<Map<String, Object>> getPost();

	Map<String, Object> getById(Integer userId);

	List<Map<String, Object>> getFirstTen();
	
	List<PostModel> saveToDBFromApi();

	PostModel saveToDBFromApibyId(Integer postId);

}
