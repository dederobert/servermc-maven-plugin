package fr.lehtto.maven.plugins;

import fr.lehtto.maven.plugins.util.FileDownloader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

/**
 * Copies plugins to server plugins folder. Copies the current plugin and defined additionalPlugins.
 *
 * @author Lehtto
 * @version 0.0.1
 * @since 0.0.1
 */
@Mojo(name = "copy-plugin", requiresDependencyResolution = ResolutionScope.COMPILE)
public class CopyPluginMojo extends AbstractServerMcMojo {

  /**
   * The {@link MavenProject maven project} entity.
   */
  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  /**
   * The build folder.
   */
  @Parameter(property = "buildFolder", readonly = true, defaultValue = "${project.build.directory}")
  private File buildFolder;

  /**
   * The jar name.
   */
  @Parameter(property = "jarName", defaultValue = "${project.build.finalName}")
  private String jarName;

  /**
   * The list of additional plugins to copy.
   */
  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  @Parameter(property = "additionalPlugins")
  private List<Plugin> additionalPlugins;

  /**
   * Whether the additional plugins should be resolved and copied.
   */
  @Parameter(property = "skipAdditionalPlugins", required = true, defaultValue = "false")
  private boolean skipAdditionalPlugins;

  /**
   * Default constructor.
   */
  public CopyPluginMojo() {
    // Default constructor
  }

  /**
   * Starts minecraft server by doing following steps.
   * <ul>
   *   <li>Checks EULA</li>
   *   <li>Creates server directory</li>
   *   <li>Retrieves build number</li>
   *   <li>Retrieves JAR URL</li>
   *   <li>Downloads PaperMC</li>
   *   <li>Creates configuration files</li>
   * </ul>
   */
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    // Creates server directory
    if (!getServerDirectory().exists()) {
      getLog().debug("Create server directory");
      if (!getServerDirectory().mkdirs()) {
        throw new MojoExecutionException("Unable to create server directory");
      }
    }

    copyPlugin();

