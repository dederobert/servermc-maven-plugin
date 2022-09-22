package fr.lehtto.maven.plugins.papermc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Build entity.
 *
 * @author Lehtto
 * @version 0.0.1
 * @since 0.0.1
 */
public class Build implements Serializable {

  private static final long serialVersionUID = 8002553388773134662L;

  /**
   * Build number.
   */
  @JsonProperty("build")
  private String buildStr;
  /**
   * List of {@link Change changes}.
   */
  private final List<Change> changes;
  /**
   * Build channel.
   */
  private String channel;
  /**
   * Build {@link Download download}.
   */
  private Download downloads;
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
   * Whether the build is promoted.
   */
  private boolean promoted;
  /**
   * Build time.
   */
  private String time;
  /**
   * PaperMC version.
   */
  private String version;

  /**
   * Default constructor.
   */
  public Build() {
    changes = new ArrayList<>();
  }

  /**
   * Gets the build number.
   *
   * @return the build number
   */
  public String getBuild() {
    return buildStr;
  }

  /**
   * Sets the build number.
   *
   * @param build the build number
   */
  public void setBuild(final String build) {
    this.buildStr = build;
  }

  /**
   * Gets the list of {@link Change changes}.
   *
   * @return immutable copy of {@link Change changes}
   */
  public List<Change> getChanges() {
    return Collections.unmodifiableList(changes);
  }

  /**
   * Sets the list of {@link Change changes}.
   *
   * @param changes the list of {@link Change changes}
   */
  public void setChanges(final List<Change> changes) {
    this.changes.clear();
    if (null != changes) {
      this.changes.addAll(changes);
    }
  }

  /**
   * Gets the channel.
   *
   * @return the channel
   */
  public String getChannel() {
    return channel;
  }

  /**
   * Sets the channel.
   *
   * @param channel the channel
   */
  public void setChannel(final String channel) {
    this.channel = channel;
  }

  /**
   * Gets the {@link Download downloads}.
   *
   * @return the {@link Download downloads}
   */
  public Download getDownloads() {
    return downloads;
  }

  /**
   * Sets the {@link Download downloads}.
   *
   * @param downloads the {@link Download downloads}
   */
  public void setDownloads(final Download downloads) {
    this.downloads = downloads;
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
   * @param projectId the project ID
   */
  public void setProjectId(final String projectId) {
    this.projectId = projectId;
  }

  /**
   * Gets project name.
   *
   * @return the project name.
   */
  public String getProjectName() {
    return projectName;
  }

  /**
   * Sets the project name.
   *
   * @param projectName the project name
   */
  public void setProjectName(final String projectName) {
    this.projectName = projectName;
  }

  /**
   * Whether the build is promoted.
   *
   * @return {@code true} when the build is promoted
   */
  public boolean isPromoted() {
    return promoted;
  }

  /**
   * Sets the flag which indicates whether the build is promoted.
   *
   * @param promoted the flag
   */
  public void setPromoted(final boolean promoted) {
    this.promoted = promoted;
  }

  /**
   * Gets the related Minecraft server version.
   *
   * @return the Minecraft server version
   */
  public String getVersion() {
    return version;
  }

  /**
   * Sets the Minecraft server version.
   *
   * @param version the Minecraft server version
   */
  public void setVersion(final String version) {
    this.version = version;
  }

  /**
   * Gets the build time.
   *
   * @return the build time
   */
  public String getTime() {
    return time;
  }

  /**
   * Sets the build time.
   *
   * @param time the build time
   */
  public void setTime(final String time) {
    this.time = time;
  }
}