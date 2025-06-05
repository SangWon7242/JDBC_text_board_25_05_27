package com.sbs.global.base;

import com.sbs.boundedContext.member.dto.Member;
import com.sbs.container.Container;
import com.sbs.global.session.Session;
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

  private Session session;

  private final String loginedMember;

  public Rq() {
    this.session = Container.session;
    this.loginedMember = "loginedMember";
  }

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

  public void setCommand(String url) {
    this.url = url;
    params = Util.getParamsFromUrl(this.url);
    urlPath = Util.getPathFromUrl(this.url);
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

  public Long getLongParam(String paramName, Long defaultValue) {
    if (!params.containsKey(paramName)) {
      return defaultValue;
    }

    try {
      return Long.parseLong(params.get(paramName));
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  public String getParam(String paramName, String defaultValue) {
    if (!params.containsKey(paramName)) return defaultValue;

    return params.get(paramName);
  }

  public Object getSessionAttr(String attrName) {
    return session.getAttribute(attrName);
  }

  public void setSessionAttr(String attrName, Object value) {
    session.setAttribute(attrName, value);
  }

  public void removeSessionAttr(String attrName) {
    session.removeAttribute(attrName);
  }

  public boolean hasSessionAttr(String attrName) {
    return session.hasAttribute(attrName);
  }

  public void login(Member member) {
    setSessionAttr(loginedMember, member);
  }

  public void logout() {
    removeSessionAttr(loginedMember);
  }

  public boolean isLogined() {
    return hasSessionAttr(loginedMember);
  }

  public boolean isLogout() {
    return !isLogined();
  }
}
