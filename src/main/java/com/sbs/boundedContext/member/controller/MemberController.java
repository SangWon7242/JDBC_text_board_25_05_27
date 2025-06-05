package com.sbs.boundedContext.member.controller;

import com.sbs.boundedContext.common.controller.Controller;
import com.sbs.boundedContext.member.service.MemberService;
import com.sbs.container.Container;
import com.sbs.global.base.Rq;

public class MemberController implements Controller {
  private MemberService memberService;

  public MemberController() {
    memberService = Container.memberService;
  }


  @Override
  public void performAction(Rq rq) {
    switch (rq.getUrlPath()) {
      case "/usr/member/join" -> doJoin(rq);
    }
  }

  private void doJoin(Rq rq) {
    String username;
    String password;
    String passwordConfirm;
    String name;

    System.out.println("== 회원 가입 ==");
    
    // 로그인 아이디 입력
    while (true) {
      System.out.print("로그인 아이디 : ");
      username = Container.scanner.nextLine();

      if(username.trim().isEmpty()) {
        System.out.println("로그인 아이디를 입력해주세요.");
        continue;
      }

      break;
    }

    // 로그인 비밀번호 입력
    while (true) {
      System.out.print("로그인 비밀번호 : ");
      password = Container.scanner.nextLine();

      if(password.trim().isEmpty()) {
        System.out.println("로그인 비밀번호를 입력해주세요.");
        continue;
      }
      
      // 로그인 비밀번호 확인 입력
      while (true) {
        System.out.print("로그인 비밀번호 확인 : ");
        passwordConfirm = Container.scanner.nextLine();

        if(passwordConfirm.trim().isEmpty()) {
          System.out.println("로그인 비밀번호 확인을 입력해주세요.");
          continue;
        }

        if(!passwordConfirm.equals(password)) {
          System.out.println("비밀번호가 일치하지 않습니다.");
          continue;
        }
        
        break;
      }

      break;
    }

    // 로그인 아이디 입력
    while (true) {
      System.out.print("이름 : ");
      name = Container.scanner.nextLine();

      if(name.trim().isEmpty()) {
        System.out.println("이름을 입력해주세요.");
        continue;
      }

      break;
    }

    long newId = memberService.join(username, password, name);

    System.out.printf("%d번 회원이 생성되었습니다.\n", newId);
  }
}
