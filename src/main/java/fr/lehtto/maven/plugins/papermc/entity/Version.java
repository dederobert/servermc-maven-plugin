package fr.lehtto.maven.plugins.papermc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Version entity.
 *
 * @author Lehtto
 * @version 0.0.1
 * @since 0.0.1
 */
public class Version implements Serializable {

  private static final long serialVersionUID = 6376247568674063744L;

  /**
   * List of build number.
   */
  private final List<Integer> builds;
  /**
   * Project ID.
   */
  @JsonProperty("project_id")
  private String projectId;
  /**
   * Project name.
   */
  @JsonProperty("project_name")
  private String projectName;
  /**
   * Minecraft version.
   */
  @JsonProperty("version")
  private String versionStr;

  /**
   * Default constructor.
   */
  public Version() {
    builds = new ArrayList<>();
  }

  /**
   * Gets the list of build number.
   *
   * @return immutable copy list of build number
   */
  public @Unmodifiable List<Integer> getBuilds() {
    return Collections.unmodifiableList(builds);
  }

  /**
   * Sets the list of build number.
   *
   * @param builds the list of build number to set
   */
  public void setBuilds(final List<Integer> builds) {
    this.builds.clear();
    if (null != builds) {
      this.builds.addAll(builds);
    }
  }

  /**
   * Gets the project ID.
   *
   * @return the project ID
   */
  public String getProjectId() {
    return projectId;
  }

  /**
   * Sets the project ID.
   *
   * @param projectId the project ID to set
   */
  public void setProjectId(final String projectId) {
    this.projectId = projectId;
  }

  /**
   * Gets the project name.
   *
   * @return the project name
   */
  public String getProjectName() {
    return projectName;
  }

  /**
   * Sets the project name.
   *
   * @param projectName the project name to set
   */
  public void setProjectName(final String projectName) {
    this.projectName = projectName;
  }

  /**
   * Gets the version.
   *
   * @return the version
   */
  public String getVersion() {
    return versionStr;
  }

  /**
   * Sets the version.
   *
   * @param version the version to set
   */
  public void setVersion(final String version) {
    this.versionStr = version;
  }

  /**
   * Gets the latest build number.
   *
   * @return the latest build number
   */
  public int getLatestBuild() {
    return Collections.max(getBuilds());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (null == o || getClass() != o.getClass()) {
      return false;
    }
    final Version version = (Version) o;
    return Objects.equals(getBuilds(), version.getBuilds()) && Objects.equals(getProjectId(),
        version.getProjectId()) && Objects.equals(getProjectName(), version.getProjectName())
        && Objects.equals(versionStr, version.versionStr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getBuilds(), getProjectId(), getProjectName(), versionStr);
  }

  @Override
  public String toString() {
    return "Version{" +
        "project_id='" + projectId + '\'' +
        ", project_name='" + projectName + '\'' +
        ", version='" + versionStr + '\'' +
        ", builds=" + builds +
        '}';
  }
}