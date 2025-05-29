package com.sbs.simpleDb;

import com.sbs.global.simpleDb.SimpleDb;
import com.sbs.global.simpleDb.Sql;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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
    makeArticleTestData();
  }

  private void truncateArticleTable() {
    simpleDb.run("TRUNCATE article");
  }

  private void makeArticleTestData() {
    IntStream.rangeClosed(1, 5).forEach(i -> {
      String subject = "제목 %d".formatted(i);
      String content = "내용 %d".formatted(i);
      simpleDb.run("""
          INSERT INTO article
          SET createDate = NOW(),
          modifiedDate = NOW(),
          subject = ?,
          content = ?;
          """, subject, content);
    });
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
  }

  @Test
  @DisplayName("SELECT 테스트 1 : 정순 조회")
  public void t2() {
    Sql sql = simpleDb.genSql();

    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("ORDER BY id ASC");

    // 데이터가 2차원으로 날라옴
    List<Map<String, Object>> articleRows = sql.selectRows();

    // 정순 체크
    IntStream.range(0, articleRows.size()).forEach(i -> {
      long id = i + 1;
      Map<String, Object> articleRow = articleRows.get(i);

      assertThat(articleRow.get("id")).isEqualTo(id);
      assertThat(articleRow.get("createDate")).isInstanceOf(LocalDateTime.class);
      assertThat(articleRow.get("createDate")).isNotNull();
      assertThat(articleRow.get("modifiedDate")).isInstanceOf(LocalDateTime.class);
      assertThat(articleRow.get("modifiedDate")).isNotNull();
      assertThat(articleRow.get("subject")).isEqualTo("제목 %d". formatted(id));
      assertThat(articleRow.get("content")).isEqualTo("내용 %d". formatted(id));
    });

    assertThat(articleRows.size()).isEqualTo(5);
  }

  @Test
  @DisplayName("SELECT 테스트 2 : 역순 조회")
  public void t3() {
    Sql sql = simpleDb.genSql();

    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("ORDER BY id DESC");

    // 데이터가 2차원으로 날라옴
    List<Map<String, Object>> articleRows = sql.selectRows();

    // 정순 체크
    IntStream.range(0, articleRows.size()).forEach(i -> {
      long id = articleRows.size() - i;
      Map<String, Object> articleRow = articleRows.get(i);

      assertThat(articleRow.get("id")).isEqualTo(id);
      assertThat(articleRow.get("createDate")).isInstanceOf(LocalDateTime.class);
      assertThat(articleRow.get("createDate")).isNotNull();
      assertThat(articleRow.get("modifiedDate")).isInstanceOf(LocalDateTime.class);
      assertThat(articleRow.get("modifiedDate")).isNotNull();
      assertThat(articleRow.get("subject")).isEqualTo("제목 %d". formatted(id));
      assertThat(articleRow.get("content")).isEqualTo("내용 %d". formatted(id));
    });

    assertThat(articleRows.size()).isEqualTo(5);
  }
}
