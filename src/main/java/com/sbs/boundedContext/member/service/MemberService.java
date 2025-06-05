package com.sbs.boundedContext.member.service;

import com.sbs.boundedContext.member.repository.MemberRepository;
import com.sbs.container.Container;

public class MemberService {
  private MemberRepository memberRepository;

  public MemberService() {
    memberRepository = Container.memberRepository;
  }

  public long join(String username, String password, String name) {
    return memberRepository.join(username, password, name);
  }
}
