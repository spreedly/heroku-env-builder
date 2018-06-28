package com.spreedly.heroku.envbuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.log4j.Logger;

class HerokuKafkaAuthConfig implements ConfigBuilder {

  private static Logger logger = Logger.getLogger(HerokuKafkaAuthConfig.class);

  private EnvRepo envRepo;

  private KeyStoreRepo keyStoreRepo;

  public HerokuKafkaAuthConfig(Map<String, Object> dependencies) {
    this.envRepo = (EnvRepo) dependencies.get(EnvRepo.class.getName());
    this.keyStoreRepo = (KeyStoreRepo) dependencies.get(KeyStoreRepo.class.getName());
  }

  public void apply(Properties properties) {

    logger.debug("Applying auth config");

    try {
      String bootstrapServersEnvVar = envRepo.replaceWithEnvVar(properties, CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG);
      logger.info("Bootstrap servers: " + bootstrapServersEnvVar);

      String[] urls = bootstrapServersEnvVar.split(",");

      List<String> bootstrapServers = new ArrayList<>();

      for (String url : urls) {
        URI uri = new URI(url);
        bootstrapServers.add(String.format("%s:%d", uri.getHost(), uri.getPort()));
      }

      properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, String.join(",", bootstrapServers));
      Props.log(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, String.join(",", bootstrapServers));

      logger.debug("Checking against bootstrap servers: " + bootstrapServers);

      URI uri = new URI(urls[0]);
      switch (uri.getScheme()) {

      case "kafka":
        logger.debug("Choosing plain text");
        properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "PLAINTEXT");
        Props.log(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "PLAINTEXT");
        break;

      case "kafka+ssl":
        logger.debug("Choosing SSL");

        properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        Props.log(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");

        String trustStoreLocation = envRepo.replaceWithEnvVar(properties, SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG);
        Props.log(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, trustStoreLocation);

        String trustedCert = (String) properties.remove("config.trustedCert");
        String trustStorePassword = properties.getProperty(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG);

        keyStoreRepo.createTrustStore(trustedCert, trustStorePassword, trustStoreLocation);

        envRepo.replaceWithEnvVar(properties, SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG);

        String keyStoreLocation = envRepo.replaceWithEnvVar(properties, SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG);
        Props.log(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, keyStoreLocation);

        String clientCertKey = (String) properties.remove("config.clientCertKey");
        String clientCert = (String) properties.remove("config.clientCert");

        String keyStorePassword = properties.getProperty(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG);

        keyStoreRepo.createKeyStore(clientCertKey, clientCert, keyStorePassword, keyStoreLocation);

        envRepo.replaceWithEnvVar(properties, SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG);

        break;

      default:
        throw new URISyntaxException(uri.getScheme(), "Unknown URI scheme");
      }

    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
