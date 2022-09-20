package fr.lehtto.maven.plugins;

import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Abstract MOJO for servermc plugin.
 *
 * @author Lehtto
 * @version 0.0.1
 * @since 0.0.1
 */
abstract class AbstractServerMcMojo extends AbstractMojo {

  /**
   * The target server's directory.
   */
  @Parameter(property = "serverDirectory", defaultValue = "target/server")
  private File serverDirectory;

  /**
   * Gets the server directory parameter.
   *
   * @return the server directory parameter
   */
  File getServerDirectory() {
    return serverDirectory;
  }
}
