package com.spreedly.heroku.envbuilder;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class ResolveEnvConfigTest {

  private ResolveEnvConfig config;

  @Before
  public void setup() {

    Map<String, String> map = new HashMap<>();
    map.put("APP_CONSUMER_GROUP", "consumer-group");
    map.put("APP_CONSUMER_CLIENT_ID", "consumer-client-id");
    map.put("APP_CONSUMER_AUTO_OFFSET_RESET", "auto-offset-reset");
    EnvRepo envRepo = new EnvRepo(new MapEnv(map));

    Map<String, Object> dependencies = new HashMap<>();
    dependencies.put(EnvRepo.class.getName(), envRepo);

    config = new ResolveEnvConfig(dependencies);
  }

  @Test
  public void apply() {

    Properties properties = new Properties();
    properties.put("group.id", "APP_CONSUMER_GROUP");
    properties.put("client.id", "APP_CONSUMER_CLIENT_ID");
    properties.put("auto.offset.reset", "APP_CONSUMER_AUTO_OFFSET_RESET");
    properties.put("enable.auto.commit", "false");

    config.apply(properties);

    assertEquals("consumer-group", properties.getProperty("group.id"));
    assertEquals("consumer-client-id", properties.getProperty("client.id"));
    assertEquals("auto-offset-reset", properties.getProperty("auto.offset.reset"));
    assertEquals("false", properties.getProperty("enable.auto.commit"));
  }
}
