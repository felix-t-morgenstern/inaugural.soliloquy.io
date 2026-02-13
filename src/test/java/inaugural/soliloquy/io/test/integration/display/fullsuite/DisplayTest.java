package inaugural.soliloquy.io.test.integration.display.fullsuite;

import inaugural.soliloquy.common.CommonModule;
import inaugural.soliloquy.io.IOMethods;
import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.WindowResolution;
import inaugural.soliloquy.io.api.dto.*;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.collections.Collections;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.common.entities.Methods;
import soliloquy.specs.gamestate.entities.Setting;
import soliloquy.specs.io.audio.entities.SoundsPlaying;
import soliloquy.specs.io.audio.factories.SoundFactory;
import soliloquy.specs.io.bootstrap.CoreLoop;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.ComponentFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.FrameExecutor;
import soliloquy.specs.io.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.io.graphics.rendering.timing.FrameTimer;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;

import java.awt.*;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import static inaugural.soliloquy.io.api.Constants.*;
import static inaugural.soliloquy.io.api.Settings.*;
import static inaugural.soliloquy.io.api.dto.AssetType.*;
import static inaugural.soliloquy.tools.CheckedExceptionWrapper.sleep;
import static inaugural.soliloquy.tools.Tools.defaultIfNull;
import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.reflection.Reflection.readMethods;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class DisplayTest {
    protected final static WindowResolution DEFAULT_RES = WindowResolution.RES_1680x1050;
    private final static String SHADER_FILENAME_PREFIX =
            "./src/main/resources/shaders/defaultShader";

    protected final static String SPRITE_ID = "spriteId";
    protected final static String RPG_WEAPONS_RELATIVE_LOCATION =
            "./src/test/resources/images/items/RPG_Weapons.png";
    protected final static String TILE_LOCATION_RELATIVE_LOCATION =
            "./src/test/resources/images/tiles/sergey-shmidt-koy6FlCCy5s-unsplash.jpg";

    protected final static String EXPLOSION_ANIMATION_ID = "explosion";
    protected final static String EXPLOSION_RELATIVE_LOCATION =
            "./src/test/resources/images/effects/Explosion.png";
    protected final static AnimationDefinitionDTO EXPLOSION_ANIMATION_DEF =
            new AnimationDefinitionDTO(
                    EXPLOSION_ANIMATION_ID,
                    600,
                    IntStream.range(0, 11).mapToObj(i -> new AnimationFrameDefinitionDTO(
                            EXPLOSION_RELATIVE_LOCATION,
                            i * 50,
                            i * 96,
                            0,
                            (i + 1) * 96,
                            96,
                            0,
                            0
                    )).toArray(AnimationFrameDefinitionDTO[]::new)
            );

    protected final static String TORCH_ANIMATION_ID = "torchAnimation";
    protected final static String TORCH_RELATIVE_LOCATION =
            "./src/test/resources/images/fixtures/animated_torch_numbered.png";
    protected final static AnimationDefinitionDTO TORCH_ANIMATION_DEF = new AnimationDefinitionDTO(
            TORCH_ANIMATION_ID,
            1800,
            IntStream.range(0, 8).mapToObj(i -> new AnimationFrameDefinitionDTO(
                    TORCH_RELATIVE_LOCATION,
                    i * 200,
                    i * 32,
                    0,
                    (i + 1) * 32,
                    64,
                    0,
                    0
            )).toArray(AnimationFrameDefinitionDTO[]::new)
    );
    protected final static String TORCH_GLOBAL_LOOPING_ANIMATION_ID = "torchGlobalAnimation";

    protected final static String IMAGE_ASSET_SET_ID = "imageAssetSet";
    protected final static String IMAGE_ASSET_SET_DISPLAY_PARAM_KEY =
            "imageAssetSetDisplayParamKey";
    protected final static String IMAGE_ASSET_SET_DISPLAY_PARAM_VAL_SPRITE =
            "imageAssetSetDisplayParamValSprite";
    protected final static String IMAGE_ASSET_SET_DISPLAY_PARAM_VAL_ANIM =
            "imageAssetSetDisplayParamValAnim";
    protected final static ImageAssetSetDefinitionDTO IMAGE_ASSET_SET_DEF =
            new ImageAssetSetDefinitionDTO(
                    IMAGE_ASSET_SET_ID,
                    new ImageAssetSetAssetDefinitionDTO(
                            SPRITE.getValue(),
                            SPRITE_ID,
                            new ImageAssetSetAssetDefinitionDTO.DisplayParamDefinitionDTO(
                                    IMAGE_ASSET_SET_DISPLAY_PARAM_KEY,
                                    IMAGE_ASSET_SET_DISPLAY_PARAM_VAL_SPRITE
                            )
                    ),
                    new ImageAssetSetAssetDefinitionDTO(
                            ANIMATION.getValue(),
                            EXPLOSION_ANIMATION_ID,
                            new ImageAssetSetAssetDefinitionDTO.DisplayParamDefinitionDTO(
                                    IMAGE_ASSET_SET_DISPLAY_PARAM_KEY,
                                    IMAGE_ASSET_SET_DISPLAY_PARAM_VAL_ANIM
                            )
                    )
            );

    protected final static String CINZEL_ID = "cinzel";
    protected final static String RELATIVE_LOCATION_CINZEL =
            "./src/test/resources/fonts/Cinzel-VariableFont_wght.ttf";
    protected final static float MAX_LOSSLESS_FONT_SIZE_CINZEL = 200f;
    protected final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_CINZEL = 0.25f;
    protected final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_CINZEL = 0.25f;
    protected final static float LEADING_ADJUSTMENT_CINZEL = 0f;
    protected final static FontStyleDefinitionGlyphPropertyDTO[] CINZEL_PLAIN_WIDTH_FACTORS =
            arrayOf(
                    new FontStyleDefinitionGlyphPropertyDTO('U', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('V', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('W', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('X', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('u', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('v', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('w', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('x', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('À', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('Á', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('Â', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('Ã', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('Ä', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('Å', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('Ü', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('ß', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('à', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('á', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('â', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('ã', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('ä', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('å', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('ë', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('ü', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('þ', 0.99f)
            );
    protected final static FontStyleDefinitionGlyphPropertyDTO[] CINZEL_ITALIC_WIDTH_FACTORS =
            arrayOf(
                    new FontStyleDefinitionGlyphPropertyDTO('I', 0.965f),
                    new FontStyleDefinitionGlyphPropertyDTO('W', 0.975f),
                    new FontStyleDefinitionGlyphPropertyDTO('i', 0.960f),
                    new FontStyleDefinitionGlyphPropertyDTO('w', 0.975f),
                    new FontStyleDefinitionGlyphPropertyDTO('^', 0.975f),
                    new FontStyleDefinitionGlyphPropertyDTO('À', 0.98f),
                    new FontStyleDefinitionGlyphPropertyDTO('Á', 0.98f),
                    new FontStyleDefinitionGlyphPropertyDTO('Â', 0.98f),
                    new FontStyleDefinitionGlyphPropertyDTO('Ã', 0.98f),
                    new FontStyleDefinitionGlyphPropertyDTO('Ä', 0.98f),
                    new FontStyleDefinitionGlyphPropertyDTO('Å', 0.98f),
                    new FontStyleDefinitionGlyphPropertyDTO('ß', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('à', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('á', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('â', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('ã', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('ä', 0.99f),
                    new FontStyleDefinitionGlyphPropertyDTO('å', 0.99f)
            );

    protected final static FontStyleDefinitionDTO CINZEL_PLAIN_DTO =
            new FontStyleDefinitionDTO(
                    ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_CINZEL,
                    arrayOf(),
                    arrayOf(),
                    CINZEL_PLAIN_WIDTH_FACTORS,
                    ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_CINZEL);
    protected final static FontStyleDefinitionDTO CINZEL_ITALIC_DTO =
            new FontStyleDefinitionDTO(
                    ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_CINZEL,
                    arrayOf(),
                    arrayOf(),
                    CINZEL_ITALIC_WIDTH_FACTORS,
                    ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_CINZEL);
    protected final static FontStyleDefinitionDTO CINZEL_BOLD_DTO =
            new FontStyleDefinitionDTO(
                    ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_CINZEL,
                    arrayOf(),
                    arrayOf(),
                    CINZEL_PLAIN_WIDTH_FACTORS,
                    ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_CINZEL);
    protected final static FontStyleDefinitionDTO CINZEL_BOLD_ITALIC_DTO =
            new FontStyleDefinitionDTO(
                    ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_CINZEL,
                    arrayOf(),
                    arrayOf(),
                    CINZEL_ITALIC_WIDTH_FACTORS,
                    ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_CINZEL);
    protected final static FontDefinitionDTO CINZEL_DEF =
            new FontDefinitionDTO(CINZEL_ID, RELATIVE_LOCATION_CINZEL,
                    MAX_LOSSLESS_FONT_SIZE_CINZEL, LEADING_ADJUSTMENT_CINZEL, CINZEL_PLAIN_DTO,
                    CINZEL_ITALIC_DTO, CINZEL_BOLD_DTO, CINZEL_BOLD_ITALIC_DTO);

    protected final Methods METHODS;

    private final static String AUDIO_DIR_RELATIVE_PATH = "\\src\\test\\resources\\sounds\\";
    protected final static String PRESS_SOUND_ID = "pressSoundId";
    protected final static String RELEASE_SOUND_ID = "releaseSoundId";

    protected static IOModule ioModule;
    private static GlobalClock Clock;
    @SuppressWarnings("rawtypes") private static java.util.function.BiFunction<UUID, Object, ProviderAtTime>
            StaticProviderFactory;

    public Component topLevelComponent;

    public DisplayTest() {
        this(setOf());
    }

    public DisplayTest(@SuppressWarnings("rawtypes") Set<Consumer> consumers) {
        METHODS = new Methods();
        Check.ifNull(consumers, "consumers")
                .forEach(action -> METHODS.CONSUMERS.put(action.id(), action));
    }

    public void runTest(
            String testName,
            AssetDefinitionsDTO assetDefinitionsDTO,
            Runnable displayTest,
            BiConsumer<IOModule, Component> populateTopLevelComponent
    ) {
        var commonModule = new CommonModule();

        var meshVerticesAndUvCoords = new float[]{0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

        // Many of these are dummy values which should be tweaked for performance
        @SuppressWarnings("rawtypes") var settings = Collections.<String, Setting>mapOf(
                AUDIO_FILETYPES_ID,
                generateMockSetting(setOf("wav", "mp3")),
                PERIODS_PER_FRAME_RATE_REPORT_AGGREGATE_ID,
                generateMockSetting(1),
                FRAME_TIMER_POLLING_INTERVAL_ID,
                generateMockSetting(-1),
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
                generateMockSetting(8),
                GRAPHICS_PRELOADER_ASSET_TYPE_BATCH_SIZES_ID,
                generateMockSetting(mapOf(setOf(
                        IMAGE,
                        SPRITE,
                        ANIMATION,
                        GLOBAL_LOOPING_ANIMATION,
                        IMAGE_ASSET_SET,
                        FONT,
                        MOUSE_CURSOR_IMAGE,
                        ANIMATED_MOUSE_CURSOR_PROVIDER,
                        STATIC_MOUSE_CURSOR_PROVIDER
                ).stream().map(assetType -> pairOf(assetType, 10)))),
                STARTING_WINDOW_DISPLAY_MODE_ID,
                generateMockSetting(WindowDisplayMode.WINDOWED),
                STARTING_WINDOW_RESOLUTION_ID,
                generateMockSetting(DEFAULT_RES),
                DEFAULT_FONT_COLOR_ID,
                generateMockSetting(Color.WHITE),
                AUDIO_RELATIVE_DIRS_ID,
                generateMockSetting(setOf(AUDIO_DIR_RELATIVE_PATH))
        );

        ioModule = new IOModule(
                commonModule,
                settings::get,
                METHODS,
                mapOf(
                        pairOf(
                                CONSOLE_FRAME_RATE_REPORTER,
                                a -> System.out.printf(
                                        "FPS >> [%s] target = %s, actual = %s%n",
                                        a.periodStart(), a.targetFps(), a.actualFps()
                                )
                        )
                ),
                testName,
                mapOf(
                        "JDSherbert - Ultimate UI SFX Pack - Cursor - 5.wav",
                        PRESS_SOUND_ID,
                        "JDSherbert - Ultimate UI SFX Pack - Select - 1.wav",
                        RELEASE_SOUND_ID
                ),
                mapOf(),
                mapOf(),
                assetDefinitionsDTO
        );

        var ioMethods = new IOMethods(
                ioModule.provide(SoundsPlaying.class),
                ioModule.provide(SoundFactory.class)
        );
        METHODS.concatenate(readMethods(ioMethods));

        DisplayTestMethods.PlaySound = ioMethods::playSound;

        METHODS.concatenate(readMethods(DisplayTestMethods.class));

        var coreLoop = ioModule.provide(CoreLoop.class);

        var frameTimer = ioModule.provide(FrameTimer.class);
        frameTimer.setTargetFps(null);

        var frameExecutor = ioModule.provide(FrameExecutor.class);
        var componentFactory = ioModule.provide(ComponentFactory.class);
        StaticProviderFactory = ioModule.provide(STATIC_PROVIDER_FACTORY);
        var wholeScreenProvider = staticProvider(WHOLE_SCREEN);
        topLevelComponent = componentFactory.make(randomUUID(), 0, setOf(), false, 0,
                staticProvider(floatBoxOf(0f, 0f)), wholeScreenProvider, null, null, null, mapOf());
        frameExecutor.setTopLevelComponent(topLevelComponent);

        coreLoop.startup(() -> {
            if (populateTopLevelComponent != null) {
                populateTopLevelComponent.accept(ioModule, topLevelComponent);
            }

            displayTest.run();
        });
    }

    protected static <T> ProviderAtTime<T> staticProvider(T val) {
        //noinspection unchecked
        return StaticProviderFactory.apply(randomUUID(), val);
    }

    protected static <T> ProviderAtTime<T> nullProvider() {
        return staticProvider(null);
    }

    protected static void runThenClose(String testName, int ms) {
        System.out.println(testName + " display test started");
        sleep(ms);
        System.out.println(testName + " display test ended");
    }

    private <T> Setting<T> generateMockSetting(T val) {
        @SuppressWarnings("unchecked") var mockSetting = (Setting<T>) mock(Setting.class);

        when(mockSetting.getValue()).thenReturn(val);

        return mockSetting;
    }

    protected static long timestamp() {
        return (Clock = defaultIfNull(Clock, ioModule.provide(GlobalClock.class)))
                .globalTimestamp();
    }
}
