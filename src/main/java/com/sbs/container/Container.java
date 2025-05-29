package com.sbs.container;

import com.sbs.boundedContext.article.controller.ArticleController;
import com.sbs.boundedContext.article.repository.ArticleRepository;
import com.sbs.boundedContext.article.service.ArticleService;
import com.sbs.global.simpleDb.SimpleDb;

import java.util.Scanner;

public class Container {
  public static Scanner scanner;
  public static SimpleDb simpleDb;

  public static ArticleRepository articleRepository;

  public static ArticleService articleService;

  public static ArticleController articleController;

  public static void init(SimpleDb dbInfo) {
    scanner = new Scanner(System.in);
    simpleDb = dbInfo;

    articleRepository = new ArticleRepository();

    articleService = new ArticleService();

    articleController = new ArticleController();
  }
}
