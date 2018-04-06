package com.spreedly.heroku.envbuilder;

import java.util.Properties;

public interface ConfigBuilder {
  void apply(Properties properties);
}
