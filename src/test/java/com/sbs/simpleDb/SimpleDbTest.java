package com.sbs.simpleDb;

import com.sbs.global.simpleDb.SimpleDb;
import com.sbs.global.simpleDb.Sql;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class SimpleDbTest {
  private SimpleDb simpleDb;

  @BeforeAll
  public void beforeAll() {
    simpleDb = new SimpleDb("localhost", "root", "", "JDBC_text_board");
    simpleDb.setDevMode(true); // 개발 모드
    
    createArticleTable();
  }

  @BeforeEach
  public void beforeEach() {
    truncateArticleTable();
  }

  private void truncateArticleTable() {
    simpleDb.run("TRUNCATE article");
  }

  private void createArticleTable() {
    simpleDb.run("DROP TABLE IF EXISTS article");

    simpleDb.run("""
        CREATE TABLE article (
        	id INT UNSIGNED  NOT NULL PRIMARY KEY AUTO_INCREMENT,
        	createDate DATETIME NOT NULL,
        	modifiedDate DATETIME NOT NULL,
        	subject CHAR(100) NOT NULL,
        	content TEXT NOT NULL
        )
        """);
  }
  
  @Test
  @DisplayName("INSERT 테스트")
  public void t1() {
    String subject = "제목 new";
    String content = "내용 new";

    /*
    simpleDb.run("""
        INSERT INTO article
        SET createDate = NOW(),
        modifiedDate = NOW(),
        subject = ?,
        content = ?;
        """, subject, content);
     */

    Sql sql = simpleDb.genSql();
    sql.append("INSERT INTO article");
    sql.append("SET createDate = NOW()");
    sql.append(", modifiedDate = NOW()");
    sql.append(", subject = ?", subject);
    sql.append(", content = ?", content);

    long newId = sql.insert(); // AUTO_INCREMENT에 의해 생성된 주키 리턴

    // 들어온 값이 0보다 크면 성공, 0보다 작으면 실패
    assertThat(newId).isGreaterThan(0);
    
    // 들어온 값이 1이랑 같냐
    // assertThat(newId).isEqualTo(1);
  }
}
