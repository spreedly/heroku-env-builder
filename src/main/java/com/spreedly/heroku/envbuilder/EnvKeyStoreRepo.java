package com.spreedly.heroku.envbuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import com.heroku.sdk.EnvKeyStore;

public class EnvKeyStoreRepo implements KeyStoreRepo {

  @Override
  public void createTrustStore(String trustedCert, String trustStorePassword, String trustStoreLocation)
      throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
    EnvKeyStore envTrustStore = EnvKeyStore.create(trustedCert, trustStorePassword);
    FileOutputStream trustStore = new FileOutputStream(trustStoreLocation);
    envTrustStore.store(trustStore);
  }

  @Override
  public void createKeyStore(String clientCertKey, String clientCert, String keyStorePassword, String keyStoreLocation)
      throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
    EnvKeyStore envKeyStore = EnvKeyStore.create(clientCertKey, clientCert, keyStorePassword);
    FileOutputStream keyStore = new FileOutputStream(keyStoreLocation);
    envKeyStore.store(keyStore);
  }
}
