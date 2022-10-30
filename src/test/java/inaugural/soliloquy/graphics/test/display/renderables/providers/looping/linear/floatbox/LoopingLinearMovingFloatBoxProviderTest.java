package inaugural.soliloquy.graphics.test.display.renderables.providers.looping.linear.floatbox;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.LoopingLinearMovingFloatBoxProvider;
import inaugural.soliloquy.graphics.test.display.rendering.renderers.spriterenderer.SpriteRendererTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBox;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBoxFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.HashMap;

import static org.mockito.Mockito.when;

class LoopingLinearMovingFloatBoxProviderTest extends SpriteRendererTest {
    protected static ProviderAtTime<FloatBox> RENDERING_DIMENSIONS_PROVIDER;

    protected static void graphicsPreloaderLoadAction() {
        long startTimestamp = GLOBAL_CLOCK.globalTimestamp();
        int periodDuration = 4000;
        int periodModuloOffset = periodDuration - (int) (startTimestamp % (periodDuration));
        FakeFloatBox origin = new FakeFloatBox(0.375f, 0.3125f, 0.625f, 0.6875f);
        float translationAmount = 0.25f;
        HashMap<Integer, FloatBox> valuesAtTimes = new HashMap<Integer, FloatBox>() {{
            put(0, origin.translate(-translationAmount, -translationAmount));
            put(1000, origin.translate(translationAmount, -translationAmount));
            put(2000, origin.translate(translationAmount, translationAmount));
            put(3000, origin.translate(-translationAmount, translationAmount));
        }};
        RENDERING_DIMENSIONS_PROVIDER =
                new LoopingLinearMovingFloatBoxProvider(java.util.UUID.randomUUID(), valuesAtTimes,
                        periodDuration, periodModuloOffset, null, null, new FakeFloatBoxFactory());
        when(SpriteRenderable.getRenderingDimensionsProvider()).thenReturn(
                RENDERING_DIMENSIONS_PROVIDER);

        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
