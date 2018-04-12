package com.spreedly.heroku.envbuilder;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ConfigWriterTest {

  private ConfigWriter configWriter;

  @Before
  public void setup() {
    Map<String, String> map = new HashMap<>();
    map.put("APP_CONSUMER_GROUP", "consumer-group");
    map.put("APP_CONSUMER_CLIENT_ID", "consumer-client-id");
    EnvRepo envRepo = new EnvRepo(new MapEnv(map));

    Map<String, Object> dependencies = new HashMap<>();
    dependencies.put(EnvRepo.class.getName(), envRepo);
    dependencies.put(KeyStoreRepo.class.getName(), new FakeKeyStoreRepo());

    configWriter = new ConfigWriter(dependencies);
  }

  @Test
  public void apply() throws Exception {
    configWriter.write("test-config.properties");
  }
}
