package fr.lehtto.maven.plugins;

import java.io.IOException;
import java.text.MessageFormat;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jetbrains.annotations.VisibleForTesting;

/**
 * Starts the Minecraft server.
 *
 * @author lehtto
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings("UseOfProcessBuilder")
@Mojo(name = "start")
public class StartMojo extends AbstractServerMcMojo {

  /**
   * The minimum size, in Go, of memory allocation pool ({@literal -Xms}).
   */
  @Parameter(property = "memoryMin", defaultValue = "1")
  private int memoryMin;

  /**
   * The maximum size, in Go, of memory allocation pool ({@literal -Xms}).
   */
  @Parameter(property = "memoryMax", defaultValue = "2")
  private int memoryMax;

  /**
   * Default constructor.
   */
  public StartMojo() {
    // Default constructor
  }

  /**
   * Starts the PaperMC server.
   */
  @Override
  public void execute() throws MojoExecutionException {
    final ProcessBuilder processBuilder = createProcessBuilder();
    processBuilder.directory(getServerDirectory());
    processBuilder.inheritIO();
    try {
      getLog().info("Starting Minecraft server");
      getLog().info(MessageFormat.format("Initial memory allocation pool size {0} Go", memoryMin));
      getLog().info(MessageFormat.format("Maximum memory allocation pool size {0} Go", memoryMax));
      final Process process = processBuilder.start();
      process.waitFor();
      getLog().info("Minecraft server stopped");
    } catch (final IOException e) {
      throw new MojoExecutionException("Unable to start PaperMC server", e);
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new MojoExecutionException("An issue occurred.", e);
    }
  }

  /**
   * Creates new instance of {@link ProcessBuilder} configured to start the server.
   *
   * @return the new {@link ProcessBuilder}
   */
  @VisibleForTesting
  ProcessBuilder createProcessBuilder() {
    // Use the current java program to run server
    final String java = System.getProperty("java.home") + "/bin/java";
    getLog().info(MessageFormat.format("Use java {0}", java));
    //noinspection StringConcatenationMissingWhitespace
    return new ProcessBuilder(java, "-Xms" + memoryMin + 'G', "-Xmx" + memoryMax + 'G',
        "-jar", "server.jar", "--nogui");
  }
}
