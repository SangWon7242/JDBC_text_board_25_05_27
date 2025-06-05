package com.sbs.boundedContext.member.repository;

import com.sbs.container.Container;
import com.sbs.global.simpleDb.Sql;

public class MemberRepository {
  public long join(String username, String password, String name) {
    Sql sql = Container.simpleDb.genSql();
    sql.append("INSERT INTO `member`");
    sql.append("SET createDate = NOW()");
    sql.append(", modifiedDate = NOW()");
    sql.append(", username = ?", username);
    sql.append(", password = ?", password);
    sql.append(", `name` = ?", name);

    long newId = sql.insert();

    return newId;
  }
}
