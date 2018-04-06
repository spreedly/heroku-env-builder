package com.spreedly.heroku.envbuilder;

import java.util.Map;
import java.util.Properties;

public class ResolveEnvConfig implements ConfigBuilder {

  private EnvRepo envRepo;

  public ResolveEnvConfig(Map<String, Object> dependencies) {
    this.envRepo = (EnvRepo) dependencies.get(EnvRepo.class.getName());
  }

  public void apply(Properties properties)  {
    for (String key : properties.stringPropertyNames()) {
      String value = envRepo.replaceWithEnvVar(properties, key);
      Props.log(key, value);
    }
  }
}
