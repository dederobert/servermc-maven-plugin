package fr.lehtto.maven.plugins.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class to download and check files.
 *
 * @author Lehtto
 * @version 0.0.1
 * @since 0.0.1
 */
public final class FileDownloader {

  private static final List<String> WHITELISTED_PROTOCOLE = Arrays.asList("http", "https");

  /**
   * Default constructor.
   */
  private FileDownloader() {
    throw new AssertionError("This constructor must not be called");
  }


  /**
   * Checks MD5 digest for given {@link File file}.
   *
   * @param file   the {@link File file} to check
   * @param md5 the digest to compare
   * @return whether the checksum is valid
   * @throws MojoExecutionException when issue occurred while computing digest
   */
  public static boolean checkMd5Checksum(final @NotNull File file, final @NotNull String md5)
      throws MojoExecutionException {
    try {
      final FileInputStream fileInputStream = new FileInputStream(file);
      final String digest = DigestUtils.md5Hex(fileInputStream);
      return digest.equals(md5);
    } catch (final FileNotFoundException e) {
      throw new MojoExecutionException("Unable to found file " + file.getAbsolutePath(), e);
    } catch (final IOException e) {
      throw new MojoExecutionException("Unable to execute SHA256 algorithm", e);
    }
  }

  /**
   * Checks SHA256 digest for given {@link File file}.
   *
   * @param file   the {@link File file} to check
   * @param sha256 the digest to compare
   * @return whether the checksum is valid
   * @throws MojoExecutionException when issue occurred while computing digest
   */
  public static boolean checkSha256Checksum(final @NotNull File file, final @NotNull String sha256)
      throws MojoExecutionException {
    try {
      final FileInputStream fileInputStream = new FileInputStream(file);
      final String digest = DigestUtils.sha256Hex(fileInputStream);
      return digest.equals(sha256);
    } catch (final FileNotFoundException e) {
      throw new MojoExecutionException("Unable to found file " + file.getAbsolutePath(), e);
    } catch (final IOException e) {
      throw new MojoExecutionException("Unable to execute SHA256 algorithm", e);
    }
  }

  /**
   * Downloads file from given {@link URL url}.
   *
   * @param destination the destination file
   * @param url         the source URL
   * @throws MojoExecutionException when issue occurred while downloading the server file.
   * @throws MojoFailureException when unsupported protocol is used
   */
  public static void downloadFile(final File destination, final URL url)
      throws MojoExecutionException, MojoFailureException {
    // Enforce security before downloading file avoid SSRF
    if (!WHITELISTED_PROTOCOLE.contains(url.getProtocol())) {
      throw new MojoFailureException(
          MessageFormat.format("Unable to download file from url {0}, only http and https protocol accepted", url));
    }

    try (final FileOutputStream out = new FileOutputStream(destination)) {
      final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      final byte[] buffer = new byte[8192];
      try (final InputStream in = connection.getInputStream()) {
        int bytesRead;
        while (-1 != (bytesRead = in.read(buffer))) {
          out.write(buffer, 0, bytesRead);
        }
      }
      // Free resources
      connection.disconnect();
    } catch (final IOException e) {
      throw new MojoExecutionException(MessageFormat.format("Unable to download PaperMC from {0}", url), e);
    } catch (final OutOfMemoryError e) {
      throw new MojoExecutionException("Unable to create buffer to download PaperMC. Not enough memory space", e);
    }
  }
}
