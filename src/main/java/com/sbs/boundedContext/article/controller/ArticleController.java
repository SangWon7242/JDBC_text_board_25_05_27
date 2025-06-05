package com.sbs.boundedContext.article.controller;

import com.sbs.boundedContext.article.dto.Article;
import com.sbs.boundedContext.article.service.ArticleService;
import com.sbs.boundedContext.common.controller.Controller;
import com.sbs.boundedContext.member.dto.Member;
import com.sbs.container.Container;
import com.sbs.global.base.Rq;

import java.util.List;

public class ArticleController implements Controller {
  private ArticleService articleService;

  public ArticleController() {
    articleService = Container.articleService;
  }

  @Override
  public void performAction(Rq rq) {
    switch (rq.getUrlPath()) {
      case "/usr/article/write" -> doWrite(rq);
      case "/usr/article/list" -> showList(rq);
      case "/usr/article/detail" -> showDetail(rq);
      case "/usr/article/modify" -> doModify(rq);
      case "/usr/article/delete" -> doDelete(rq);
    }
  }

  public void doWrite(Rq rq) {
    if(!rq.isLogined()) {
      System.out.println("로그인 후 이용해주세요.");
      return;
    }

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

    Member member = rq.getLoginedMember();
    long memberId = member.getId();

    long newId = articleService.write(memberId, subject, content);

    System.out.printf("%d번 게시물이 등록되었습니다.\n", newId);
  }

  public void showList(Rq rq) {
    List<Article> articles = articleService.findAll();

    if(articles.isEmpty()) {
      System.out.println("게시물이 없습니다.");
      return;
    }

    System.out.println("== 게시물 리스트 ==");
    System.out.println("번호 | 제목 | 작성날짜 | 작성자");

    articles.forEach(article -> {
      System.out.printf("%d번 | %s | %s | %s\n", article.getId(), article.getSubject(), article.getCreateDate(), article.getExtra__writerName());
    });
  }

  private void showDetail(Rq rq) {
    long id = rq.getLongParam("id", 0L);

    if(id == 0) {
      System.out.println("올바른 값을 입력해주세요.");
      return;
    }

    Article article = articleService.findById(id);

    if (article == null) {
      System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
      return;
    }

    System.out.println("== 게시물 상세보기 ==");
    System.out.printf("번호 : %d\n", article.getId());
    System.out.printf("작성 날짜 : %s\n", article.getCreateDate());
    System.out.printf("수정 날짜 : %s\n", article.getModifiedDate());
    System.out.printf("제목 : %s\n", article.getSubject());
    System.out.printf("내용 : %s\n", article.getContent());
    System.out.printf("작성자 : %s\n", article.getExtra__writerName());
  }

  private void doModify(Rq rq) {
    if(!rq.isLogined()) {
      System.out.println("로그인 후 이용해주세요.");
      return;
    }

    long id = rq.getLongParam("id", 0L);

    if(id == 0) {
      System.out.println("올바른 값을 입력해주세요.");
      return;
    }

    Article article = articleService.findById(id);

    if (article == null) {
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

    articleService.modify(id, subject, content);

    System.out.printf("%d번 게시물이 수정되었습니다.\n", id);
  }

  private void doDelete(Rq rq) {
    if(!rq.isLogined()) {
      System.out.println("로그인 후 이용해주세요.");
      return;
    }

    long id = rq.getLongParam("id", 0L);

    if(id == 0) {
      System.out.println("올바른 값을 입력해주세요.");
      return;
    }

    Article article = articleService.findById(id);

    if (article == null) {
      System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
      return;
    }

    articleService.delete(id);

    System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);
  }
}
