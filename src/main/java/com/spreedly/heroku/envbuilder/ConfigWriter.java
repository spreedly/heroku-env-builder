package com.spreedly.heroku.envbuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigWriter {

  private static Logger logger = Logger.getLogger("com.spreedly.heroku.envbuilder.config");

  private Map<String, Object> dependencies;

  public ConfigWriter(Map<String, Object> dependencies) {
    this.dependencies = dependencies;
  }

  public void write(String resourceName) throws Exception {
    Properties properties = new Properties();
    properties.load(ClassLoader.getSystemResourceAsStream(resourceName));

    List<String> builders = new ArrayList<>();
    for (String key : properties.stringPropertyNames()) {
      if (key.startsWith("config.builder")) {
        builders.add(key);
      }
    }

    Collections.sort(builders);
    for (String builder : builders) {
      String builderClassName = (String) properties.remove(builder);
      Class<?> builderClass = Class.forName(builderClassName);
      Constructor<?> constructor = builderClass.getConstructor(Map.class);
      ConfigBuilder configBuilder = (ConfigBuilder) constructor.newInstance(dependencies);
      configBuilder.apply(properties);
    }

    String configLocation = (String) properties.remove("config.output.location");
    logger.info("Writing properties to: " + configLocation);

    try (BufferedWriter out = new BufferedWriter(new FileWriter(configLocation))) {
      for (String key : properties.stringPropertyNames()) {
        out.write(key + "=" + properties.getProperty(key));
        out.newLine();
      }
    }
  }

}
