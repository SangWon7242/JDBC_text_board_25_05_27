package com.sbs;

import com.sbs.global.simpleDb.SimpleDb;

public class App {
  private SimpleDb simpleDb;

  public App() {
    simpleDb = new SimpleDb("localhost", "root", "", "JDBC_text_board");
    simpleDb.setDevMode(true); // 개발 모드
  }

  public void run() {
    System.out.println("== JDBC 텍스트 게시판 ==");
  }
}
