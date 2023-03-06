package com.testo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.testo.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	Page<Post> findByCreatedBy(Long userId, Pageable pageable);

	Page<Post> findByCategoryId(Long categoryId, Pageable pageable);

	Page<Post> findByTags(Long id, Pageable pageable);

	Long countByCreatedBy(Long userId);
}
