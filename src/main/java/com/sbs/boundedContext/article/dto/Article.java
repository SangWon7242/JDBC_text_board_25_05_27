package com.sbs.boundedContext.article.dto;

import com.sbs.boundedContext.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Article extends BaseDto {
  private long id;
  private String subject;
  private String content;
}