    if (skipAdditionalPlugins) {
      getLog().info("Additional plugins are skipped");
    } else {
      copyAdditionalPlugin();
    }
  }

  /**
   * Copies plugin JAR into server plugins folder.
   *
   * @throws MojoExecutionException when issue occurred while coping JAR file
   */
  @VisibleForTesting
  void copyPlugin() throws MojoExecutionException {
    final File pluginsFolder = createFile(getServerDirectory(), "plugins");
    final File sourcePluginJar = createFile(buildFolder, jarName + ".jar");
    final File destinationPluginJar = createFile(pluginsFolder, jarName + ".jar");

    if (!pluginsFolder.exists() && !pluginsFolder.mkdirs()) {
      throw new MojoExecutionException("Unable to create plugins directory");
    }
    getLog().info(MessageFormat.format("Copy plugin JAR {0}.jar", jarName));
    copyFile(sourcePluginJar, destinationPluginJar);
    getLog().info("Plugin JAR copied");
  }

  /**
   * Copies plugin JAR into server plugins folder.
   *
   * @throws MojoExecutionException when issue occurred while coping JAR file
   * @throws MojoFailureException   when one plugin checksum is not valid
   */
  @VisibleForTesting
  void copyAdditionalPlugin() throws MojoExecutionException, MojoFailureException {
    getLog().info("Copy additional plugins");
    final File pluginsFolder = new File(getServerDirectory(), "plugins");

    for (final Plugin plugin : additionalPlugins) {
      final File destinationPluginJar = new File(pluginsFolder, FilenameUtils.getName( plugin.getName() + ".jar"));
      if (null != plugin.getFile()) {
        // Plugin file is defined
        getLog().info(MessageFormat.format("Copy plugin {0} from file {1}", plugin.getName(), plugin.getFile()));
        copyFile(plugin.getFile(), destinationPluginJar);
      } else if (null != plugin.getUrl()) {
        // Plugin download URL is defined
        getLog().info(MessageFormat.format("Download plugin {0} from url {1}", plugin.getName(), plugin.getUrl()));
        FileDownloader.downloadFile(destinationPluginJar, plugin.getUrl());
        if (null != plugin.getSha256() && !plugin.getSha256().isEmpty() && !FileDownloader.checkSha256Checksum(
            destinationPluginJar, plugin.getSha256())) {
          throw new MojoFailureException(MessageFormat.format("Plugin {0} checksum is not valid", plugin.getName()));
        }
        if (null != plugin.getSha256() && !plugin.getSha256().isEmpty() && !FileDownloader.checkMd5Checksum(
            destinationPluginJar, plugin.getSha256())) {
          throw new MojoFailureException(MessageFormat.format("Plugin {0} checksum is not valid", plugin.getName()));
        }
      } else if (StringUtils.isNotBlank(plugin.getArtifactId()) && StringUtils.isNotBlank(plugin.getGroupId())) {
        getLog().info(MessageFormat.format("Copy plugin {0} from dependencies", plugin.getName()));
        final Artifact artifact = searchArtifact(plugin.getGroupId(), plugin.getArtifactId())
            .orElseThrow(() -> new MojoFailureException(
                MessageFormat.format("Unable to found plugin {0} in dependencies with groupId {1} and artifactId {2}",
                    plugin.getName(), plugin.getGroupId(), plugin.getArtifactId())));
        final File sourceFile = artifact.getFile();
        final File destinationFile = new File(pluginsFolder, FilenameUtils.getName(artifact.getArtifactId() + ".jar"));
        getLog().info("Plugin found in dependencies");
        copyFile(sourceFile, destinationFile);
      } else {
        throw new MojoFailureException(
            MessageFormat.format(
                "Not source provided for plugin {0}, please provide a file, an URL or a groupId/artifactId",
                plugin.getName()));
      }
    }
    getLog().info("Additional plugins copied");
  }

  /**
   * Searches artifact in project.
   *
   * @param groupId    the artifact's group ID
   * @param artifactId the artifact's ID
   * @return the found artifact or {@link Optional#empty()}
   * @throws MojoFailureException when the artifact is found but not resolved
   */
  private Optional<Artifact> searchArtifact(final @NotNull String groupId, final @NotNull String artifactId)
      throws MojoFailureException {
    for (final Object object : project.getDependencyArtifacts()) {
      if (object instanceof Artifact) {
        final Artifact artifact = (Artifact) object;
        if (artifact.getGroupId().equals(groupId) && artifact.getArtifactId()
            .equals(artifactId)) {
          if (!artifact.isResolved()) {
            throw new MojoFailureException(
                MessageFormat.format("Artifact {0} is not yet resolved", artifact.getArtifactId()));
          }
          return Optional.of(artifact);
        }
      }
    }
    return Optional.empty();
  }

  /**
   * Copy file from given source to given destination.
   *
   * @param sourcePluginJar      the source {@link File file}
   * @param destinationPluginJar the destination {@link File file}
   * @throws MojoExecutionException when issue occurred while copying the file
   */
  @VisibleForTesting
  void copyFile(final File sourcePluginJar, final File destinationPluginJar) throws MojoExecutionException {

    try (final FileInputStream in = new FileInputStream(sourcePluginJar)) {
      try (final FileOutputStream out = new FileOutputStream(destinationPluginJar)) {
        final byte[] buffer = new byte[1024];
        int lengthRead;
        while (0 < (lengthRead = in.read(buffer))) {
          out.write(buffer, 0, lengthRead);
          out.flush();
        }
      }
    } catch (final IOException e) {
      throw new MojoExecutionException("Unable to copy plugin " + sourcePluginJar.getName() + " JAR file", e);
    } catch (final OutOfMemoryError e) {
      throw new MojoExecutionException("Unable to create buffer to download PaperMC. Not enough memory space", e);
    }
  }

  /**
   * Creates new instance of {@link File}.
   *
   * @param parent the parent {@link File}
   * @param path   the path
   * @return new instance of {@link File}
   */
  @VisibleForTesting
  File createFile(final File parent, final String path) {
    return new File(parent, path);
  }
}