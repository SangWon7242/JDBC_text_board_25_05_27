package com.sbs.global.session;

import java.util.HashMap;
import java.util.Map;

public class Session {
  private Map<String, Object> sessionStore;

  public Session() {
    sessionStore = new HashMap<>();
  }

  // 저장, 조회, 삭제, 존재여부
  public Object getAttribute(String key) {
    return sessionStore.get(key);
  }

  public void setAttribute(String key, Object value) {
    sessionStore.put(key, value);
  }

  public void removeAttribute(String key) {
    sessionStore.remove(key);
  }

  public boolean hasAttribute(String key) {
    return sessionStore.containsKey(key);
  }
}
