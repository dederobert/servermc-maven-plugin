package fr.lehtto.maven.plugins;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import fr.lehtto.maven.plugins.papermc.PaperApiClient;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 * Tests for {@link InstallMojo}.
 *
 * @author Lehtto
 * @version 0.1.0
 * @since 0.1.0
 */
@SuppressWarnings("PackageVisibleField")
@DisplayName("Install MOJO")
class InstallMojoTest {

  @Mock
  PaperApiClient client;
  @Mock
  Map<String, String> properties;
  @Mock
  File eulaFile;
  @Mock
  File jarFile;
  @Mock
  File propertiesFile;
  private File serverDirectory;
  @Mock
  Log log;
  @Mock
  Map pluginContext;
  @InjectMocks
  @Spy
  InstallMojo installMojo;

  /**
   * Setups the tests.
   *
   * @throws IllegalAccessException when issue occurred
   * @throws NoSuchFieldException   when issue occurred
   */
  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    MockitoAnnotations.openMocks(this);
    serverDirectory = spy(new File("parentPath"));

    final Field serverDirectoryField = installMojo.getClass().getSuperclass().getDeclaredField("serverDirectory");
    serverDirectoryField.setAccessible(true);
    serverDirectoryField.set(installMojo, serverDirectory);
  }

  /**
   * Test for {@link InstallMojo#execute()}.
   */
  @DisplayName("Execute")
  @Nested
  class Execute {

    /**
     * No EULA.
     *
     * @throws IllegalAccessException when issue occurred
     * @throws NoSuchFieldException   when issue occurred
     */
    @DisplayName("No EULA")
    @Test
    void withoutEula() throws NoSuchFieldException, IllegalAccessException {
      // STUBBING
      final Field eula = installMojo.getClass().getDeclaredField("eula");
      eula.setAccessible(true);
      eula.set(installMojo, false);

      // CALL
      assertThatExceptionOfType(MojoFailureException.class).isThrownBy(() -> installMojo.execute()).withMessage(
          "Please read EULA from https://aka.ms/MinecraftEULA and "
              + "indicate your agreement by using parameter eula");
    }

    /**
     * No EULA.
     *
     * @throws MojoExecutionException when an issue occurred
     * @throws MojoFailureException   when an issue occurred
     * @throws IllegalAccessException when issue occurred
     * @throws NoSuchFieldException   when issue occurred
     */
    @DisplayName("Nominal case")
    @Test
    void nominalCase()
        throws NoSuchFieldException, IllegalAccessException, MojoExecutionException, MojoFailureException {
      // STUBBING
      final Field eula = installMojo.getClass().getDeclaredField("eula");
      eula.setAccessible(true);
      eula.set(installMojo, true);
      doReturn(false).when(serverDirectory).exists();
      doReturn(true /* ignored */).when(serverDirectory).mkdirs();
      doNothing().when(installMojo).createEulaFile();
      doNothing().when(installMojo).retrievePaperBuildNumber();
      doNothing().when(installMojo).retrieveJarUrl();
      doNothing().when(installMojo).downloadPaper();
      doNothing().when(installMojo).createPropertiesFile();

      // CALL
      installMojo.execute();

      // VERIFY
      final InOrder inOrder = inOrder(installMojo);
      verify(serverDirectory).exists();
      verify(serverDirectory).mkdirs();
      verify(installMojo).createEulaFile();
      inOrder.verify(installMojo).retrievePaperBuildNumber();
      inOrder.verify(installMojo).retrieveJarUrl();
      inOrder.verify(installMojo).downloadPaper();
      verify(installMojo).createPropertiesFile();
    }
  }

  /**
   * Tests for {@link InstallMojo#retrievePaperBuildNumber()}.
   */
  @DisplayName("Retrieve paper build number")
  @Nested
  class RetrievePaperBuildNumber {

    /**
     * With build number.
     *
     * @throws NoSuchFieldException   when an issue occurred
     * @throws IllegalAccessException when an issue occurred
     * @throws MojoExecutionException when an issue occurred
     */
    @DisplayName("With build number")
    @Test
    void withBuildNumber() throws NoSuchFieldException, IllegalAccessException, MojoExecutionException {
      // STUBBING
      final Field buildNumberField = installMojo.getClass().getDeclaredField("buildNumber");
      buildNumberField.setAccessible(true);
      buildNumberField.set(installMojo, "152");

      // CALL
      installMojo.retrievePaperBuildNumber();

      // VERIFY
      verify(client, never()).retrieveVersion(anyString(), anyString());
    }
  }
}

//Generated with love by TestMe :)
// Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme