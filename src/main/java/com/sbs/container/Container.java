package com.sbs.container;

import com.sbs.boundedContext.article.controller.ArticleController;
import com.sbs.boundedContext.article.repository.ArticleRepository;
import com.sbs.boundedContext.article.service.ArticleService;

import java.util.Scanner;

public class Container {
  public static Scanner scanner;

  public static ArticleRepository articleRepository;

  public static ArticleService articleService;

  public static ArticleController articleController;

  static {
    scanner = new Scanner(System.in);

    articleRepository = new ArticleRepository();

    articleService = new ArticleService();

    articleController = new ArticleController();
  }
}
