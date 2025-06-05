package com.sbs.boundedContext.article.dto;

import com.sbs.boundedContext.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Article extends BaseDto {
  private long id;
  private LocalDateTime createDate;
  private LocalDateTime modifiedDate;
  private String subject;
  private String content;
  private long memberId;
  private String extra__writerName;
}
