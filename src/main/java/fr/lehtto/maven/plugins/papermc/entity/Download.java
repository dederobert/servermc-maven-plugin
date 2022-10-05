package fr.lehtto.maven.plugins.papermc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * Download entity.
 *
 * @author Lehtto
 * @version 0.0.1
 * @since 0.0.1
 */
public class Download implements Serializable {

  private static final long serialVersionUID = -3866191674301952938L;

  /**
   * {@link Application} related to this download.
   */
  private Application application;
  /**
   * {@link MojangMapping Mojang mappings}.
   */
  @JsonProperty("mojang-mappings")
  private MojangMapping mojangMappings;

  /**
   * Default constructor.
   */
  public Download() {
    // Default constructor
  }

  /**
   * Gets the {@link Application application}.
   *
   * @return the {@link Application application}
   */
  public Application getApplication() {
    return application;
  }

  /**
   * Sets the {@link Application application}.
   *
   * @param application the {@link Application application}
   */
  public void setApplication(final Application application) {
    this.application = application;
  }

  /**
   * Gets the {@link MojangMapping mappings}.
   *
   * @return the {@link MojangMapping mappings}
   */
  public MojangMapping getMojangMappings() {
    return mojangMappings;
  }

  /**
   * Sets the {@link MojangMapping mappings}.
   *
   * @param mojangMappings the {@link MojangMapping mappings}
   */
  public void setMojangMappings(final MojangMapping mojangMappings) {
    this.mojangMappings = mojangMappings;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (null == o || getClass() != o.getClass()) {
      return false;
    }
    final Download download = (Download) o;
    return Objects.equals(getApplication(), download.getApplication()) && Objects.equals(
        getMojangMappings(), download.getMojangMappings());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getApplication(), getMojangMappings());
  }

  @Override
  public String toString() {
    return "Download{" +
        "application=" + application +
        ", mojangMappings=" + mojangMappings +
        '}';
  }
}