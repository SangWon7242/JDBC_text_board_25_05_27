package com.sbs.boundedContext.article.service;

import com.sbs.boundedContext.article.repository.ArticleRepository;
import com.sbs.container.Container;

import java.util.List;
import java.util.Map;

public class ArticleService {
  private ArticleRepository articleRepository;

  public ArticleService() {
    articleRepository = Container.articleRepository;
  }

  public long write(String subject, String content) {
    return articleRepository.write(subject, content);
  }

  public List<Map<String,Object>> findAll() {
    return articleRepository.findAll();
  }

  public Map<String, Object> findById(long id) {
    return articleRepository.findById(id);
  }

  public void modify(long id, String subject, String content) {
    articleRepository.modify(id, subject, content);
  }

  public void delete(long id) {
    articleRepository.delete(id);
  }
}
