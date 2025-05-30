package com.sbs.boundedContext.member.controller;

import com.sbs.boundedContext.common.controller.Controller;
import com.sbs.container.Container;
import com.sbs.global.base.Rq;
import com.sbs.global.simpleDb.Sql;

public class MemberController implements Controller {
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

    Sql sql = Container.simpleDb.genSql();
    sql.append("INSERT INTO `member`");
    sql.append("SET createDate = NOW()");
    sql.append(", modifiedDate = NOW()");
    sql.append(", username = ?", username);
    sql.append(", password = ?", password);
    sql.append(", `name` = ?", name);

    long newId = sql.insert();

    System.out.printf("%d번 회원이 생성되었습니다.\n", newId);
  }
}
