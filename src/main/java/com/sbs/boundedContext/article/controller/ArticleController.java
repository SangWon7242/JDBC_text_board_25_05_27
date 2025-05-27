package com.sbs.boundedContext.article.controller;

import com.sbs.container.Container;
import com.sbs.global.base.Rq;
import com.sbs.global.simpleDb.Sql;

public class ArticleController {

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
}
