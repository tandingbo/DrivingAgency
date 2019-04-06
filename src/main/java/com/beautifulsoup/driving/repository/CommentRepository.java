package com.beautifulsoup.driving.repository;

import com.beautifulsoup.driving.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment,String> {
    @Nullable
    List<Comment> findAllByName(String name);

    @Nullable
    List<Comment> findAllByNameOrderByPublishTimeDesc(String name);
}
