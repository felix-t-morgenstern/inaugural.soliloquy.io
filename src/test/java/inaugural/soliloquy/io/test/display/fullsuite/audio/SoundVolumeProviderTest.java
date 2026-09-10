package inaugural.soliloquy.io.test.display.fullsuite.audio;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.*;
import inaugural.soliloquy.io.test.display.fullsuite.DisplayTest;
import soliloquy.specs.io.audio.factories.SoundFactory;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingProvider;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactory;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;

import java.util.UUID;
import java.util.function.BiFunction;

import static inaugural.soliloquy.io.api.Constants.STATIC_PROVIDER_FACTORY;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.exception.CheckedExceptionWrapper.sleep;
import static java.util.UUID.randomUUID;

public class SoundVolumeProviderTest extends DisplayTest {
    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Play sound on mouse events display test",
                new AssetDefinitionsDTO(
                        new ImageDefinitionDTO[]{},
                        new FontDefinitionDTO[]{},
                        new SpriteDefinitionDTO[]{},
                        new AnimationDefinitionDTO[]{},
                        new GlobalLoopingAnimationDefinitionDTO[]{},
                        new ImageAssetSetDefinitionDTO[]{},
                        new MouseCursorImageDefinitionDTO[]{},
                        new AnimatedMouseCursorDefinitionDTO[]{},
                        new StaticMouseCursorDefinitionDTO[]{}
                ),
                SoundVolumeProviderTest::runThenClose,
                SoundVolumeProviderTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {

    }

    private static void runThenClose(IOModule ioModule) {
        var clock = ioModule.provide(GlobalClock.class);
        var timestamp = clock.globalTimestamp();

        @SuppressWarnings("rawtypes") BiFunction<UUID, Object, ProviderAtTime>
                staticProviderFactory = ioModule.provide(STATIC_PROVIDER_FACTORY);
        var finiteLinearFactory = ioModule.provide(FiniteLinearMovingProviderFactory.class);

        var initialDelay = 5000;
        var fadeTime = 8000;
        var playTime = 8000;
        var exitDelay = 2000;

        ProviderAtTime<Float> fadeInAndOut = finiteLinearFactory.make(
                randomUUID(),
                mapOf(
                        timestamp, 0f,
                        timestamp + initialDelay, 0f,
                        timestamp + initialDelay + fadeTime, 1f,
                        timestamp + initialDelay + fadeTime + playTime, 1f,
                        timestamp + initialDelay + fadeTime + fadeTime, 0f
                ),
                null
        );

        var soundFactory = ioModule.provide(SoundFactory.class);
        var sound = soundFactory.make(
                DISPLAY_TEST_MUSIC_ID,
                fadeInAndOut
        );
        sound.play();
        sleep(initialDelay + fadeTime + fadeTime + exitDelay);
    }
}
