package com.spreedly.heroku.envbuilder;

import java.util.Properties;

import org.apache.log4j.Logger;

public class EnvRepo {

  private static Logger logger = Logger.getLogger(EnvRepo.class);

  private Env env;

  public EnvRepo(Env env) {
    this.env = env;
  }

  public String resolveFromEnvVar(Properties properties, String propertyName) {
    String envVar = (String) properties.remove(propertyName);
    logger.debug("Resolving value for " + propertyName + " as " + envVar + " to " + env.get(envVar));
    return env.get(envVar);
  }

  public String replaceWithEnvVar(Properties properties, String propertyName) {
    String envVar = properties.getProperty(propertyName);
    String value = env.get(envVar);

    logger.debug("Replacing value for " + propertyName + " as " + envVar + " with " + value);

    if (value != null) {
      properties.put(propertyName, value);
    }
    return value;
  }
}
