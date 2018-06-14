package com.spreedly.heroku.envbuilder;

import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ResolveEnvConfig implements ConfigBuilder {

  private static Logger logger = Logger.getLogger(ResolveEnvConfig.class);

  private EnvRepo envRepo;

  public ResolveEnvConfig(Map<String, Object> dependencies) {
    this.envRepo = (EnvRepo) dependencies.get(EnvRepo.class.getName());
  }

  public void apply(Properties properties)  {
    logger.debug("Resolving properties from env vars");
    for (String key : properties.stringPropertyNames()) {
      String value = envRepo.replaceWithEnvVar(properties, key);
      logger.debug("Resolving " + key + "=" + value);
      Props.log(key, value);
    }
  }
}
