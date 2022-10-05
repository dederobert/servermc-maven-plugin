package fr.lehtto.maven.plugins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import fr.lehtto.maven.plugins.util.FileDownloader;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;

/**
 * Tests for {@link CopyPluginMojo}.
 *
 * @author Lehtto
 * @version 0.1.0
 * @since 0.0.1
 */
@SuppressWarnings("PackageVisibleField")
@DisplayName("MOJO copy plugins")
class CopyPluginMojoTest {

  private static final String jarName = "DUMMY_JAR_NAME";
  @InjectMocks
  @Spy
  CopyPluginMojo copyPluginMojo;
  @Mock
  File buildFolder;
  @Mock
  File serverDirectory;
  @Mock
  List<Plugin> additionalPlugins;
  @Mock
  private Log log;
  @Mock
  private Map pluginContext;

  @Captor
  ArgumentCaptor<File> fileArgumentCaptor;

  /**
   * Setups tests.
   *
   * @throws NoSuchFieldException   when issue occurred
   * @throws IllegalAccessException when issue occurred
   */
  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    MockitoAnnotations.openMocks(this);
    final Field jarNameField = copyPluginMojo.getClass().getDeclaredField("jarName");
    jarNameField.setAccessible(true);
    jarNameField.set(copyPluginMojo, jarName);
  }

  /**
   * Tests for {@link CopyPluginMojo#execute()}.
   */
  @DisplayName("Execute")
  @Nested
  class ExecuteTests {

    /**
     * Directory creation success.
     *
     * @throws MojoExecutionException when an issue occurred
     * @throws MojoFailureException   when an issue occurred
     */
    @DisplayName("Server directory creation success")
    @Test
    void testCreationSuccess() throws MojoExecutionException, MojoFailureException {

      // STUBBING
      doReturn(false).when(serverDirectory).exists();
      doReturn(true).when(serverDirectory).mkdirs();
      doNothing().when(copyPluginMojo).copyPlugin();
      doNothing().when(copyPluginMojo).copyAdditionalPlugin();

      // CALL
      copyPluginMojo.execute();

      // VERIFY
      verify(serverDirectory).exists();
      verify(serverDirectory).mkdirs();
    }

    /**
     * Directory creation failed.
     */
    @DisplayName("Server directory creation failed")
    @Test
    void testCreationFailed() {

      // STUBBING
      doReturn(false).when(serverDirectory).exists();
      doReturn(false).when(serverDirectory).mkdirs();

      // CALL
      assertThatExceptionOfType(MojoExecutionException.class)
          .isThrownBy(() -> copyPluginMojo.execute())
          .withMessage("Unable to create server directory");

      // VERIFY
      verify(serverDirectory).exists();
      verify(serverDirectory).mkdirs();
    }

  }

  /**
   * Tests for {@link CopyPluginMojo#copyPlugin()}.
   */
  @DisplayName("Copy plugin")
  @Nested
  class CopyPluginTests {

    /**
     * Creation of plugin directory is successful.
     *
     * @throws MojoExecutionException when issue occurred
     */
    @DisplayName("Plugin directory creation success")
    @Test
    void testCreationSuccess() throws MojoExecutionException {

      // MOCKS
      final File pluginsFolder = mock(File.class);
      final File sourcePlugin = mock(File.class);
      final File destinationPluginJar = mock(File.class);

      // STUBBING
      doReturn(pluginsFolder).when(copyPluginMojo).createFile(serverDirectory, "plugins");
      doReturn(sourcePlugin).when(copyPluginMojo).createFile(buildFolder, jarName + ".jar");
      doReturn(destinationPluginJar).when(copyPluginMojo).createFile(pluginsFolder, jarName + ".jar");
      doReturn(false).when(pluginsFolder).exists();
      doReturn(true).when(pluginsFolder).mkdirs();
      doNothing().when(copyPluginMojo).copyFile(sourcePlugin, destinationPluginJar);

      // CALL
      copyPluginMojo.copyPlugin();

      // VERIFY
      verify(copyPluginMojo).createFile(serverDirectory, "plugins");
      verify(copyPluginMojo).createFile(buildFolder, jarName + ".jar");
      verify(copyPluginMojo).createFile(pluginsFolder, jarName + ".jar");
      verify(pluginsFolder).exists();
      verify(pluginsFolder).mkdirs();
      verify(copyPluginMojo).copyFile(sourcePlugin, destinationPluginJar);
    }

    /**
     * Creation of plugin directory failed.
     */
    @DisplayName("Plugin directory creation failed")
    @Test
    void testCreationFailed() {

      // MOCKS
      final File pluginsFolder = mock(File.class);
      final File sourcePlugin = mock(File.class);
      final File destinationPluginJar = mock(File.class);

      // STUBBING
      doReturn(pluginsFolder).when(copyPluginMojo).createFile(serverDirectory, "plugins");
      doReturn(sourcePlugin).when(copyPluginMojo).createFile(buildFolder, jarName + ".jar");
      doReturn(destinationPluginJar).when(copyPluginMojo).createFile(pluginsFolder, jarName + ".jar");
      doReturn(false).when(pluginsFolder).exists();
      doReturn(false).when(pluginsFolder).mkdirs();

      // CALL
      assertThatExceptionOfType(MojoExecutionException.class)
          .isThrownBy(() -> copyPluginMojo.copyPlugin())
          .withMessage("Unable to create plugins directory");

      // VERIFY
      verify(copyPluginMojo).createFile(serverDirectory, "plugins");
      verify(copyPluginMojo).createFile(buildFolder, jarName + ".jar");
      verify(copyPluginMojo).createFile(pluginsFolder, jarName + ".jar");
      verify(pluginsFolder).exists();
      verify(pluginsFolder).mkdirs();
    }
  }

  /**
   * Tests for {@link CopyPluginMojo#copyAdditionalPlugin()}.
   */
  @DisplayName("Copy additional plugins")
  @Nested
  class CopyAdditionalPluginTests {

    /**
     * With file path.
     *
     * @throws MojoExecutionException when an issue occurred
     * @throws MojoFailureException   when an issue occurred
     */
    @DisplayName("With file path")
    @Test
    void withFilePath() throws MojoExecutionException, MojoFailureException {
      // MOCKS
      final List<Plugin> plugins = Collections.singletonList(mock(Plugin.class));
      final File pluginFile = mock(File.class);
      final File file = new File("");
      final String pluginName = "pluginName";

      // STUBBING
      doReturn(file).when(copyPluginMojo).getServerDirectory();
      doReturn(plugins.iterator()).when(additionalPlugins).iterator();
      doReturn(pluginFile).when(plugins.get(0)).getFile();
      doNothing().when(copyPluginMojo).copyFile(eq(pluginFile), any());
      doReturn(pluginName).when(plugins.get(0)).getName();

      // CALL
      copyPluginMojo.copyAdditionalPlugin();

      // VERIFY
      verify(additionalPlugins).iterator();
      verify(plugins.get(0), times(3)).getFile();
      verify(copyPluginMojo).copyFile(eq(pluginFile), fileArgumentCaptor.capture());

      // ASSERT
      assertThat(fileArgumentCaptor.getValue())
          .hasFileName(pluginName + ".jar")
          .hasParent("/plugins");
    }

    /**
     * With url.
     *
     * @throws MojoExecutionException when an issue occurred
     * @throws MojoFailureException   when an issue occurred
     */
    @DisplayName("With url")
    @Test
    void withUrl() throws MojoExecutionException, MojoFailureException {
      try (final MockedStatic<FileDownloader> fileDownloaderMockedStatic = mockStatic(FileDownloader.class)) {
        // MOCKS
        final List<Plugin> plugins = Collections.singletonList(mock(Plugin.class));
        final File pluginFile = mock(File.class);
        final File file = new File("");
        final String pluginName = "pluginName";
        final URL url = mock(URL.class);

        // STUBBING
        doReturn(file).when(copyPluginMojo).getServerDirectory();
        doReturn(plugins.iterator()).when(additionalPlugins).iterator();
        doReturn(null).when(plugins.get(0)).getFile();
        doReturn(url).when(plugins.get(0)).getUrl();
        fileDownloaderMockedStatic.when(() -> FileDownloader.downloadFile(any(), eq(url)))
            .thenAnswer((Answer<Void>) invocationOnMock -> null);
        doReturn(pluginName).when(plugins.get(0)).getName();

        // CALL
        copyPluginMojo.copyAdditionalPlugin();

        // VERIFY
        verify(additionalPlugins).iterator();
        verify(plugins.get(0)).getFile();
        fileDownloaderMockedStatic.verify(() -> FileDownloader.downloadFile(fileArgumentCaptor.capture(), eq(url)));

        // ASSERT
        assertThat(fileArgumentCaptor.getValue())
            .hasFileName(pluginName + ".jar")
            .hasParent("/plugins");
      }
    }

    /**
     * With url - invalid SHA256.
     */
    @DisplayName("With url - invalid SHA256")
    @Test
    void withUrlInvalidSha256() {
      try (final MockedStatic<FileDownloader> fileDownloaderMockedStatic = mockStatic(FileDownloader.class)) {
        // MOCKS
        final List<Plugin> plugins = Collections.singletonList(mock(Plugin.class));
        final File pluginFile = mock(File.class);
        final File file = new File("");
        final String pluginName = "pluginName";
        final URL url = mock(URL.class);
        final String sha256 = "SHA256";

        // STUBBING
        doReturn(file).when(copyPluginMojo).getServerDirectory();
        doReturn(plugins.iterator()).when(additionalPlugins).iterator();
        doReturn(null).when(plugins.get(0)).getFile();
        doReturn(url).when(plugins.get(0)).getUrl();
        fileDownloaderMockedStatic.when(() -> FileDownloader.downloadFile(any(), eq(url)))
            .thenAnswer((Answer<Void>) invocationOnMock -> null);
        doReturn(pluginName).when(plugins.get(0)).getName();
        doReturn(sha256).when(plugins.get(0)).getSha256();
        fileDownloaderMockedStatic.when(() -> FileDownloader.checkSha256Checksum(any(), eq(sha256))).thenReturn(false);

        // CALL
        assertThatExceptionOfType(MojoFailureException.class)
            .isThrownBy(() -> copyPluginMojo.copyAdditionalPlugin())
            .withMessage("Plugin %s checksum is not valid", pluginName);

        // VERIFY
        verify(additionalPlugins).iterator();
        verify(plugins.get(0)).getFile();
        fileDownloaderMockedStatic.verify(() -> FileDownloader.downloadFile(fileArgumentCaptor.capture(), eq(url)));
        fileDownloaderMockedStatic.verify(
            () -> FileDownloader.checkSha256Checksum(fileArgumentCaptor.getValue(), sha256));

        // ASSERT
        assertThat(fileArgumentCaptor.getValue())
            .hasFileName(pluginName + ".jar")
            .hasParent("/plugins");
      }
    }

    /**
     * With url - invalid MD5.
     */
    @DisplayName("With url - invalid MD5")
    @Test
    void withUrlInvalidMd5() {
      try (final MockedStatic<FileDownloader> fileDownloaderMockedStatic = mockStatic(FileDownloader.class)) {
        // MOCKS
        final List<Plugin> plugins = Collections.singletonList(mock(Plugin.class));
        final File pluginFile = mock(File.class);
        final File file = new File("");
        final String pluginName = "pluginName";
        final URL url = mock(URL.class);
        final String md5 = "md5";

        // STUBBING
        doReturn(file).when(copyPluginMojo).getServerDirectory();
        doReturn(plugins.iterator()).when(additionalPlugins).iterator();
        doReturn(null).when(plugins.get(0)).getFile();
        doReturn(url).when(plugins.get(0)).getUrl();
        fileDownloaderMockedStatic.when(() -> FileDownloader.downloadFile(any(), eq(url)))
            .thenAnswer((Answer<Void>) invocationOnMock -> null);
        doReturn(pluginName).when(plugins.get(0)).getName();
        doReturn(md5).when(plugins.get(0)).getMd5();
        fileDownloaderMockedStatic.when(() -> FileDownloader.checkMd5Checksum(any(), eq(md5))).thenReturn(false);

        // CALL
        assertThatExceptionOfType(MojoFailureException.class)
            .isThrownBy(() -> copyPluginMojo.copyAdditionalPlugin())
            .withMessage("Plugin %s checksum is not valid", pluginName);

        // VERIFY
        verify(copyPluginMojo).getServerDirectory();
        verify(additionalPlugins).iterator();
        verify(plugins.get(0)).getFile();
        verify(plugins.get(0), times(3)).getUrl();
        fileDownloaderMockedStatic.verify(() -> FileDownloader.downloadFile(fileArgumentCaptor.capture(), eq(url)));
        verify(plugins.get(0), times(3)).getName();
        verify(plugins.get(0), times(3)).getMd5();
        fileDownloaderMockedStatic.verify(
            () -> FileDownloader.checkMd5Checksum(fileArgumentCaptor.getValue(), md5));

        // ASSERT
        assertThat(fileArgumentCaptor.getValue())
            .hasFileName(pluginName + ".jar")
            .hasParent("/plugins");
      }
    }

    /**
     * With groupId and artifactId - Found.
     *
     * @throws MojoExecutionException when an issue occurred
     * @throws MojoFailureException   when an issue occurred
     */
    @DisplayName("With groupId & artifactId - Found")
    @Test
    void withGroupIdAndArtifactIdFound() throws MojoExecutionException, MojoFailureException {
      // MOCKS
      final List<Plugin> plugins = Collections.singletonList(mock(Plugin.class));
      final File pluginFile = mock(File.class);
      final File file = new File("");
      final String pluginName = "pluginName";
      final String groupId = "groupId";
      final String artifactId = "artifactId";
      final Artifact artifact = mock(Artifact.class);
      final File artifactFile = mock(File.class);

      // STUBBING
      doReturn(file).when(copyPluginMojo).getServerDirectory();
      doReturn(plugins.iterator()).when(additionalPlugins).iterator();
      doReturn(null).when(plugins.get(0)).getFile();
      doReturn(null).when(plugins.get(0)).getUrl();
      doReturn(groupId).when(plugins.get(0)).getGroupId();
      doReturn(artifactId).when(plugins.get(0)).getArtifactId();
      doReturn(Optional.of(artifact)).when(copyPluginMojo).searchArtifact(groupId, artifactId);
      doReturn(pluginName).when(plugins.get(0)).getName();
      doReturn(artifactFile).when(artifact).getFile();
      doReturn(pluginName).when(artifact).getArtifactId();
      doNothing().when(copyPluginMojo).copyFile(eq(artifactFile), any());

      // CALL
      copyPluginMojo.copyAdditionalPlugin();

      // VERIFY
      verify(copyPluginMojo).getServerDirectory();
      verify(additionalPlugins).iterator();
      verify(plugins.get(0)).getFile();
      verify(plugins.get(0)).getUrl();
      verify(plugins.get(0), times(2)).getGroupId();
      verify(plugins.get(0), times(2)).getArtifactId();
      verify(copyPluginMojo).searchArtifact(groupId, artifactId);
      verify(plugins.get(0), times(2)).getName();
      verify(artifact).getFile();
      verify(copyPluginMojo).copyFile(eq(artifactFile), fileArgumentCaptor.capture());

      // ASSERT
      assertThat(fileArgumentCaptor.getValue())
          .hasParent("/plugins")
          .hasFileName(pluginName + ".jar");
    }

    /**
     * With groupId and artifactId - Not found.
     *
     * @throws MojoFailureException when an issue occurred
     */
    @DisplayName("With groupId & artifactId - Not found")
    @Test
    void withGroupIdAndArtifactIdNotFound() throws MojoFailureException {
      // MOCKS
      final List<Plugin> plugins = Collections.singletonList(mock(Plugin.class));
      final File pluginFile = mock(File.class);
      final File file = new File("");
      final String pluginName = "pluginName";
      final String groupId = "groupId";
      final String artifactId = "artifactId";

      // STUBBING
      doReturn(file).when(copyPluginMojo).getServerDirectory();
      doReturn(plugins.iterator()).when(additionalPlugins).iterator();
      doReturn(null).when(plugins.get(0)).getFile();
      doReturn(null).when(plugins.get(0)).getUrl();
      doReturn(groupId).when(plugins.get(0)).getGroupId();
      doReturn(artifactId).when(plugins.get(0)).getArtifactId();
      doReturn(Optional.empty()).when(copyPluginMojo).searchArtifact(groupId, artifactId);
      doReturn(pluginName).when(plugins.get(0)).getName();

      // CALL
      assertThatExceptionOfType(MojoFailureException.class)
          .isThrownBy(() -> copyPluginMojo.copyAdditionalPlugin())
          .withMessage("Unable to found plugin %s in dependencies with groupId %s and artifactId %s", pluginName,
              groupId, artifactId);
    }
  }

  /**
   * Test for {@link CopyPluginMojo#createFile(File, String)}.
   */
  @DisplayName("Create file")
  @Test
  void testCreateFile() {
    // INPUTS
    final File parent = new File("parentPath");
    final String path = "path";

    // CALL
    final File result = copyPluginMojo.createFile(parent, path);

    // ASSERT
    assertThat(result).hasParent(parent).hasFileName(path);
  }
}

//Generated with love by TestMe :)
// Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme