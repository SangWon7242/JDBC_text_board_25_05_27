package com.sbs.boundedContext.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Article {
  private long id;
  private LocalDateTime createDate;
  private LocalDateTime modifiedDate;
  private String subject;
  private String content;
}
