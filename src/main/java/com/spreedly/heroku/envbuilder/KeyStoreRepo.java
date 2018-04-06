package com.spreedly.heroku.envbuilder;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public interface KeyStoreRepo {

  void createKeyStore(String clientCertKey, String clientCert, String keyStorePassword, String keyStoreLocation)
      throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException;

  void createTrustStore(String trustedCert, String trustStorePassword, String trustStoreLocation)
      throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException;
}
