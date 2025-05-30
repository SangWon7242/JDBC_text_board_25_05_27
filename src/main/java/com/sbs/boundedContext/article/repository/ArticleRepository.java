package com.sbs.boundedContext.article.repository;

import com.sbs.boundedContext.article.dto.Article;
import com.sbs.container.Container;
import com.sbs.global.simpleDb.Sql;

import java.util.Collections;
import java.util.List;

public class ArticleRepository {
  public long write(String subject, String content) {
    Sql sql = Container.simpleDb.genSql();
    sql.append("INSERT INTO article");
    sql.append("SET createDate = NOW()");
    sql.append(", modifiedDate = NOW()");
    sql.append(", subject = ?", subject);
    sql.append(", content = ?", content);

    long newId = sql.insert();

    return newId;
  }

  public List<Article> findAll() {
    Sql sql = Container.simpleDb.genSql();
    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("ORDER BY id DESC");

    List<Article> articleRows = sql.selectRows(Article.class);

    // if(articleRows.isEmpty()) return null;
    if(articleRows.isEmpty()) return Collections.emptyList();

    return articleRows;
  }

  public Article findById(long id) {
    Sql sql = Container.simpleDb.genSql();
    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("WHERE id = ?", id);

    Article article = sql.selectRow(Article.class);

    if(article == null) return null;

    return article;
  }

  public void modify(long id, String subject, String content) {
    Sql sql = Container.simpleDb.genSql();
    sql.append("UPDATE article");
    sql.append("SET modifiedDate = NOW()");
    sql.append(", subject = ?", subject);
    sql.append(", content = ?", content);
    sql.append("WHERE id = ?", id);

    sql.update();
  }

  public void delete(long id) {
    Sql sql = Container.simpleDb.genSql();
    sql.append("DELETE FROM article");
    sql.append("WHERE id = ?", id);

    sql.delete();
  }
}
