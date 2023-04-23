package inaugural.soliloquy.graphics.test.display.renderables.providers.looping.linear.floatf;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.LoopingLinearMovingFloatProvider;
import inaugural.soliloquy.graphics.test.display.rendering.renderers.spriterenderer.SpriteRendererBorderTest;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;

class LoopingLinearMovingFloatProviderTest extends SpriteRendererBorderTest {
    protected static ProviderAtTime<Float> BORDER_THICKNESS_PROVIDER;

    protected static void graphicsPreloaderLoadAction() {
        var startTimestamp = GLOBAL_CLOCK.globalTimestamp();
        var periodDuration = 2000;
        var periodModuloOffset = (int) (startTimestamp % periodDuration);
        var maxBorderThickness = 0.03125f;
        var valuesAtTimes = mapOf(
            Pair.of(0, maxBorderThickness * 0.125f),
            Pair.of(250, maxBorderThickness * 0.75f),
            Pair.of(500, maxBorderThickness * 0.25f),
            Pair.of(750, maxBorderThickness)
        );
        BORDER_THICKNESS_PROVIDER =
                new LoopingLinearMovingFloatProvider(java.util.UUID.randomUUID(), valuesAtTimes,
                        periodDuration, periodModuloOffset, null, null);
        SpriteRenderable.setBorderThicknessProvider(BORDER_THICKNESS_PROVIDER);

        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
