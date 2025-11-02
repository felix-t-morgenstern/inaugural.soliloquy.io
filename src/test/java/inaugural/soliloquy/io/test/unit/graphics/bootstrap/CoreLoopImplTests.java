package inaugural.soliloquy.io.test.unit.graphics.bootstrap;

import inaugural.soliloquy.io.bootstrap.CoreLoopImpl;
import inaugural.soliloquy.io.mouse.MouseListener;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeFrameTimer;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeGraphicsPreloader;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import soliloquy.specs.io.bootstrap.CoreLoop;
import soliloquy.specs.io.bootstrap.assetfactories.AudioLoader;
import soliloquy.specs.io.graphics.rendering.FrameExecutor;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.factories.ShaderFactory;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;
import soliloquy.specs.io.input.keyboard.KeyEventListener;
import soliloquy.specs.io.input.mouse.MouseCursor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CoreLoopImplTests {
    private final String TITLEBAR = randomString();
    private final FakeFrameTimer FRAME_TIMER = new FakeFrameTimer();
    private final int FRAME_TIMER_POLLING_INTERVAL = 20;
    private final long GLOBAL_TIMESTAMP = randomLong();
    private final String SHADER_FILE_PREFIX = "shaderFilePrefix";
    @SuppressWarnings("rawtypes") private final Renderer MOCK_RENDERER = mock(Renderer.class);
    @SuppressWarnings("rawtypes")
    private final Set<Renderer> RENDERERS_WITH_SHADER = setOf(MOCK_RENDERER);
    @SuppressWarnings("rawtypes")
    private final Set<Renderer> RENDERERS_WITH_MESH = setOf(MOCK_RENDERER);
    private final float[] MESH_VERTICES = new float[]{0.123f};
    private final float[] MESH_UV_COORDINATES = new float[]{0.456f};
    private final FakeGraphicsPreloader GRAPHICS_PRELOADER = new FakeGraphicsPreloader();
    private final Set<String> AUDIO_REL_DIRS = setOf(randomString());
    private final Map<String, String> IDS_FOR_FILENAMES = mapOf();
    private final Map<String, Integer> DEFAULT_LOOP_STOP_MS_BY_ID = mapOf();
    private final Map<String, Integer> DEFAULT_LOOP_RESTART_MS_BY_ID = mapOf();

    private BiFunction<float[], float[], Mesh> meshFactory;
    @Mock private Shader mockShader;
    @Mock private ShaderFactory mockShaderFactory;
    @Mock private Mesh mockMesh;
    @Mock private GlobalClock mockGlobalClock;
    @Mock private FrameExecutor mockFrameExecutor;
    @Mock private WindowResolutionManager mockWindowResolutionManager;
    @Mock private AudioLoader mockAudioLoader;
    @Mock private KeyEventListener mockKeyEventListener;
    @Mock private MouseCursor mockMouseCursor;
    @Mock private MouseListener mockMouseListener;

    private Long windowId;

    private CoreLoop coreLoop;

    @BeforeEach
    public void setUp() {
        lenient().when(mockShaderFactory.make(anyString())).thenReturn(mockShader);

        meshFactory = (_, _) -> mockMesh;

        mockGlobalClock = mock(GlobalClock.class);
        lenient().when(mockGlobalClock.globalTimestamp()).thenReturn(GLOBAL_TIMESTAMP);

        mockFrameExecutor = mock(FrameExecutor.class);

        mockWindowResolutionManager = mock(WindowResolutionManager.class);
        lenient().when(
                        mockWindowResolutionManager.updateWindowSizeAndLocation(anyLong(),
                                anyString()))
                .thenAnswer((Answer<Long>) _ -> {
                    if (windowId != null) {
                        return windowId;
                    }
                    long newWindowId = glfwCreateWindow(800, 600, "My titlebar", 0, 0);
                    glfwMakeContextCurrent(newWindowId);
                    return windowId = newWindowId;
                });

        mockMouseListener = mock(MouseListener.class);

        coreLoop = new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        );
    }

    @Test
    public void testInvalidConstructorParams() {
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                null,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                "",
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                null,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                1000,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                null,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                null,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                null,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                null,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                null,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                null,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                "",
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                null,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                null,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                null,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                null,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                null,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                null,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                null,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                null,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                null,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                null,
                mockKeyEventListener,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                null,
                mockMouseCursor,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                null,
                mockMouseListener
        ));
        assertThrows(IllegalArgumentException.class, () -> new CoreLoopImpl(
                TITLEBAR,
                FRAME_TIMER,
                FRAME_TIMER_POLLING_INTERVAL,
                mockWindowResolutionManager,
                mockGlobalClock,
                mockFrameExecutor,
                mockShaderFactory,
                RENDERERS_WITH_SHADER,
                SHADER_FILE_PREFIX,
                meshFactory,
                RENDERERS_WITH_MESH,
                MESH_VERTICES,
                MESH_UV_COORDINATES,
                GRAPHICS_PRELOADER,
                mockAudioLoader,
                AUDIO_REL_DIRS,
                IDS_FOR_FILENAMES,
                DEFAULT_LOOP_STOP_MS_BY_ID,
                DEFAULT_LOOP_RESTART_MS_BY_ID,
                mockKeyEventListener,
                mockMouseCursor,
                null
        ));
    }

    @Test
    public void testGetTitlebar() {
        assertEquals(TITLEBAR, coreLoop.getTitlebar());
    }

    @Test
    public void testSetTitlebar() {
        var newTitlebar = randomString();

        coreLoop.setTitlebar(newTitlebar);

        assertEquals(newTitlebar, coreLoop.getTitlebar());
    }

    @Test
    public void testSetTitlebarWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> coreLoop.setTitlebar(null));
        assertThrows(IllegalArgumentException.class, () -> coreLoop.setTitlebar(""));
    }

    @Test
    public void testUpdateWhenWindowIdIsZero() {
        FRAME_TIMER.ShouldExecuteNextFrame = true;
        when(mockWindowResolutionManager.updateWindowSizeAndLocation(anyLong(),
                anyString())).thenReturn(0L);

        assertThrows(IllegalStateException.class, () ->
                coreLoop.startup(() -> closeAfterSomeTime(coreLoop)));
    }

    @Test
    public void testWhenFrameTimerDoesNotPermitNewFrames() {
        FRAME_TIMER.ShouldExecuteNextFrame = false;

        coreLoop.startup(() -> closeAfterSomeTime(coreLoop));

        verify(mockFrameExecutor, never()).execute(anyLong());
        verify(mockWindowResolutionManager, once())
                .updateWindowSizeAndLocation(anyLong(), anyString());
    }

    @Test
    public void testUpdateDimensionsOnFrame() {
        FRAME_TIMER.ShouldExecuteNextFrame = false;

        coreLoop.startup(() -> closeAfterSomeTime(coreLoop));


    }

    @Test
    public void testWhenFrameTimerPermitsNewFrames() {
        FRAME_TIMER.ShouldExecuteNextFrame = true;

        coreLoop.startup(() -> closeAfterSomeTime(coreLoop));

        // NB: This test is *somewhat* indeterminate, since the polling interval used from
        //     FrameTimer does *not* guarantee polling at *precisely* that rate; instead, it only
        //     specifies the delay between either the last time a frame was rendered, or the last
        //     time FrameRender instructed CoreLoop to not render a frame, and the next
        //     time CoreLoop asks FrameTimer whether to render the next frame. This test
        //     should no longer be indeterminate if CoreLoop is refactored to treat the
        //     polling interval as an actual interval, rather than merely a delay; however, since
        //     the interval should be very small in practice (e.g. 2-5ms), this slight
        //     indeterminacy should not radically affect performance.
        verify(mockWindowResolutionManager, atLeast(1))
                .updateWindowSizeAndLocation(anyLong(), anyString());
        verify(mockFrameExecutor, atLeast(1)).execute(GLOBAL_TIMESTAMP);
    }

    @Test
    public void testGraphicsPreloaderCalledBeforeFrameTimer() {
        List<Object> invokedClassesInOrder = listOf();
        FRAME_TIMER.AddThisWhenLoadIsCalled = GRAPHICS_PRELOADER.AddThisWhenLoadIsCalled =
                invokedClassesInOrder;

        coreLoop.startup(() -> closeAfterSomeTime(coreLoop));

        assertTrue(invokedClassesInOrder.size() >= 2);
        assertSame(GRAPHICS_PRELOADER, invokedClassesInOrder.get(0));
        assertSame(FRAME_TIMER, invokedClassesInOrder.get(1));
    }

    @Test
    public void testAudioLoaderCalledBeforeFrameTimer() {
        coreLoop.startup(() -> closeAfterSomeTime(coreLoop));

        //noinspection OptionalGetWithoutIsPresent
        verify(mockAudioLoader, once()).loadFromDirectory(
                eq(AUDIO_REL_DIRS.stream().findFirst().get()),
                same(IDS_FOR_FILENAMES),
                same(DEFAULT_LOOP_STOP_MS_BY_ID),
                same(DEFAULT_LOOP_RESTART_MS_BY_ID)
        );
    }

    @Test
    public void testMeshAndShaderPassedToRenderersAndGraphicsPreloaderCalledAndMouseCursorUpdateCalled() {
        FRAME_TIMER.ShouldExecuteNextFrame = false;

        coreLoop.startup(() -> closeAfterSomeTime(coreLoop));

        verify(MOCK_RENDERER, once()).setMesh(mockMesh);
        verify(mockShaderFactory, once()).make(SHADER_FILE_PREFIX);
        verify(MOCK_RENDERER, once()).setShader(mockShader);
        assertTrue(GRAPHICS_PRELOADER.LoadCalled);
        verify(mockMouseCursor, atLeastOnce()).updateCursor(anyLong());
    }

    // NB: It is impossible to directly test the calls to MouseListener, since even
    // glfwSetCursorPos does not trigger the cursor position callback. Refer to the display tests!

    private static void closeAfterSomeTime(CoreLoop coreLoop) {
        CheckedExceptionWrapper.sleep(100);

        while (coreLoop.windowId() <= 0) {
            CheckedExceptionWrapper.sleep(100);
        }

        glfwSetWindowShouldClose(coreLoop.windowId(), true);
    }
}
