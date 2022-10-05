package fr.lehtto.maven.plugins.papermc.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Change entity. <br /> Represents a change in PaperMC server.
 *
 * @author Lehtto
 * @version 0.0.1
 * @since 0.0.1
 */
public class Change implements Serializable {


  private static final long serialVersionUID = -1358139167887736105L;

  /**
   * Change's commit ID.
   */
  private String commit;
  /**
   * Change's summary.
   */
  private String summary;
  /**
   * Change's message.
   */
  private String message;

  /**
   * Default constructor.
   */
  public Change() {
    // Default constructor
  }

  /**
   * Gets the change's commit ID.
   *
   * @return the commit ID
   */
  public String getCommit() {
    return commit;
  }

  /**
   * Sets the change's commit ID.
   *
   * @param commit the commit ID
   */
  public void setCommit(final String commit) {
    this.commit = commit;
  }

  /**
   * Gets the change's summary.
   *
   * @return the summary
   */
  public String getSummary() {
    return summary;
  }

  /**
   * Sets the change's summary.
   *
   * @param summary the summary
   */
  public void setSummary(final String summary) {
    this.summary = summary;
  }

  /**
   * Gets the change's message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the change's message.
   *
   * @param message the message
   */
  public void setMessage(final String message) {
    this.message = message;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (null == o || getClass() != o.getClass()) {
      return false;
    }
    final Change change = (Change) o;
    return Objects.equals(getCommit(), change.getCommit()) && Objects.equals(getSummary(),
        change.getSummary()) && Objects.equals(getMessage(), change.getMessage());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCommit(), getSummary(), getMessage());
  }

  @Override
  public String toString() {
    return "Change{" +
        "commit='" + commit + '\'' +
        ", summary='" + summary + '\'' +
        ", message='" + message + '\'' +
        '}';
  }
}