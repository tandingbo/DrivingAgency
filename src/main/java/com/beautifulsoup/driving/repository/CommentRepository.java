package com.beautifulsoup.driving.repository;

import com.beautifulsoup.driving.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,String> {
}
