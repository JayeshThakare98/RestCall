package com.restCa.Service;

import java.util.List;
import java.util.Map;

public interface PostService {

	List<Map<String, Object>> getPost();

	Map<String, Object> getById(Integer userId);

	List<Map<String, Object>> getFirstTen();

}
