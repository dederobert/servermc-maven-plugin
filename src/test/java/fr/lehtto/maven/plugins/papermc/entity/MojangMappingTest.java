package fr.lehtto.maven.plugins.papermc.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanVerifier;

/**
 * Tests for {@link MojangMapping}.
 *
 * @author Lehtto
 * @version 0.1.0
 * @since 0.1.0
 */
@DisplayName("MojangMapping POJO")
class MojangMappingTest {

  /**
   * Bean test for class {@link MojangMapping}.
   */
  @DisplayName("Test valid POJO")
  @Test
  void testPojo() {
    BeanVerifier.verifyBean(MojangMapping.class);
  }
}