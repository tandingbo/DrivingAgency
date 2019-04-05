package com.beautifulsoup.driving.repository;

import com.beautifulsoup.driving.pojo.Announcement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;

public interface AnnouncementRepository extends MongoRepository<Announcement,String> {
    @Nullable
    Announcement findFirstByOrderByPublishTimeDesc();
}
