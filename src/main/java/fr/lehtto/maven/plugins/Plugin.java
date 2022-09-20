package fr.lehtto.maven.plugins;

import java.io.File;
import java.net.URL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Configuration complex type. <br /> Represents a plugin.
 *
 * @author Lehtto
 * @version 0.0.1
 * @since 0.0.1
 */
public class Plugin {

  private String artifactId;
  private File file;
  private String groupId;
  private String md5;
  private String name;
  private URL url;
  private String sha256;

  /**
   * Default constructor.
   */
  public Plugin() {
    // Default constructor
  }

  /**
   * Gets the plugin artifact ID.
   *
   * @return the plugin artifact ID, can be {@code null}
   */
  public @Nullable String getArtifactId() {
    return artifactId;
  }

  /**
   * Sets the plugin artifact ID.
   *
   * @param artifactId the plugin artifact ID
   */
  public void setArtifactId(final String artifactId) {
    this.artifactId = artifactId;
  }

  /**
   * Gets the plugin file.
   *
   * @return the plugin file, can be {@code null}
   */
  public @Nullable File getFile() {
    return file;
  }

  /**
   * Sets the plugin file.
   *
   * @param file the plugin file
   */
  public void setFile(final File file) {
    this.file = file;
  }

  /**
   * Gets the plugin group ID.
   *
   * @return the plugin group ID, can be {@code null}
   */
  public @Nullable String getGroupId() {
    return groupId;
  }

  /**
   * Sets the plugin group ID.
   *
   * @param groupId the plugin group ID
   */
  public void setGroupId(final String groupId) {
    this.groupId = groupId;
  }

  /**
   * Gets the MD5 digest.
   *
   * @return the MD5 digest
   */
  public @Nullable String getMd5() {
    return md5;
  }

  /**
   * Sets the MD5 digest.
   *
   * @param md5 the MD5 digest
   */
  public void setMd5(final String md5) {
    this.md5 = md5;
  }

  /**
   * Gets the plugin name.
   *
   * @return the plugin name
   */
  public @NotNull String getName() {
    return name;
  }

  /**
   * Sets the plugin name.
   *
   * @param name the plugin name
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * Gets the plugin download URL.
   *
   * @return the plugin download URL, can be {@code null}
   */
  public @Nullable URL getUrl() {
    return url;
  }

  /**
   * Sets the plugin download URL.
   *
   * @param url the plugin download URL
   */
  public void setUrl(final URL url) {
    this.url = url;
  }

  /**
   * Gets the plugin SHA256 digest.
   *
   * @return the plugin SHA256 digest, can be {@code null}
   */
  public @Nullable String getSha256() {
    return sha256;
  }

  /**
   * Sets the plugin SHA256 digest.
   *
   * @param sha256 the plugin SHA256 digest
   */
  public void setSha256(final String sha256) {
    this.sha256 = sha256;
  }
}
