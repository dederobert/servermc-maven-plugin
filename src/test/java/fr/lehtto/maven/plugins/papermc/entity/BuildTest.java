package fr.lehtto.maven.plugins.papermc.entity;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanVerifier;

/**
 * Tests for {@link Build}.
 *
 * @author Lehtto
 * @version 0.1.0
 * @since 0.1.0
 */
@DisplayName("Build POJO")
@Disabled("Equals on field changes is invalid")
class BuildTest {

  /**
   * Bean test for class {@link Build}.
   */
  @DisplayName("Test valid POJO")
  @Test
  void testPojo() {
    BeanVerifier.verifyBean(Build.class);
  }
}