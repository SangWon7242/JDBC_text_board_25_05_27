package com.sbs.boundedContext.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Member {
  private long id;
  private LocalDateTime createDate;
  private LocalDateTime modifiedDate;
  private String username;
  private String password;
  private String name;
}
