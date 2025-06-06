package com.sbs;

import com.sbs.boundedContext.article.controller.ArticleController;
import com.sbs.boundedContext.common.controller.Controller;
import com.sbs.boundedContext.member.controller.MemberController;
import com.sbs.boundedContext.member.dto.Member;
import com.sbs.container.Container;
import com.sbs.global.base.Rq;
import com.sbs.global.simpleDb.SimpleDb;

import java.util.Scanner;

public class App {
  private SimpleDb simpleDb;
  private ArticleController articleController;
  private MemberController memberController;

  public App() {
    simpleDb = new SimpleDb("localhost", "root", "", "JDBC_text_board");
    simpleDb.setDevMode(true); // 개발 모드

    Container.init(simpleDb);

    memberController = Container.memberController;
    articleController = Container.articleController;
  }

  public void run() {
    System.out.println("== JDBC 텍스트 게시판 ==");
    Scanner sc = Container.scanner;

    while (true) {
      Rq rq = new Rq();

      Member member = rq.getLoginedMember();

      String promptName = "명령";

      if(member != null) {
        promptName = member.getUsername();
      }

      System.out.printf("%s) ", promptName);
      String cmd = sc.nextLine();

      rq.setCommand(cmd);

      Controller controller = getControllerByUrl(rq.getUrlPath());

      if(controller != null) {
        controller.performAction(rq);
      } else if (rq.getUrlPath().equals("exit")) {
        System.out.println("게시판을 종료합니다.");
        break;
      }
    }

    sc.close();
  }

  private Controller getControllerByUrl(String urlPath) {
    if(urlPath.startsWith("/usr/article")) {
      return articleController;
    }
    else if(urlPath.startsWith("/usr/member")) {
      return memberController;
    }

    return null;
  }
}
