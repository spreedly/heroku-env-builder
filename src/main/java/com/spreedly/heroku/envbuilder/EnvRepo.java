package com.spreedly.heroku.envbuilder;

import java.util.Properties;

public class EnvRepo {

  private Env env;

  public EnvRepo(Env env) {
    this.env = env;
  }

  public String resolveFromEnvVar(Properties properties, String propertyName) {
    String envVar = (String) properties.remove(propertyName);
    return env.get(envVar);
  }

  public String replaceWithEnvVar(Properties properties, String propertyName) {
    String envVar = properties.getProperty(propertyName);
    String value = env.get(envVar);
    if (value != null) {
      properties.put(propertyName, value);
    }
    return value;
  }
}
