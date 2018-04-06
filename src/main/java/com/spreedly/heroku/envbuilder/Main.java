package com.spreedly.heroku.envbuilder;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class Main {

  private static Logger logger = Logger.getLogger("com.spreedly.heroku.envbuilder.config");

  public static void main(String[] args) {

    try {
      EnvRepo env = new EnvRepo(new SystemEnv());
      KeyStoreRepo keyStoreRepo = new EnvKeyStoreRepo();

      Map<String, Object> dependencies = new HashMap<>();
      dependencies.put(EnvRepo.class.getName(), env);
      dependencies.put(KeyStoreRepo.class.getName(), keyStoreRepo);

      ConfigWriter configWriter = new ConfigWriter(env);
      for (String config : args) {
        logger.info("Processing file: " + config);
        configWriter.write(config);
      }
    } catch (Exception e) {
      logger.error(e);
    }
  }
}
