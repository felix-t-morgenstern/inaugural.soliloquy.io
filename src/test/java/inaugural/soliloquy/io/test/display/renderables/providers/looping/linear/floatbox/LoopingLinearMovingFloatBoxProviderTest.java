package inaugural.soliloquy.io.test.display.renderables.providers.looping.linear.floatbox;

import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.LoopingLinearMovingFloatBoxProvider;
import inaugural.soliloquy.io.test.display.rendering.renderers.spriterenderer.SpriteRendererTest;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.valueobjects.FloatBox.translate;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

class LoopingLinearMovingFloatBoxProviderTest extends SpriteRendererTest {
    protected static ProviderAtTime<FloatBox> RENDERING_DIMENSIONS_PROVIDER;

    protected static void graphicsPreloaderLoadAction() {
        var startTimestamp = GLOBAL_CLOCK.globalTimestamp();
        var periodDuration = 4000;
        var periodModuloOffset = periodDuration - (int) (startTimestamp % (periodDuration));
        var origin = floatBoxOf(0.375f, 0.3125f, 0.625f, 0.6875f);
        var translationAmount = 0.25f;
        var valuesAtTimes = mapOf(
            pairOf(0, translate(origin, -translationAmount, -translationAmount)),
            pairOf(1000, translate(origin, translationAmount, -translationAmount)),
            pairOf(2000, translate(origin, translationAmount, translationAmount)),
            pairOf(3000, translate(origin, -translationAmount, translationAmount))
        );
        RENDERING_DIMENSIONS_PROVIDER =
                new LoopingLinearMovingFloatBoxProvider(java.util.UUID.randomUUID(), valuesAtTimes,
                        periodDuration, periodModuloOffset, null, null);
        SpriteRenderable.setRenderingDimensionsProvider(RENDERING_DIMENSIONS_PROVIDER);

        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
