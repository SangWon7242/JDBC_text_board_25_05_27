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

  @Test
  @DisplayName("SELECT 테스트 3 : 원하는 게시물 번호 조회")
  public void t4() {
    Sql sql = simpleDb.genSql();
    /*
    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("WHERE id = 1");
     */
    sql.append("SELECT * FROM article WHERE id = 1");
    Map<String, Object> articleRow = sql.selectRow();

    assertThat(articleRow.get("id")).isEqualTo(1L);
    assertThat(articleRow.get("createDate")).isInstanceOf(LocalDateTime.class);
    assertThat(articleRow.get("createDate")).isNotNull();
    assertThat(articleRow.get("modifiedDate")).isInstanceOf(LocalDateTime.class);
    assertThat(articleRow.get("modifiedDate")).isNotNull();
    assertThat(articleRow.get("subject")).isEqualTo("제목 1");
    assertThat(articleRow.get("content")).isEqualTo("내용 1");
  }

  @Test
  @DisplayName("SELECT 테스트 4 : 원하는 게시물 유무 확인")
  public void t5() {
    Sql sql = simpleDb.genSql();
    /*
    SELECT COUNT(*) > 0
    FROM article
    WHERE id = 6
    실행 결과값 : 1 or 0
    */
    sql.append("SELECT COUNT(*) > 0")
        .append("FROM article")
        .append("WHERE id = 6");

    boolean isExist = sql.selectBoolean();
    assertThat(isExist).isEqualTo(false);
  }

  @Test
  @DisplayName("UPDATE 테스트")
  public void t6() {
    Sql sql = simpleDb.genSql();

    /*
    UPDATE article
    SET subject = '제목 new'
    WHERE id IN (1, 2, 3, 4);
    */

    sql.append("UPDATE article")
        .append("SET subject = '제목 new'")
        .append("WHERE id IN (?, ?, ?, ?)", 1, 2, 3, 4);

    long affectedRowsCount = sql.update();
    assertThat(affectedRowsCount).isEqualTo(4);
  }
}
