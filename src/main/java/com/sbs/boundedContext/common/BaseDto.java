package com.sbs.boundedContext.common;

import java.time.LocalDateTime;

public abstract class BaseDto {
  protected long id;
  protected LocalDateTime createDate;
  protected LocalDateTime modifiedDate;
}
