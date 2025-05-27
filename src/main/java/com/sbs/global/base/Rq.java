package com.sbs.global.base;

import com.sbs.global.simpleDb.SimpleDb;
import com.sbs.global.simpleDb.Sql;
import com.sbs.global.util.Util;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class Rq {
  private String url;

  @Getter
  private Map<String, String> params;

  @Getter
  private String urlPath;

  @Getter
  @Setter
  private String controllerTypeCode;

  @Getter
  @Setter
  private String controllerName;

  @Getter
  @Setter
  private String actionMethodName;

  private Sql sql;

  public String getActionPath() {
    String[] commandBits = urlPath.split("/");

    if(commandBits.length < 4) {
      return null;
    }
    // /usr/article/list
    // [, "usr", "article", "list"]
    controllerTypeCode = commandBits[1];
    controllerName = commandBits[2];
    actionMethodName = commandBits[3];

    return "/%s/%s/%s".formatted(controllerTypeCode, controllerName, actionMethodName);
  }

  public void setCommand(String url, SimpleDb simpleDb) {
    this.url = url;
    params = Util.getParamsFromUrl(this.url);
    urlPath = Util.getPathFromUrl(this.url);

    sql = simpleDb.genSql();
  }

  public int getIntParam(String paramName, int defaultValue) {
    if (!params.containsKey(paramName)) {
      return defaultValue;
    }

    try {
      return Integer.parseInt(params.get(paramName));
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  public String getParam(String paramName, String defaultValue) {
    if (!params.containsKey(paramName)) return defaultValue;

    return params.get(paramName);
  }

  public Sql sql() {
    return sql;
  }
}
