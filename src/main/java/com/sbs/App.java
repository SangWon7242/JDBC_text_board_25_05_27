package com.sbs;

import com.sbs.container.Container;
import com.sbs.global.base.Rq;
import com.sbs.global.simpleDb.SimpleDb;

import java.util.Scanner;

public class App {
  private SimpleDb simpleDb;

  public App() {
    simpleDb = new SimpleDb("localhost", "root", "", "JDBC_text_board");
    simpleDb.setDevMode(true); // 개발 모드
  }

  public void run() {
    System.out.println("== JDBC 텍스트 게시판 ==");
    Scanner sc = Container.scanner;

    while (true) {
      Rq rq = new Rq();

      String promptName = "명령";

      System.out.printf("%s) ", promptName);
      String cmd = sc.nextLine();

      rq.setCommand(cmd);

      if(rq.getActionPath().equals("/usr/article/write")) {
        System.out.println("== 게시물 작성 ==");
        System.out.print("제목 : ");
        String subject = sc.nextLine();

        System.out.print("내용 : ");
        String content = sc.nextLine();

        int id = 1;

        System.out.printf("%d번 게시물이 등록되었습니다.\n", id);
      } else if (cmd.equals("exit")) {
        System.out.println("게시판을 종료합니다.");
        break;
      }

      sc.close();
    }
  }
}
