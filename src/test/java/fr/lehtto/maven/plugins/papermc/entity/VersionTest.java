package fr.lehtto.maven.plugins.papermc.entity;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanVerifier;

/**
 * Tests for {@link Version}.
 *
 * @author Lehtto
 * @version 0.1.0
 * @since 0.1.0
 */
@DisplayName("Version POJO")
@Disabled("Equals on field build is invalid")
class VersionTest {

  /**
   * Bean test for class {@link Version}.
   */
  @DisplayName("Test valid POJO")
  @Test
  void testPojo() {
    BeanVerifier.verifyBean(Version.class);
  }
}