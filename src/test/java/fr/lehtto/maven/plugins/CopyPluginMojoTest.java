package fr.lehtto.maven.plugins;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 * Tests for {@link CopyPluginMojo}.
 *
 * @author Lehtto
 * @version 0.0.1
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
  private List<Plugin> additionalPlugins;
  @Mock
  private Log log;
  @Mock
  private Map pluginContext;

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
}

//Generated with love by TestMe :)
// Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme