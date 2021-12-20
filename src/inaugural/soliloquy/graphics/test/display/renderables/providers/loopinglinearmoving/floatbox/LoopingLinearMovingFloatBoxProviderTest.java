package inaugural.soliloquy.graphics.test.display.renderables.providers.loopinglinearmoving.floatbox;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.LoopingLinearMovingFloatBoxProvider;
import inaugural.soliloquy.graphics.test.display.rendering.renderers.spriterenderer.SpriteRendererTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.HashMap;

class LoopingLinearMovingFloatBoxProviderTest extends SpriteRendererTest {
    protected static ProviderAtTime<FloatBox> RENDERING_DIMENSIONS_PROVIDER;

    protected static void graphicsPreloaderLoadAction() {
        long startTimestamp = new FakeGlobalClock().globalTimestamp();
        int periodDuration = 4000;
        int periodModuloOffset = periodDuration - (int)(startTimestamp % (periodDuration));
        FakeFloatBox origin = new FakeFloatBox(0.375f, 0.3125f, 0.625f, 0.6875f);
        float translationAmount = 0.25f;
        HashMap<Integer, FloatBox> valuesAtTimes = new HashMap<Integer, FloatBox>() {{
            put(0, origin.translate(-translationAmount, -translationAmount));
            put(1000, origin.translate(translationAmount, -translationAmount));
            put(2000, origin.translate(translationAmount, translationAmount));
            put(3000, origin.translate(-translationAmount, translationAmount));
        }};

        SpriteRenderable.RenderingDimensionsProvider = RENDERING_DIMENSIONS_PROVIDER =
                new LoopingLinearMovingFloatBoxProvider(new FakeEntityUuid(), valuesAtTimes,
                        periodDuration, periodModuloOffset, null, null, new FakeFloatBoxFactory());

        Sprite.Image = new ImageFactoryImpl(0.5f).make(RPG_WEAPONS_RELATIVE_LOCATION, false);
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
