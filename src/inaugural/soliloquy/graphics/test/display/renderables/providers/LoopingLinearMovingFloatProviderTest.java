package inaugural.soliloquy.graphics.test.display.renderables.providers;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.LoopingLinearMovingFloatProvider;
import inaugural.soliloquy.graphics.test.display.rendering.renderers.spriterenderer.SpriteRendererBorderTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.util.HashMap;

class LoopingLinearMovingFloatProviderTest extends SpriteRendererBorderTest {
    protected static ProviderAtTime<Float> BORDER_THICKNESS_PROVIDER;

    protected static void graphicsPreloaderLoadAction() {
        long startTimestamp = new FakeGlobalClock().globalTimestamp();
        int periodDuration = 2000;
        int periodModuloOffset = (int)(startTimestamp % periodDuration);
        float maxBorderThickness = 0.03125f;
        HashMap<Long, Float> valuesAtTimes = new HashMap<Long, Float>() {{
            put(0L, maxBorderThickness * 0.125f);
            put(250L, maxBorderThickness * 0.75f);
            put(500L, maxBorderThickness * 0.25f);
            put(750L, maxBorderThickness);
        }};
        SpriteRenderable.BorderThicknessProvider = BORDER_THICKNESS_PROVIDER =
                new LoopingLinearMovingFloatProvider(new FakeEntityUuid(), valuesAtTimes,
                        periodDuration, periodModuloOffset, null, null);

        Sprite.Image = new ImageFactoryImpl(0.5f).make(RPG_WEAPONS_RELATIVE_LOCATION, false);
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
