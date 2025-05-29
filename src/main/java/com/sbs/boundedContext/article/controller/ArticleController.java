package com.sbs.boundedContext.article.controller;

import com.sbs.boundedContext.common.controller.Controller;
import com.sbs.container.Container;
import com.sbs.global.base.Rq;
import com.sbs.global.simpleDb.Sql;

import java.util.List;
import java.util.Map;

public class ArticleController implements Controller {

  @Override
  public void performAction(Rq rq) {
    switch (rq.getUrlPath()) {
      case "/usr/article/write" -> doWrite(rq);
      case "/usr/article/list" -> showList(rq);
      case "/usr/article/detail" -> showDetail(rq);
      case "/usr/article/modify" -> doModify(rq);
    }
  }

  public void doWrite(Rq rq) {
    System.out.println("== 게시물 작성 ==");

    System.out.print("제목 : ");
    String subject = Container.scanner.nextLine();

    if(subject.trim().isEmpty()) {
      System.out.println("제목을 입력해주세요.");
      return;
    }

    System.out.print("내용 : ");
    String content = Container.scanner.nextLine();

    if(content.trim().isEmpty()) {
      System.out.println("내용을 입력해주세요.");
      return;
    }

    Sql sql = Container.simpleDb.genSql();
    sql.append("INSERT INTO article");
    sql.append("SET createDate = NOW()");
    sql.append(", modifiedDate = NOW()");
    sql.append(", subject = ?", subject);
    sql.append(", content = ?", content);

    long newId = sql.insert();

    System.out.printf("%d번 게시물이 등록되었습니다.\n", newId);
  }

  public void showList(Rq rq) {
    Sql sql = Container.simpleDb.genSql();
    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("ORDER BY id DESC");

    List<Map<String, Object>> articleRows = sql.selectRows();

    System.out.println("== 게시물 리스트 ==");
    System.out.println("번호 | 제목 | 작성날짜");
    
    articleRows.forEach(articleRow -> {
      System.out.printf("%d번 | %s | %s\n", (long) articleRow.get("id"), articleRow.get("subject"), articleRow.get("createDate"));
    });


  }

  private void showDetail(Rq rq) {
    long id = rq.getLongParam("id", 0L);

    if(id == 0) {
      System.out.println("올바른 값을 입력해주세요.");
      return;
    }

    Sql sql = Container.simpleDb.genSql();
    sql.append("SELECT COUNT(*) > 0");
    sql.append("FROM article");
    sql.append("WHERE id = ?", id);

    boolean isExist = sql.selectBoolean();

    if(!isExist) {
      System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
      return;
    }

    sql = Container.simpleDb.genSql();
    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("WHERE id = ?", id);

    Map<String, Object> articleRow = sql.selectRow();

    System.out.println("== 게시물 상세보기 ==");
    System.out.printf("번호 : %d\n", (long) articleRow.get("id"));
    System.out.printf("작성 날짜 : %s\n", articleRow.get("createDate"));
    System.out.printf("수정 날짜 : %s\n", articleRow.get("modifiedDate"));
    System.out.printf("제목 : %s\n", articleRow.get("subject"));
    System.out.printf("내용 : %s\n", articleRow.get("content"));
  }

  private void doModify(Rq rq) {
    long id = rq.getLongParam("id", 0L);

    if(id == 0) {
      System.out.println("올바른 값을 입력해주세요.");
      return;
    }

    Sql sql = Container.simpleDb.genSql();
    sql.append("SELECT COUNT(*) > 0");
    sql.append("FROM article");
    sql.append("WHERE id = ?", id);

    boolean isExist = sql.selectBoolean();

    if(!isExist) {
      System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
      return;
    }

    System.out.println("== 게시물 수정 ==");

    System.out.print("제목 : ");
    String subject = Container.scanner.nextLine();
    
    if(subject.trim().isEmpty()) {
      System.out.println("제목을 입력해주세요.");
      return;
    }

    System.out.print("내용 : ");
    String content = Container.scanner.nextLine();

    if(content.trim().isEmpty()) {
      System.out.println("내용을 입력해주세요.");
      return;
    }

    sql = Container.simpleDb.genSql();
    sql.append("UPDATE article");
    sql.append("SET modifiedDate = NOW()");
    sql.append(", subject = ?", subject);
    sql.append(", content = ?", content);
    sql.append("WHERE id = ?", id);

    sql.update();

    System.out.printf("%d번 게시물이 수정되었습니다.\n", id);
  }
}
