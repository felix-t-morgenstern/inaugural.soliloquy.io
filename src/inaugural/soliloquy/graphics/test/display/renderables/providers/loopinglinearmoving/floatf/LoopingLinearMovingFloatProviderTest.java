package inaugural.soliloquy.graphics.test.display.renderables.providers.loopinglinearmoving.floatf;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.LoopingLinearMovingFloatProvider;
import inaugural.soliloquy.graphics.test.display.rendering.renderers.spriterenderer.SpriteRendererBorderTest;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.util.HashMap;

class LoopingLinearMovingFloatProviderTest extends SpriteRendererBorderTest {
    protected static ProviderAtTime<Float> BORDER_THICKNESS_PROVIDER;

    protected static void graphicsPreloaderLoadAction() {
        long startTimestamp = GLOBAL_CLOCK.globalTimestamp();
        int periodDuration = 2000;
        int periodModuloOffset = (int) (startTimestamp % periodDuration);
        float maxBorderThickness = 0.03125f;
        HashMap<Integer, Float> valuesAtTimes = new HashMap<Integer, Float>() {{
            put(0, maxBorderThickness * 0.125f);
            put(250, maxBorderThickness * 0.75f);
            put(500, maxBorderThickness * 0.25f);
            put(750, maxBorderThickness);
        }};
        SpriteRenderable.BorderThicknessProvider = BORDER_THICKNESS_PROVIDER =
                new LoopingLinearMovingFloatProvider(java.util.UUID.randomUUID(), valuesAtTimes,
                        periodDuration, periodModuloOffset, null, null);

        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
