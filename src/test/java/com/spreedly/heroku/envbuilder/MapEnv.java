package com.spreedly.heroku.envbuilder;

import java.util.Map;

public class MapEnv implements Env {

  private Map<String, String> map;

  public MapEnv(Map<String, String> map) {
    this.map = map;
  }

  @Override
  public String get(String propertyName) {
    return map.get(propertyName);
  }
}
