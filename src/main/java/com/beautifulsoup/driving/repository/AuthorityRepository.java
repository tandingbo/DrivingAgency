package com.beautifulsoup.driving.repository;

import com.beautifulsoup.driving.pojo.Authority;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority,Integer> {
    List<Authority> findAllByIdIn(Collection<Integer> ids);
}
