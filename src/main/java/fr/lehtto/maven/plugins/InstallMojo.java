package fr.lehtto.maven.plugins;

import fr.lehtto.maven.plugins.papermc.PaperApiClient;
import fr.lehtto.maven.plugins.papermc.entity.Build;
import fr.lehtto.maven.plugins.papermc.entity.Version;
import fr.lehtto.maven.plugins.util.FileDownloader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jetbrains.annotations.VisibleForTesting;

/**
 * Installs (download, setup) the server.
 *
 * @author Lehtto
 * @version 0.0.1
 * @since 0.0.1
 */
@Mojo(name = "install")
public class InstallMojo extends AbstractServerMcMojo {

  private static final String BASE_URL = "https://api.papermc.io/v2/projects/paper/";
  private final PaperApiClient client;

  /**
   * The base URL to use to download paper MC.
   */
  @Parameter(property = "base-url", defaultValue = BASE_URL)
  private String baseUrl;

  /**
   * The build number to use.
   */
  @Parameter(property = "buildNumber")
  private String buildNumber;

  /**
   * Whether the EULA is agreed.
   */
  @Parameter(property = "eula", defaultValue = "false")
  private boolean eula;

  /**
   * Minecraft version to use.
   */
  @Parameter(property = "mc-version", required = true)
  private String mcVersion;

  /**
   * Whether the JAR file has to be overridden if it exists.
   */
  @Parameter(property = "override-jar", required = true, defaultValue = "false")
  private boolean overrideJar;

  /**
   * Whether the JAR file has to be overridden if it exists.
   */
  @Parameter(property = "override-properties", required = true, defaultValue = "false")
  private boolean overrideProperties;

  /**
   * Initial values of "server.properties".
   */
  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  @Parameter(property = "properties")
  private Map<String, String> properties;


  private File eulaFile;
  private File jarFile;
  private String jarUrl;
  private File propertiesFile;
  private String sha256;

  /**
   * Default constructor.
   */
  public InstallMojo() {
    client = new PaperApiClient();
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
    getLog().info(MessageFormat.format("Initializing PaperMC server for minecraft version {0}", mcVersion));
    if (!eula) {
      throw new MojoFailureException(
          "Please read EULA from https://aka.ms/MinecraftEULA and indicate your agreement by using parameter eula");
    }

    // Creates server directory
    if (!getServerDirectory().exists()) {
      getLog().debug("Create server directory");
      getServerDirectory().mkdirs();
    }

    // Defines files
    jarFile = new File(getServerDirectory(), "server.jar");
    propertiesFile = new File(getServerDirectory(), "server.properties");
    eulaFile = new File(getServerDirectory(), "eula.txt");

    createEulaFile();

    if (!jarFile.exists() || overrideJar) {
      retrievePaperBuildNumber();
      retrieveJarUrl();
      downloadPaper();
    }

    if (!propertiesFile.exists() || overrideProperties) {
      createPropertiesFile();
    }
  }


  /**
   * Creates {@literal eula.txt} file.
   *
   * @throws MojoExecutionException when issue occurred while writing file content
   */
  @VisibleForTesting
  void createEulaFile() throws MojoExecutionException {
    try (final FileOutputStream eulaOut = new FileOutputStream(eulaFile)) {
      eulaOut.write("eula=true".getBytes(StandardCharsets.UTF_8));
    } catch (final IOException e) {
      throw new MojoExecutionException("Unable to create EULA file", e);
    }
  }

  /**
   * Retrieves the latest paper build number for specified version.
   *
   * @throws MojoExecutionException when issue occurred while calling PaperMC API
   */
  @VisibleForTesting
  void retrievePaperBuildNumber() throws MojoExecutionException {
    if (null != buildNumber && !buildNumber.trim().isEmpty()) {
      // Paper build number is set by the user
      return;
    }

    // Call Paper API
    getLog().info(MessageFormat.format("Retrieve latest PaperMC build number for minecraft version {0}", mcVersion));
    final Version paperVersion = client.retrieveVersion(baseUrl, mcVersion);
    final int latestBuildVersion = paperVersion.getLatestBuild();
    getLog().info(MessageFormat.format("Use PaperMC build version {0}", latestBuildVersion));
    buildNumber = String.valueOf(latestBuildVersion);
  }

  /**
   * Retrieves the JAR download URL for specified version and specified build number.
   *
   * @throws MojoExecutionException when issue occurred while calling PaperMC API
   */
  @VisibleForTesting
  void retrieveJarUrl() throws MojoExecutionException {
    getLog().info(
        MessageFormat.format("Retrieve JAR name for minecraft version {0} and paper build number {1}", mcVersion,
            buildNumber));
    final Build paperBuild = client.retrieveBuild(baseUrl, mcVersion, buildNumber);
    final String jarName = paperBuild.getDownloads().getApplication().getName();
    final String buildTime = paperBuild.getTime();
    sha256 = paperBuild.getDownloads().getApplication().getSha256();

    getLog().info(MessageFormat.format("Using PaperMC JAR \"{0}\", built on {1}", jarName, buildTime));
    jarUrl = MessageFormat.format("{0}versions/{1}/builds/{2}/downloads/{3}",
        baseUrl, mcVersion, this.buildNumber, jarName);
  }

  /**
   * Downloads the PaperMC server JAR.
   *
   * @throws MojoExecutionException when issue occurred while downloading the server file.
   * @throws MojoFailureException   when checksum verification failed
   */
  @VisibleForTesting
  void downloadPaper() throws MojoExecutionException, MojoFailureException {
    getLog().info(MessageFormat.format("Starting downloading server JAR from {0}", jarUrl));
    try {
      FileDownloader.downloadFile(jarFile, new URL(jarUrl));
    } catch (final IOException e) {
      throw new MojoExecutionException(MessageFormat.format("JAR URL {0} is incorrect", jarUrl), e);
    }

    getLog().info("Verify JAR checksum");
    if (!FileDownloader.checkSha256Checksum(jarFile, sha256)) {
      throw new MojoFailureException("Server JAR checksum is not valid");
    }
    getLog().info("PaperMC downloaded");
  }


  /**
   * Creates the {@literal server.properties} file with configured properties.
   *
   * @throws MojoExecutionException when issue occurred while writing file content.
   */
  @VisibleForTesting
  void createPropertiesFile() throws MojoExecutionException {
    getLog().info("Creating properties file");
    // Clear the properties fil
    try (final FileOutputStream out = new FileOutputStream(propertiesFile)) {
      out.write("#Add server configuration".getBytes(StandardCharsets.UTF_8));
    } catch (final IOException e) {
      throw new MojoExecutionException("Unable to clear properties file", e);
    }

    // Write properties
    try (final FileOutputStream out = new FileOutputStream(propertiesFile, true)) {
      for (final Entry<String, String> property : properties.entrySet()) {
        out.write('\n');
        out.write(property.getKey().getBytes(StandardCharsets.UTF_8));
        out.write('=');
        out.write(property.getValue().getBytes(StandardCharsets.UTF_8));
      }
    } catch (final IOException e) {
      throw new MojoExecutionException("Unable to create properties file", e);
    }
  }
}