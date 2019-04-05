package com.beautifulsoup.driving.pojo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "announcement")
public class Announcement implements Serializable {
    @Id
    private String id;
    private String content;
    private Date publishTime;
}
