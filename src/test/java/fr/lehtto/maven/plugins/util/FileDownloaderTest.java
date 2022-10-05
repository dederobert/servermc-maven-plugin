package fr.lehtto.maven.plugins.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link FileDownloader}.
 *
 * @author Lehtto
 * @version 0.1.0
 * @since 0.1.0
 */
class FileDownloaderTest {

  private static final String SHA256_TEST_FILE_PATH = "fr/lehtto/maven/plugins/util/testSha256CheckSum.txt";
  private static final String MD5_TEST_FILE_PATH = "fr/lehtto/maven/plugins/util/testMd5CheckSum.txt";

  /**
   * Tests {@link FileDownloader#checkMd5Checksum(File, String)}.
   */
  @DisplayName("CheckMd5Checksum")
  @Nested
  class CheckMd5ChecksumTests {

    /**
     * Valid MD5.
     *
     * @throws MojoExecutionException when an issue occurred
     * @throws URISyntaxException     when issue occurred
     */
    @DisplayName("Valid MD5")
    @Test
    void withValidMd5() throws MojoExecutionException, URISyntaxException {
      // GIVEN
      final URL resource = getClass().getClassLoader().getResource(MD5_TEST_FILE_PATH);
      assert null != resource;
      final String md5 = "1cb8d79565024aed7c5de47e4c2e169d";

      final boolean result = FileDownloader.checkMd5Checksum(new File(resource.toURI().getPath()), md5);
      assertThat(result).isTrue();
    }

    /**
     * Invalid MD5.
     *
     * @throws MojoExecutionException when an issue occurred
     * @throws URISyntaxException     when issue occurred
     */
    @DisplayName("Invalid MD5")
    @Test
    void withInvalidMd5() throws MojoExecutionException, URISyntaxException {
      // GIVEN
      final URL resource = getClass().getClassLoader().getResource(MD5_TEST_FILE_PATH);
      assert null != resource;
      final String md5 = "invalidMd5";

      final boolean result = FileDownloader.checkMd5Checksum(new File(resource.toURI().getPath()), md5);
      assertThat(result).isFalse();
    }
  }

  /**
   * Tests for {@link FileDownloader#checkSha256Checksum(File, String)}.
   */
  @DisplayName("CheckSha256Checksum")
  @Nested
  class CheckSha256Checksum {

    /**
     * Valid SHA256.
     *
     * @throws MojoExecutionException when issue occurred
     * @throws URISyntaxException     when issue occurred
     */
    @DisplayName("Valid SHA256")
    @Test
    void withValidSha256() throws MojoExecutionException, URISyntaxException {
      final URL resource = getClass().getClassLoader().getResource(SHA256_TEST_FILE_PATH);
      assert null != resource : "Unable to found test file";
      final String sha256 = "c2b9a27cd3972d6ca86ddeda12972ebfe51676df6500d0f149cc8f9f8e43fa3d";

      final boolean result = FileDownloader.checkSha256Checksum(new File(resource.toURI().getPath()), sha256);
      assertThat(result).isTrue();
    }

    /**
     * Invalid SHA256.
     *
     * @throws MojoExecutionException when issue occurred
     * @throws URISyntaxException     when issue occurred
     */
    @DisplayName("Valid SHA256")
    @Test
    void withInvalidSha256() throws MojoExecutionException, URISyntaxException {
      final URL resource = getClass().getClassLoader().getResource(SHA256_TEST_FILE_PATH);
      assert null != resource : "Unable to found test file";
      final String sha256 = "invalidSha256";

      final boolean result = FileDownloader.checkSha256Checksum(new File(resource.toURI().getPath()), sha256);
      assertThat(result).isFalse();
    }
  }

//  @Test
//  void testDownloadFile() throws MojoExecutionException, MojoFailureException {
//    FileDownloader.downloadFile(
//        new File(getClass().getResource("/fr/lehtto/maven/plugins/util/PleaseReplaceMeWithTestFile.txt").getFile()),
//        null);
//  }
}

//Generated with love by TestMe :)
// Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme