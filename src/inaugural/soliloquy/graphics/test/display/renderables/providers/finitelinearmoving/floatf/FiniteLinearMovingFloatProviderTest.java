package inaugural.soliloquy.graphics.test.display.renderables.providers.finitelinearmoving.floatf;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingFloatProvider;
import inaugural.soliloquy.graphics.test.display.renderables.providers.finitelinearmoving.FiniteLinearMovingProviderTest;

import java.awt.*;
import java.util.HashMap;

class FiniteLinearMovingFloatProviderTest extends FiniteLinearMovingProviderTest {
    protected static final Color BORDER_COLOR = Color.getHSBColor(0.75f, 1f, 1f);
    protected static final Float BORDER_THICKNESS = 0.025f;

    protected static void graphicsPreloaderLoadAction() {
        Sprite.Image = new ImageFactoryImpl(0.5f).make(RPG_WEAPONS_RELATIVE_LOCATION, false);
        long timestamp = GLOBAL_CLOCK.globalTimestamp();
        HashMap<Long, Float> borderThicknessProvider = new HashMap<>();
        borderThicknessProvider.put(timestamp, 0f);
        borderThicknessProvider.put(timestamp + 3000, BORDER_THICKNESS);
        SpriteRenderable.BorderThicknessProvider = new FiniteLinearMovingFloatProvider(
                java.util.UUID.randomUUID(),
                borderThicknessProvider,
                null, null
        );
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
