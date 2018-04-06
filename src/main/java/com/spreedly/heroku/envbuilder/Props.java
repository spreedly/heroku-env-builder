package com.spreedly.heroku.envbuilder;

import org.apache.log4j.Logger;

public class Props {

  private static Logger logger = Logger.getLogger("com.spreedly.heroku.envbuilder.config");

  public static void log(String key, String value) {
    logger.info(key + "=" + value);
  }
}
