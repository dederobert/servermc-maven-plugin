package fr.lehtto.maven.plugins;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanVerifier;

/**
 * Tests for {@link Plugin}.
 *
 * @author Lehtto
 * @version 0.1.0
 * @since 0.1.0
 */
@DisplayName("Plugin POJO")
class PluginTest {

  /**
   * Bean test for class {@link Plugin}.
   */
  @DisplayName("Test valid POJO")
  @Test
  void testPojo() {
    BeanVerifier.verifyBean(Plugin.class);
  }
}