package com.sbs.simpleDb;

import com.sbs.boundedContext.article.dto.Article;
import com.sbs.boundedContext.member.dto.Member;
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
    createMemberTable();
  }

  @BeforeEach
  public void beforeEach() {
    truncateTable();
    makeArticleTestData();
    makeMemberTestData();
  }

  private void truncateTable() {
    simpleDb.run("TRUNCATE `member`");
    simpleDb.run("TRUNCATE article");
  }

  private void makeMemberTestData() {
    IntStream.rangeClosed(1, 5).forEach(i -> {
      String username = "user%d".formatted(i);
      String password = "1234";
      String name = "이름%d".formatted(i);

      Sql sql = simpleDb.genSql();
      sql.append("INSERT INTO `member`");
      sql.append("SET createDate = NOW()");
      sql.append(", modifiedDate = NOW()");
      sql.append(", username = ?", username);
      sql.append(", password = ?", password);
      sql.append(", `name` = ?", name);

      sql.insert();
    });
  }

  private void makeArticleTestData() {
    IntStream.rangeClosed(1, 5).forEach(i -> {
      String subject = "제목 %d".formatted(i);
      String content = "내용 %d".formatted(i);
      simpleDb.run("""
          INSERT INTO article
          SET createDate = NOW(),
          modifiedDate = NOW(),
          memberId = ?,
          subject = ?,
          content = ?;
          """, i, subject, content);
    });
  }

  private void createArticleTable() {
    simpleDb.run("DROP TABLE IF EXISTS article");

    simpleDb.run("""
        CREATE TABLE article (
          id INT UNSIGNED  NOT NULL PRIMARY KEY AUTO_INCREMENT,
          createDate DATETIME NOT NULL,
          modifiedDate DATETIME NOT NULL,
          memberId INT UNSIGNED NOT NULL,
          subject CHAR(100) NOT NULL,
          content TEXT NOT NULL
        )
        """);
  }

  private void createMemberTable() {
    simpleDb.run("DROP TABLE IF EXISTS `member`");

    simpleDb.run("""
        CREATE TABLE `member` (
          id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
          createDate DATETIME NOT NULL,
          modifiedDate DATETIME NOT NULL,
          username CHAR(50) NOT NULL UNIQUE,
          password CHAR(100) NOT NULL,
          `name` CHAR(50) NOT NULL
        )
        """);
  }

  @Test
  @DisplayName("INSERT 테스트")
  public void t1() {
    String subject = "제목 new";
    String content = "내용 new";
    int memberId = 1;

    /*
    simpleDb.run("""
        INSERT INTO article
        SET createDate = NOW(),
        modifiedDate = NOW(),
        memberId = ?,
        subject = ?,
        content = ?;
        """, memberId, subject, content);
     */

    Sql sql = simpleDb.genSql();
    sql.append("INSERT INTO article");
    sql.append("SET createDate = NOW()");
    sql.append(", modifiedDate = NOW()");
    sql.append(", memberId = ?", memberId);
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
      long memberId = i + 1;
      Map<String, Object> articleRow = articleRows.get(i);

      assertThat(articleRow.get("id")).isEqualTo(id);
      assertThat(articleRow.get("createDate")).isInstanceOf(LocalDateTime.class);
      assertThat(articleRow.get("createDate")).isNotNull();
      assertThat(articleRow.get("modifiedDate")).isInstanceOf(LocalDateTime.class);
      assertThat(articleRow.get("modifiedDate")).isNotNull();
      assertThat(articleRow.get("memberId")).isEqualTo(memberId);
      assertThat(articleRow.get("subject")).isEqualTo("제목 %d".formatted(id));
      assertThat(articleRow.get("content")).isEqualTo("내용 %d".formatted(id));
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
      long memberId = articleRows.size() - i;
      Map<String, Object> articleRow = articleRows.get(i);

      assertThat(articleRow.get("id")).isEqualTo(id);
      assertThat(articleRow.get("createDate")).isInstanceOf(LocalDateTime.class);
      assertThat(articleRow.get("createDate")).isNotNull();
      assertThat(articleRow.get("modifiedDate")).isInstanceOf(LocalDateTime.class);
      assertThat(articleRow.get("modifiedDate")).isNotNull();
      assertThat(articleRow.get("memberId")).isEqualTo(memberId);
      assertThat(articleRow.get("subject")).isEqualTo("제목 %d".formatted(id));
      assertThat(articleRow.get("content")).isEqualTo("내용 %d".formatted(id));
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
    assertThat(articleRow.get("createDate")).isInstanceOf(LocalDateTime.class); // LocalDateTime의 클래스의 Class 객체
    assertThat(articleRow.get("createDate")).isNotNull();
    assertThat(articleRow.get("modifiedDate")).isInstanceOf(LocalDateTime.class);
    assertThat(articleRow.get("modifiedDate")).isNotNull();
    assertThat(articleRow.get("memberId")).isEqualTo(1L);
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
        .append("SET modifiedDate = NOW()")
        .append(", subject = '제목 new'")
        .append("WHERE id IN (?, ?, ?, ?)", 1, 2, 3, 4);

    long affectedRowsCount = sql.update();
    assertThat(affectedRowsCount).isEqualTo(4);
  }

  @Test
  @DisplayName("DELETE 테스트")
  @Disabled // 삭제가 필요한 경우에만 활성화
  public void t7() {
    Sql sql = simpleDb.genSql();

  /*
  DELETE FROM article
  WHERE id IN (1, 3, 5);
  */

    sql.append("DELETE FROM article")
        .append("WHERE id IN (?, ?, ?)", 1, 3, 5);

    // 삭제된 row 개수
    long affectedRowsCount = sql.delete();
    assertThat(affectedRowsCount).isEqualTo(3);
  }

  @Test
  @DisplayName("selectRows, Article")
  public void t8() {
    Sql sql = simpleDb.genSql();

    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("ORDER BY id ASC");

    List<Article> articleRows = sql.selectRows(Article.class);

    // 정순 체크
    IntStream.range(0, articleRows.size()).forEach(i -> {
      long id = i + 1;
      long memberId = i + 1;

      Article article = articleRows.get(i);

      assertThat(article.getId()).isEqualTo(id);
      assertThat(article.getCreateDate()).isInstanceOf(LocalDateTime.class);
      assertThat(article.getCreateDate()).isNotNull();
      assertThat(article.getModifiedDate()).isInstanceOf(LocalDateTime.class);
      assertThat(article.getModifiedDate()).isNotNull();
      assertThat(article.getMemberId()).isEqualTo(memberId);
      assertThat(article.getSubject()).isEqualTo("제목 %d".formatted(id));
      assertThat(article.getContent()).isEqualTo("내용 %d".formatted(id));
    });
  }

  @Test
  @DisplayName("selectRow, Article")
  public void t9() {
    Sql sql = simpleDb.genSql();

    sql.append("SELECT *");
    sql.append("FROM article");
    sql.append("WHERE id = 1");

    Article article = sql.selectRow(Article.class);

    assertThat(article.getId()).isEqualTo(1L);
    assertThat(article.getCreateDate()).isInstanceOf(LocalDateTime.class);
    assertThat(article.getCreateDate()).isNotNull();
    assertThat(article.getModifiedDate()).isInstanceOf(LocalDateTime.class);
    assertThat(article.getModifiedDate()).isNotNull();
    assertThat(article.getMemberId()).isEqualTo(1);
    assertThat(article.getSubject()).isEqualTo("제목 1");
    assertThat(article.getContent()).isEqualTo("내용 1");
  }

  @Test
  @DisplayName("회원가입 테스트")
  public void t10() {
    String username = "user6";
    String password = "1234";
    String name = "양관식";

    /*
    INSERT INTO `member`
    SET createDate = NOW(),
    modifiedDate = NOW(),
    username = 'user1',
    password = '1234',
    `name` = '양관식';
    */

    Sql sql = simpleDb.genSql();
    sql.append("INSERT INTO `member`");
    sql.append("SET createDate = NOW()");
    sql.append(", modifiedDate = NOW()");
    sql.append(", username = ?", username);
    sql.append(", password = ?", password);
    sql.append(", `name` = ?", name);

    long newId = sql.insert();

    // 들어온 값이 0보다 크면 성공, 0보다 작으면 실패
    assertThat(newId).isGreaterThan(0);
  }

  @Test
  @DisplayName("회원 아이디 존재여부 확인")
  public void t11() {
    String username = "user1";

    /*
    SELECT COUNT(*) > 0
    FROM `member`
    WHERE username = 'user1';
    */

    Sql sql = simpleDb.genSql();
    sql.append("SELECT COUNT(*) > 0");
    sql.append("FROM `member`");
    sql.append("WHERE username = ?", username);

    boolean isExist = sql.selectBoolean();
    assertThat(isExist).isEqualTo(true);
  }

  @Test
  @DisplayName("selectRow, Member")
  public void t12() {
    Sql sql = simpleDb.genSql();

    String username = "user1";

    sql.append("SELECT *");
    sql.append("FROM `member`");
    sql.append("WHERE username = ?", username);

    Member member = sql.selectRow(Member.class);

    assertThat(member.getId()).isEqualTo(1L);
    assertThat(member.getCreateDate()).isInstanceOf(LocalDateTime.class);
    assertThat(member.getCreateDate()).isNotNull();
    assertThat(member.getModifiedDate()).isInstanceOf(LocalDateTime.class);
    assertThat(member.getModifiedDate()).isNotNull();
    assertThat(member.getUsername()).isEqualTo("user1");
    assertThat(member.getPassword()).isEqualTo("1234");
    assertThat(member.getName()).isEqualTo("이름1");
  }

  @Test
  @DisplayName("게시물 작성자 번호를 통한 게시물 조회")
  public void t13() {
    /*
    SELECT A.*, M.name AS extra__writerName
    FROM article AS A
    INNER JOIN `member` AS M
    ON A.memberId = M.id
    WHERE M.id = 1;
    */

    Sql sql = simpleDb.genSql();
    sql.append("SELECT A.*, M.name AS extra__writerName");
    sql.append("FROM article AS A");
    sql.append("INNER JOIN `member` AS M");
    sql.append("ON A.memberId = M.id");
    sql.append("WHERE M.id = 1;");

    Article article = sql.selectRow(Article.class);

    assertThat(article.getId()).isEqualTo(1L);
    assertThat(article.getCreateDate()).isInstanceOf(LocalDateTime.class);
    assertThat(article.getCreateDate()).isNotNull();
    assertThat(article.getModifiedDate()).isInstanceOf(LocalDateTime.class);
    assertThat(article.getModifiedDate()).isNotNull();
    assertThat(article.getMemberId()).isEqualTo(1L);
    assertThat(article.getSubject()).isEqualTo("제목 1");
    assertThat(article.getContent()).isEqualTo("내용 1");
    assertThat(article.getExtra__writerName()).isEqualTo("이름1");
  }
}
