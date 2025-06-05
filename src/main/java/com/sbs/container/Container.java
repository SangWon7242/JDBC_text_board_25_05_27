package com.sbs.container;

import com.sbs.boundedContext.article.controller.ArticleController;
import com.sbs.boundedContext.article.repository.ArticleRepository;
import com.sbs.boundedContext.article.service.ArticleService;
import com.sbs.boundedContext.member.controller.MemberController;
import com.sbs.boundedContext.member.repository.MemberRepository;
import com.sbs.boundedContext.member.service.MemberService;
import com.sbs.global.session.Session;
import com.sbs.global.simpleDb.SimpleDb;

import java.util.Scanner;

public class Container {
  public static Scanner scanner;
  public static SimpleDb simpleDb;
  public static Session session;

  public static MemberRepository memberRepository;
  public static ArticleRepository articleRepository;

  public static MemberService memberService;
  public static ArticleService articleService;

  public static MemberController memberController;
  public static ArticleController articleController;

  public static void init(SimpleDb dbInfo) {
    scanner = new Scanner(System.in);
    simpleDb = dbInfo;
    session = new Session();

    memberRepository = new MemberRepository();
    articleRepository = new ArticleRepository();

    memberService = new MemberService();
    articleService = new ArticleService();

    memberController = new MemberController();
    articleController = new ArticleController();
  }
}
