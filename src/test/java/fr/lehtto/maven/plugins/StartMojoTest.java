package fr.lehtto.maven.plugins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import org.apache.maven.plugin.MojoExecutionException;
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
 * Tests for {@link StartMojo}.
 *
 * @author Lehtto
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings("PackageVisibleField")
@DisplayName("MOJO start")
class StartMojoTest {

  @InjectMocks
  @Spy
  StartMojo startMojo;

  private static final int memoryMin = 5;
  private static final int memoryMax = 10;
  @Mock
  File serverDirectory;
  @Mock
  private Log log;
  @Mock
  private Map pluginContext;

  /**
   * Setups tests.
   * @throws IllegalAccessException when issue occurred
   * @throws NoSuchFieldException when issue occurred
   */
  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    MockitoAnnotations.openMocks(this);

    // Set memoryMin
    final Field memoryMinField = startMojo.getClass().getDeclaredField("memoryMin");
    memoryMinField.setAccessible(true);
    memoryMinField.set(startMojo, memoryMin);

    // Set memoryMax
    final Field memoryMaxField = startMojo.getClass().getDeclaredField("memoryMax");
    memoryMaxField.setAccessible(true);
    memoryMaxField.set(startMojo, memoryMax);
  }

  /**
   * Test of {@link StartMojo#createProcessBuilder()}.
   */
  @DisplayName("Create process builder")
  @Test
  void testCreateProcessBuilder() {
    // CALL
    final ProcessBuilder result = startMojo.createProcessBuilder();

    // ASSERT
    //noinspection StringConcatenationMissingWhitespace
    assertThat(result.command())
        .containsExactly(
            "java",
            "-Xms" + memoryMin + 'G',
            "-Xmx" + memoryMax + 'G',
            "-jar",
            "server.jar",
            "--nogui"
        );
  }

  /**
   * Tests of {@link StartMojo#execute()}.
   */
  @DisplayName("Execute")
  @Nested
  class ExecuteTests {

    /**
     * Test nominal case.
     *
     * @throws MojoExecutionException when issue occurred
     * @throws IOException            when issue occurred
     * @throws InterruptedException   when issue occurred
     */
    @DisplayName("Nominal case")
    @Test
    void testNominalCase() throws IOException, InterruptedException, MojoExecutionException {
      // MOCKS
      final ProcessBuilder processBuilder = mock(ProcessBuilder.class);
      final Process process = mock(Process.class);

      // STUBBING
      doReturn(processBuilder).when(startMojo).createProcessBuilder();
      doReturn(processBuilder).when(processBuilder).directory(serverDirectory);
      doReturn(processBuilder).when(processBuilder).inheritIO();
      doReturn(process).when(processBuilder).start();
      doReturn(0/* ignored */).when(process).waitFor();

      // CALL
      startMojo.execute();

      // VERIFY
      verify(startMojo).createProcessBuilder();
      verify(processBuilder).directory(serverDirectory);
      verify(processBuilder).inheritIO();
      verify(processBuilder).start();
      verify(process).waitFor();
    }

    /**
     * Test {@link IOException} is thrown while starting process.
     *
     * @throws IOException when issue occurred
     */
    @DisplayName("IOException")
    @Test
    void testIoException() throws IOException {

      // MOCKS
      final ProcessBuilder processBuilder = mock(ProcessBuilder.class);
      final Process process = mock(Process.class);

      // STUBBING
      doReturn(processBuilder).when(startMojo).createProcessBuilder();
      doReturn(processBuilder).when(processBuilder).directory(serverDirectory);
      doReturn(processBuilder).when(processBuilder).inheritIO();
      doThrow(IOException.class).when(processBuilder).start();

      // CALL
      assertThatExceptionOfType(MojoExecutionException.class)
          .isThrownBy(() -> startMojo.execute())
          .withMessage("Unable to start PaperMC server")
          .withCauseInstanceOf(IOException.class);

      // VERIFY
      verify(startMojo).createProcessBuilder();
      verify(processBuilder).directory(serverDirectory);
      verify(processBuilder).inheritIO();
      verify(processBuilder).start();
    }

    /**
     * Test {@link InterruptedException} is thrown while wait for.
     *
     * @throws IOException          when issue occurred
     * @throws InterruptedException when issue occurred
     */
    @DisplayName("IOException")
    @Test
    void testInterruptedException() throws InterruptedException, IOException {
      // MOCKS
      final ProcessBuilder processBuilder = mock(ProcessBuilder.class);
      final Process process = mock(Process.class);
      final Thread thread = mock(Thread.class);

      // STUBBING
      doReturn(processBuilder).when(startMojo).createProcessBuilder();
      doReturn(processBuilder).when(processBuilder).directory(serverDirectory);
      doReturn(processBuilder).when(processBuilder).inheritIO();
      doReturn(process).when(processBuilder).start();
      doThrow(InterruptedException.class).when(process).waitFor();

      // CALL
      assertThatExceptionOfType(MojoExecutionException.class)
          .isThrownBy(() -> startMojo.execute())
          .withMessage("An issue occurred.")
          .withCauseInstanceOf(InterruptedException.class);

      // VERIFY
      verify(startMojo).createProcessBuilder();
      verify(processBuilder).directory(serverDirectory);
      verify(processBuilder).inheritIO();
      verify(processBuilder).start();

      assertThat(Thread.interrupted()).isTrue();
    }
  }
}

//Generated with love by TestMe :)
// Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme