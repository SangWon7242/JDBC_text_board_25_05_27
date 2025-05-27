package com.sbs.simpleDb;

import com.sbs.global.simpleDb.SimpleDb;
import org.junit.jupiter.api.*;

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
  @DisplayName("Insert 테스트")
  public void t1() {
    
  }
}
