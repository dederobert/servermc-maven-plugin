package fr.lehtto.maven.plugins.papermc.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Application entity.
 *
 * @author Lehtto
 * @version 0.0.1
 * @since 0.0.1
 */
public class Application implements Serializable {

  private static final long serialVersionUID = 7368436452277703491L;

  /**
   * Application name.
   */
  private String name;
  /**
   * Application SHA256 digest.
   */
  private String sha256;

  /**
   * Default constructor.
   */
  public Application() {
    // Default constructor
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name the name
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * Gets the SHA256 digest.
   *
   * @return the SHA256 digest
   */
  public String getSha256() {
    return sha256;
  }

  /**
   * Sets the SHA256 digest.
   *
   * @param sha256 the SHA256 digest
   */
  public void setSha256(final String sha256) {
    this.sha256 = sha256;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Application)) {
      return false;
    }
    final Application that = (Application) o;
    return Objects.equals(getName(), that.getName()) && Objects.equals(getSha256(), that.getSha256());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getSha256());
  }

  @Override
  public String toString() {
    return "Application{" +
        "name='" + name + '\'' +
        ", sha256='" + sha256 + '\'' +
        '}';
  }
}