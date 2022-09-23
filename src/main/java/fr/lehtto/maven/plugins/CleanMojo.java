package fr.lehtto.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.function.Predicate;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

/**
 * Cleans server folder.
 *
 * @author lehtto
 * @version 0.0.1
 * @since 0.0.1
 */
@Mojo(name = "clean")
public class CleanMojo extends AbstractServerMcMojo {

  @Parameter(property = "ignoreWorlds", required = true, defaultValue = "false")
  private boolean ignoreWorlds;

  /**
   * Default constructor.
   */
  public CleanMojo() {
    // Default constructor
  }

  /**
   * Starts the PaperMC server.
   */
  @Override
  public void execute() throws MojoExecutionException {
    getLog().info("Delete server folder");
    deleteDir(getServerDirectory(), dir -> !ignoreWorlds || !isWorldDir(dir));
    getLog().info("Server folders deleted");
  }

  /**
   * Deletes a directory and all its sub-elements.
   *
   * @param file   the directory to delete
   * @param filter {@link Predicate predicate} which indicate whether the file should be deleted
   * @return whether the folder is deleted
   * @throws MojoExecutionException when a file/directory cannot be deleted
   */
  @VisibleForTesting
  boolean deleteDir(final File file, final Predicate<File> filter) throws MojoExecutionException {
    if (file.exists()) {
      final File[] content = file.listFiles();
      boolean cleaned = true;
      if (null != content) {
        for (final File subFile : content) {
          if (filter.test(subFile)) {
            cleaned &= deleteDir(subFile, filter);
          } else {
            cleaned = false;
          }
        }
      }
      if (cleaned && filter.test(file)) {
        try {
          getLog().debug("Delete " + file.getAbsolutePath());
          return Files.deleteIfExists(file.toPath());
        } catch (final IOException e) {
          throw new MojoExecutionException(MessageFormat.format("Unable to delete {0}", file.getAbsolutePath()), e);
        }
      }
      getLog().info(MessageFormat.format("Spare the {0} file/folder", file.getName()));
      return false;
    }
    return true;
  }

  /**
   * Checks whether the {@link File directory} is a world directory.
   *
   * @param directory the {@link File directory} to check
   * @return {@code true} is the {@link File directory} is a world directory
   */
  private boolean isWorldDir(final @NotNull File directory) {
    final String directoryName = directory.getName();
    return Arrays.asList("world", "world_nether", "world_the_end").contains(directoryName);
  }

}
