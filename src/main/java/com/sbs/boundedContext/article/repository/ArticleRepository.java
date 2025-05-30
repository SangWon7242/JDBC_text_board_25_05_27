package com.sbs.boundedContext.article.repository;

import com.sbs.container.Container;
import com.sbs.global.simpleDb.Sql;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

  public List<Map<String, Object>> findAll() {
    Sql sql = Container.simpleDb.genSql();
    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("ORDER BY id DESC");

    List<Map<String, Object>> articleRows = sql.selectRows();

    // if(articleRows.isEmpty()) return null;
    if(articleRows.isEmpty()) return Collections.emptyList();

    return articleRows;
  }

  public Map<String, Object> findById(long id) {
    Sql sql = Container.simpleDb.genSql();
    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("WHERE id = ?", id);

    Map<String, Object> articleRow = sql.selectRow();

    if(articleRow == null) return null;

    return articleRow;
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
