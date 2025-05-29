package com.sbs.boundedContext.common.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BaseDto {
  protected long id;
  protected LocalDateTime createDate;
  protected LocalDateTime modifiedDate;

  public String getFormatCreateDate() {
    return createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  public String getFormatModifiedDate() {
    return modifiedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }
}
