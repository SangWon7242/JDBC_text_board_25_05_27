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
    }
  }

  public void doWrite(Rq rq) {
    System.out.println("== 게시물 작성 ==");
    System.out.print("제목 : ");
    String subject = Container.scanner.nextLine();

    System.out.print("내용 : ");
    String content = Container.scanner.nextLine();

    Sql sql = rq.sql();
    sql.append("INSERT INTO article");
    sql.append("SET createDate = NOW()");
    sql.append(", modifiedDate = NOW()");
    sql.append(", subject = ?", subject);
    sql.append(", content = ?", content);

    long newId = sql.insert();

    System.out.printf("%d번 게시물이 등록되었습니다.\n", newId);
  }

  public void showList(Rq rq) {
    Sql sql = rq.sql();
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
    System.out.println("== 게시물 상세보기 ==");
  }
}
