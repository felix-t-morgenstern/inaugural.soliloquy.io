package inaugural.soliloquy.io.test.integration.display.fullsuite;

import inaugural.soliloquy.common.CommonModule;
import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.WindowResolution;
import inaugural.soliloquy.io.api.dto.AssetDefinitionsDTO;
import inaugural.soliloquy.tools.collections.Collections;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.gamestate.entities.Setting;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.rendering.FrameExecutor;
import soliloquy.specs.io.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.io.graphics.rendering.timing.FrameTimer;

import java.awt.*;
import java.util.function.BiConsumer;

import static inaugural.soliloquy.io.api.Settings.*;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DisplayTest {
    private final static String SHADER_FILENAME_PREFIX =
            "./src/main/resources/shaders/defaultShader";

    protected void runTest(
            String titlebar,
            AssetDefinitionsDTO assetDefinitionsDTO,
            Runnable displayTestThread,
            BiConsumer<IOModule, Component> populateTopLevelComponent
    ) {
        var commonModule = new CommonModule();
        //var gamestateModule = new GameStateModule();

        var meshVerticesAndUvCoords = new float[]{0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

        @SuppressWarnings("rawtypes") var settings = Collections.<String, Setting>mapOf(
                AUDIO_FILETYPES_ID,
                listOf(),
                PERIODS_PER_FRAME_RATE_REPORT_AGGREGATE_ID,
                generateMockSetting(10),
                FRAME_TIMER_POLLING_INTERVAL_ID,
                generateMockSetting(100),
                FRAME_EXECUTOR_SEMAPHORE_PERMISSIONS_ID,
                generateMockSetting(3),
                SHADER_FILENAME_PREFIX_ID,
                generateMockSetting(SHADER_FILENAME_PREFIX),
                MESH_VERTICES_ID,
                generateMockSetting(meshVerticesAndUvCoords),
                MESH_UV_COORDS_ID,
                generateMockSetting(meshVerticesAndUvCoords),
                MOUSE_CAPTURE_ALPHA_THRESHOLD_ID,
                generateMockSetting(0.5f),
                GRAPHICS_PRELOADER_THREAD_POOL_SIZE_ID,
                generateMockSetting(4),
                GRAPHICS_PRELOADER_ASSET_TYPE_BATCH_SIZES_ID,
                generateMockSetting(20),
                STARTING_WINDOW_DISPLAY_MODE_ID,
                generateMockSetting(WindowDisplayMode.WINDOWED),
                STARTING_WINDOW_RESOLUTION_ID,
                generateMockSetting(WindowResolution.RES_1680x1050),
                DEFAULT_FONT_COLOR_ID,
                generateMockSetting(Color.WHITE)
        );
        @SuppressWarnings("rawtypes") var actions = Collections.<String, Action>mapOf();

        var ioModule = new IOModule(
                commonModule,
                settings::get,
                actions::get,
                listOf(),
                titlebar,
                assetDefinitionsDTO
        );

        var coreLoop = ioModule.provide(GraphicsCoreLoop.class);

        var frameTimer = ioModule.provide(FrameTimer.class);
        frameTimer.setTargetFps(null);

        var frameExecutor = ioModule.provide(FrameExecutor.class);
//        var componentFactory = ioModule.provide(ComponentFactory)
//        frameExecutor.setTopLevelComponent(topLevelComponent);

        coreLoop.startup(displayTestThread);
    }

    private <T> Setting<T> generateMockSetting(T val) {
        @SuppressWarnings("unchecked") var mockSetting = (Setting<T>) mock(Setting.class);

        when(mockSetting.getValue()).thenReturn(val);

        return mockSetting;
    }
}
