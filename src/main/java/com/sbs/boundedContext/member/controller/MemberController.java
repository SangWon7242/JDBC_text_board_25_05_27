package com.sbs.boundedContext.member.controller;

import com.sbs.boundedContext.common.controller.Controller;
import com.sbs.boundedContext.member.dto.Member;
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
      case "/usr/member/login" -> doLogin(rq);
      case "/usr/member/logout" -> doLogout(rq);
    }
  }

  private void doLogin(Rq rq) {
    if(rq.isLogined()) {
      System.out.println("이미 로그인 되어있습니다.");
      return;
    }

    String username;
    String password;
    Member member;

    System.out.println("== 로그인 ==");

    // 로그인 아이디 입력
    while (true) {
      System.out.print("로그인 아이디 : ");
      username = Container.scanner.nextLine();

      if(username.trim().isEmpty()) {
        System.out.println("로그인 아이디를 입력해주세요.");
        continue;
      }

      member = memberService.findByUsername(username);

      if(member == null) {
        System.out.printf("%s(은)는 존재하지 않는 아이디입니다.\n", username);
        continue;
      }

      break;
    }

    int tryPasswordMaxCount = 3;
    int tryPasswordCount = 0;

    // 로그인 비밀번호 입력
    while (true) {
      if(tryPasswordCount >= tryPasswordMaxCount) {
        System.out.printf("비밀번호를 %d회 틀렸습니다. 로그인을 취소합니다.\n", tryPasswordMaxCount);
        return;
      }

      System.out.print("로그인 비밀번호 : ");
      password = Container.scanner.nextLine();

      if(password.trim().isEmpty()) {
        System.out.println("로그인 비밀번호를 입력해주세요.");
        continue;
      }

      if(!member.getPassword().equals(password)) {
        tryPasswordCount++;
        System.out.println("비밀번호가 일치하지 않습니다.");
        System.out.printf("비밀번호 틀린 횟수(%d / %d)\n", tryPasswordCount, tryPasswordMaxCount);
        continue;
      }

      break;
    }

    rq.login(member);

    System.out.printf("'%s'님 로그인을 환영합니다.\n", member.getName());
  }

  private void doLogout(Rq rq) {
    if(rq.isLogout()) {
      System.out.println("로그인후 이용해주세요.");
      return;
    }

    rq.logout();
    System.out.println("로그아웃 되었습니다.");
  }

  private void doJoin(Rq rq) {
    String username;
    String password;
    String passwordConfirm;
    String name;
    Member member;

    System.out.println("== 회원 가입 ==");
    
    // 로그인 아이디 입력
    while (true) {
      System.out.print("로그인 아이디 : ");
      username = Container.scanner.nextLine();

      if(username.trim().isEmpty()) {
        System.out.println("로그인 아이디를 입력해주세요.");
        continue;
      }

      member = memberService.findByUsername(username);

      if(member != null) {
        System.out.printf("%s(은)는 존재하는 아이디입니다.\n", username);
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
