package inaugural.soliloquy.graphics.test.display.renderables.providers.finite.linear.color;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingColorProviderImpl;
import inaugural.soliloquy.graphics.test.display.renderables.providers.finite.linear.FiniteLinearMovingProviderTest;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

class FiniteLinearMovingColorProviderImplTest extends FiniteLinearMovingProviderTest {
    protected static final float BORDER_THICKNESS = 0.05f;

    protected static void graphicsPreloaderLoadAction() {
        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        long timestamp = GLOBAL_CLOCK.globalTimestamp();
        HashMap<Long, Color> borderColorProviderValues = new HashMap<Long, Color>() {{
            put(timestamp, new Color(68, 20, 135, 0));
            put(timestamp + 1000, new Color(0, 238, 255, 127));
            put(timestamp + 3000, new Color(83, 94, 112, 63));
        }};
        ArrayList<Boolean> hueMovementIsClockwise = new ArrayList<Boolean>() {{
            add(false);
            add(true);
        }};
        SpriteRenderable.BorderColorProvider = new FiniteLinearMovingColorProviderImpl(
                java.util.UUID.randomUUID(), borderColorProviderValues, hueMovementIsClockwise,
                null, null);
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
