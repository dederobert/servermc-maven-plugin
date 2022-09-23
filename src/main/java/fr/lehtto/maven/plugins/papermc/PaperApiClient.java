package fr.lehtto.maven.plugins.papermc;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lehtto.maven.plugins.papermc.entity.Build;
import fr.lehtto.maven.plugins.papermc.entity.Version;
import java.io.IOException;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Client for Paper API.
 *
 * @author Lehtto
 * @version 0.0.1
 * @since 0.0.1
 */
public class PaperApiClient {

  private static final int HTTP_OK = 200;
  private final OkHttpClient client;
  private final ObjectMapper objectMapper;

  /**
   * Default constructor.
   */
  public PaperApiClient() {
    client = new OkHttpClient();
    objectMapper = new ObjectMapper();
  }

  /**
   * Retrieves PaperMC server {@link Version version} related to given Minecraft version.
   *
   * @param baseUrl   the base URL to use to fetch PaperMC API
   * @param mcVersion the Minecraft version to use
   * @return the PaperMC server {@link Version version}
   * @throws MojoExecutionException when issue occurred while fetch PaperMC API
   */
  public Version retrieveVersion(final String baseUrl, final String mcVersion) throws MojoExecutionException {
    // Call Paper API
    final Request request = new Request.Builder()
        .url(baseUrl + "versions/" + mcVersion)
        .build();
    try (final Response response = client.newCall(request).execute()) {
      final int status = response.code();
      if (HTTP_OK != status) {
        throw new MojoExecutionException(
            "Unable to call PaperMC API status: " + status + ", body " + Objects.requireNonNull(response.body())
                .string());
      }
      final ResponseBody responseBody = response.body();
      if (null == responseBody) {
        throw new MojoExecutionException("Unable to fetch PaperMC build number");
      }

      return objectMapper.readValue(responseBody.string(), Version.class);
    } catch (final IOException e) {
      throw new MojoExecutionException("Unable to fetch PaperMC build number", e);
    }
  }

  /**
   * Retrieves PaperMC {@link Build build} based on given Minecraft version and given build number.
   *
   * @param baseUrl    the base URL to use to fetch PaperMC API
   * @param mcVersion  the Minecraft version to use
   * @param paperBuild the build number to use
   * @return the PaperMC {@link Build build}
   * @throws MojoExecutionException when issue occurred while fetch PaperMC API
   */
  public Build retrieveBuild(final String baseUrl, final String mcVersion, final String paperBuild)
      throws MojoExecutionException {
    // Gets URL to download JAR
    final Request request = new Request.Builder()
        .url(baseUrl + "versions/" + mcVersion + "/builds/" + paperBuild)
        .build();
    try (final Response response = client.newCall(request).execute()) {
      final int status = response.code();
      if (HTTP_OK != status) {
        throw new MojoExecutionException(
            "Unable to call PaperMC API status: " + status + ", body " + Objects.requireNonNull(response.body())
                .string());
      }
      final ResponseBody responseBody = response.body();
      if (null == responseBody) {
        throw new MojoExecutionException("Unable to fetch PaperMC download URL");
      }

      return objectMapper.readValue(responseBody.string(), Build.class);
    } catch (final IOException e) {
      throw new MojoExecutionException("Unable to fetch PaperMC download URL", e);
    }
  }
}
