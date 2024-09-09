package com.restCa.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restCa.Model.PostModel;

public interface PostRepository extends JpaRepository<PostModel, Integer>{
	 Optional<PostModel> findByUserId(Integer userId);

}
