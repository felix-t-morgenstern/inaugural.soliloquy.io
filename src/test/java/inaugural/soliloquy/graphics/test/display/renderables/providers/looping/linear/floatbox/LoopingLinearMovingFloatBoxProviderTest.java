package inaugural.soliloquy.graphics.test.display.renderables.providers.looping.linear.floatbox;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.LoopingLinearMovingFloatBoxProvider;
import inaugural.soliloquy.graphics.rendering.FloatBoxImpl;
import inaugural.soliloquy.graphics.test.display.rendering.renderers.spriterenderer.SpriteRendererTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBoxFactory;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.valueobjects.Pair.pairOf;

class LoopingLinearMovingFloatBoxProviderTest extends SpriteRendererTest {
    protected static ProviderAtTime<FloatBox> RENDERING_DIMENSIONS_PROVIDER;

    protected static void graphicsPreloaderLoadAction() {
        var startTimestamp = GLOBAL_CLOCK.globalTimestamp();
        var periodDuration = 4000;
        var periodModuloOffset = periodDuration - (int) (startTimestamp % (periodDuration));
        var origin = new FloatBoxImpl(0.375f, 0.3125f, 0.625f, 0.6875f);
        var translationAmount = 0.25f;
        var valuesAtTimes = mapOf(
            pairOf(0, origin.translate(-translationAmount, -translationAmount)),
            pairOf(1000, origin.translate(translationAmount, -translationAmount)),
            pairOf(2000, origin.translate(translationAmount, translationAmount)),
            pairOf(3000, origin.translate(-translationAmount, translationAmount))
        );
        RENDERING_DIMENSIONS_PROVIDER =
                new LoopingLinearMovingFloatBoxProvider(java.util.UUID.randomUUID(), valuesAtTimes,
                        periodDuration, periodModuloOffset, null, null, new FakeFloatBoxFactory());
        SpriteRenderable.setRenderingDimensionsProvider(RENDERING_DIMENSIONS_PROVIDER);

        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
