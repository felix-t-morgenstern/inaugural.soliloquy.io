package inaugural.soliloquy.graphics.test.display.renderables.providers;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingColorProviderImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

class FiniteLinearMovingColorProviderImplTest extends FiniteLinearMovingProviderTest {
    protected static final float BORDER_THICKNESS = 0.05f;

    protected static void graphicsPreloaderLoadAction() {
        Sprite.Image = new ImageFactoryImpl(0.5f).make(RPG_WEAPONS_RELATIVE_LOCATION, false);
        long timestamp = new FakeGlobalClock().globalTimestamp();
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
                new FakeEntityUuid(), borderColorProviderValues, hueMovementIsClockwise,
                null, null);
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
