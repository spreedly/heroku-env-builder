package com.spreedly.heroku.envbuilder;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class HerokuKafkaAuthConfigTest {

  @Test
  public void http() {
    Map<String, String> map = new HashMap<>();
    map.put("KAFKA_URL", "kafka://url1.com:9092,kafka://url2.com:9092");
    EnvRepo envRepo = new EnvRepo(new MapEnv(map));

    KeyStoreRepo keyStoreRepo = new FakeKeyStoreRepo();

    Map<String, Object> dependencies = new HashMap<>();
    dependencies.put(EnvRepo.class.getName(), envRepo);
    dependencies.put(KeyStoreRepo.class.getName(), keyStoreRepo);

    HerokuKafkaAuthConfig config = new HerokuKafkaAuthConfig(dependencies);
    Properties properties = new Properties();
    properties.put("bootstrap.servers", "KAFKA_URL");
    properties.put("config.trustedCert", "KAFKA_TRUSTED_CERT");
    properties.put("config.clientCertKey", "KAFKA_CLIENT_CERT_KEY");
    properties.put("config.clientCert", "KAFKA_CLIENT_CERT");
    properties.put("ssl.truststore.location", "APP_TRUSTSTORE_LOCATION");
    properties.put("ssl.truststore.password", "APP_TRUSTSTORE_PASSWORD");
    properties.put("ssl.keystore.location", "APP_KEYSTORE_LOCATION");
    properties.put("ssl.keystore.password", "APP_KEYSTORE_PASSWORD");

    config.apply(properties);

    assertEquals("PLAINTEXT", properties.getProperty("security.protocol"));
    assertEquals("url1.com:9092,url2.com:9092", properties.getProperty("bootstrap.servers"));
    assertEquals("APP_TRUSTSTORE_LOCATION", properties.getProperty("ssl.truststore.location"));
    assertEquals("APP_TRUSTSTORE_PASSWORD", properties.getProperty("ssl.truststore.password"));
    assertEquals("APP_KEYSTORE_LOCATION", properties.getProperty("ssl.keystore.location"));
  }

  @Test
  public void https() {

    Map<String, String> map = new HashMap<>();
    map.put("KAFKA_URL", "kafka+ssl://url1.com:9092,kafka+ssl://url2.com:9092");
    map.put("KAFKA_TRUSTED_CERT", "kafka-trusted-cert");
    map.put("KAFKA_CLIENT_CERT_KEY", "kafka-client-cert-key");
    map.put("KAFKA_CLIENT_CERT", "kafka-client-cert");
    map.put("APP_TRUSTSTORE_LOCATION", "truststore-location");
    map.put("APP_TRUSTSTORE_PASSWORD", "truststore-password");
    map.put("APP_KEYSTORE_LOCATION", "keystore-location");
    map.put("APP_KEYSTORE_PASSWORD", "keystore-password");
    EnvRepo envRepo = new EnvRepo(new MapEnv(map));

    KeyStoreRepo keyStoreRepo = new FakeKeyStoreRepo();

    Map<String, Object> dependencies = new HashMap<>();
    dependencies.put(EnvRepo.class.getName(), envRepo);
    dependencies.put(KeyStoreRepo.class.getName(), keyStoreRepo);

    HerokuKafkaAuthConfig config = new HerokuKafkaAuthConfig(dependencies);

    Properties properties = new Properties();
    properties.put("bootstrap.servers", "KAFKA_URL");
    properties.put("config.trustedCert", "KAFKA_TRUSTED_CERT");
    properties.put("config.clientCertKey", "KAFKA_CLIENT_CERT_KEY");
    properties.put("config.clientCert", "KAFKA_CLIENT_CERT");
    properties.put("ssl.truststore.location", "APP_TRUSTSTORE_LOCATION");
    properties.put("ssl.truststore.password", "APP_TRUSTSTORE_PASSWORD");
    properties.put("ssl.keystore.location", "APP_KEYSTORE_LOCATION");
    properties.put("ssl.keystore.password", "APP_KEYSTORE_PASSWORD");

    config.apply(properties);

    assertEquals("SSL", properties.getProperty("security.protocol"));
    assertEquals("url1.com:9092,url2.com:9092", properties.getProperty("bootstrap.servers"));
    assertEquals("truststore-location", properties.getProperty("ssl.truststore.location"));
    assertEquals("truststore-password", properties.getProperty("ssl.truststore.password"));
    assertEquals("keystore-location", properties.getProperty("ssl.keystore.location"));
  }
}
