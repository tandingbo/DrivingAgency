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
@Document(collection = "comment")
public class Comment implements Serializable {
    @Id
    private String id;
    private String name;
    private String author;
    private String content;
    private Date publishTime;
}
