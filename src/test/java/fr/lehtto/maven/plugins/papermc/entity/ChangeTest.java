package fr.lehtto.maven.plugins.papermc.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanVerifier;

/**
 * Tests for {@link Change}.
 *
 * @author Lehtto
 * @version 0.1.0
 * @since 0.1.0
 */
@DisplayName("Change POJO")
class ChangeTest {

  /**
   * Bean test for class {@link Change}.
   */
  @DisplayName("Test valid POJO")
  @Test
  void testPojo() {
    BeanVerifier.verifyBean(Change.class);
  }
}