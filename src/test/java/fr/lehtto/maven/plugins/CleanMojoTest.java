package fr.lehtto.maven.plugins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Predicate;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 * Tests for {@link CleanMojo}.
 *
 * @author Lehtto
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings("PackageVisibleField")
@DisplayName("MOJO clean")
class CleanMojoTest {

  @Mock
  private File serverDirectory;
  @Mock
  private Log log;
  @Mock
  private Map pluginContext;

  @InjectMocks
  @Spy
  CleanMojo cleanMojo;

  /**
   * Setups tests.
   */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  /**
   * Tests for {@link CleanMojo#execute()}.
   *
   * @throws MojoExecutionException when issue occurred
   */
  @DisplayName("Execute")
  @Test
  void testExecute() throws MojoExecutionException {
    // MOCKS
    final String dummyFileName = "DUMMY_FILE_NAME";

    // STUBBING
    doReturn(dummyFileName).when(serverDirectory).getName();
    doReturn(true).when(cleanMojo).deleteDir(eq(serverDirectory), any());

    // CALL
    cleanMojo.execute();

    // VERIFY
    verify(cleanMojo).deleteDir(eq(serverDirectory), any());
  }

  /**
   * Tests for {@link CleanMojo#deleteDir(File, Predicate)}.
   */
  @DisplayName("Delete dir")
  @Nested
  class DeleteDirTests {

    /**
     * Child deleted.
     *
     * @throws MojoExecutionException when issue occurred
     */
    @DisplayName("Child deleted")
    @Test
    void testChildDeleted() throws MojoExecutionException {
      try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
        // INPUT
        final File file = mock(File.class);
        final Predicate<File> predicate = mock(Predicate.class);

        // MOCKS
        final File[] content = new File[]{mock(File.class)};
        final String dummyAbsolutePath = "DUMMY_ABSOLUTE_PATH";
        final Path path = mock(Path.class);

        // STUBBING
        doReturn(true).when(file).exists();
        doReturn(content).when(file).listFiles();
        doReturn(true).when(cleanMojo).deleteDir(content[0], predicate);
        doReturn(dummyAbsolutePath).when(file).getAbsolutePath();
        doReturn(path).when(file).toPath();
        filesMockedStatic.when(() -> Files.deleteIfExists(path)).thenReturn(true);
        doReturn(true).when(predicate).test(file);
        doReturn(true).when(predicate).test(content[0]);

        // CALL
        cleanMojo.deleteDir(file, predicate);

        // VERIFY
        verify(file).exists();
        verify(file).listFiles();
        verify(cleanMojo).deleteDir(content[0], predicate);
        verify(predicate).test(file);
        filesMockedStatic.verify(() -> Files.deleteIfExists(path));
      }
    }

    /**
     * Child not deleted.
     *
     * @throws MojoExecutionException when issue occurred
     */
    @DisplayName("Child not deleted")
    @Test
    void testChildNotDeleted() throws MojoExecutionException {
      try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
        // INPUT
        final File file = mock(File.class);
        final Predicate<File> predicate = mock(Predicate.class);

        // MOCKS
        final File[] content = new File[]{mock(File.class)};
        final String dummyAbsolutePath = "DUMMY_ABSOLUTE_PATH";
        final Path path = mock(Path.class);

        // STUBBING
        doReturn(true).when(predicate).test(file);
        doReturn(true).when(file).exists();
        doReturn(content).when(file).listFiles();
        doReturn(false).when(cleanMojo).deleteDir(content[0], predicate);
        doReturn(dummyAbsolutePath).when(file).getAbsolutePath();
        doReturn(path).when(file).toPath();
        doReturn(true).when(predicate).test(content[0]);

        // CALL
        cleanMojo.deleteDir(file, predicate);

        // VERIFY
        verify(predicate).test(file);
        verify(file).exists();
        verify(file).listFiles();
        verify(cleanMojo).deleteDir(content[0], predicate);
        filesMockedStatic.verify(() -> Files.deleteIfExists(path), never());
      }
    }
  }

  /**
   * Tests for {@link CleanMojo#isWorldDir(File)}.
   */
  @DisplayName("Is world dir")
  @Nested
  class IsWorldDirTests {

    /**
     * World dir.
     *
     * @param pathName the directory pathname
     */
    @DisplayName("World dir")
    @ParameterizedTest
    @ValueSource(strings = {"world", "world_nether", "world_the_end"})
    void IsWorldDir(final String pathName) {
      // INPUTS
      final File file = new File(pathName);

      // CALL
      final boolean result = cleanMojo.isWorldDir(file);

      // ASSERT
      assertThat(result).isTrue();
    }

    /**
     * Not world dir.
     */
    @DisplayName("Not world dir")
    @Test
    void IsNotWorldDir() {
      // INPUTS
      final File file = new File("pathName");

      // CALL
      final boolean result = cleanMojo.isWorldDir(file);

      // ASSERT
      assertThat(result).isFalse();
    }
  }
}

//Generated with love by TestMe :)
// Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme