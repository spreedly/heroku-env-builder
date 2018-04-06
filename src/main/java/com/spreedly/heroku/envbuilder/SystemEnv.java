package com.spreedly.heroku.envbuilder;

public class SystemEnv implements Env {

  @Override
  public String get(String propertyName) {
    return System.getenv(propertyName);
  }
}
