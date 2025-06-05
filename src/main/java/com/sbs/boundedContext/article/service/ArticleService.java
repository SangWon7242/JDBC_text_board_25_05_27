package com.sbs.boundedContext.article.service;

import com.sbs.boundedContext.article.dto.Article;
import com.sbs.boundedContext.article.repository.ArticleRepository;
import com.sbs.container.Container;

import java.util.List;

public class ArticleService {
  private ArticleRepository articleRepository;

  public ArticleService() {
    articleRepository = Container.articleRepository;
  }

  public long write(long memberId, String subject, String content) {
    return articleRepository.write(memberId, subject, content);
  }

  public List<Article> findAll() {
    return articleRepository.findAll();
  }

  public Article findById(long id) {
    return articleRepository.findById(id);
  }

  public void modify(long id, String subject, String content) {
    articleRepository.modify(id, subject, content);
  }

  public void delete(long id) {
    articleRepository.delete(id);
  }
}
