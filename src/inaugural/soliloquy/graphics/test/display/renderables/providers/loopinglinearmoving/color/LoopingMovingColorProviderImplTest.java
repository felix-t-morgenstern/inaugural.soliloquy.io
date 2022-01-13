package inaugural.soliloquy.graphics.test.display.renderables.providers.loopinglinearmoving.color;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.LoopingLinearMovingColorProviderImpl;
import inaugural.soliloquy.graphics.test.display.rendering.renderers.spriterenderer.SpriteRendererBorderTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingColorProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

class LoopingMovingColorProviderImplTest extends SpriteRendererBorderTest {
    protected static LoopingLinearMovingColorProvider BORDER_COLOR_PROVIDER;
    protected static float BORDER_THICKNESS = 0.03125f;

    protected static void graphicsPreloaderLoadAction() {
        int periodDuration = 4000;

        HashMap<Integer, Color> valuesAtTimes = new HashMap<Integer, Color>(){{
            put(0, Color.RED);
            put(periodDuration / 4, Color.BLUE);
            put((periodDuration / 4) * 2, Color.GREEN);
            put((periodDuration / 4) * 3, new Color(152, 52, 235));
        }};

        ArrayList<Boolean> movementIsClockwise = new ArrayList<Boolean>() {{
            add(true);
            add(false);
            add(true);
            add(false);
        }};

        SpriteRenderable.BorderColorProvider = BORDER_COLOR_PROVIDER =
                new LoopingLinearMovingColorProviderImpl(new FakeEntityUuid(), valuesAtTimes,
                        movementIsClockwise, periodDuration, 0, null, null);

        Sprite.Image = new ImageFactoryImpl(0.5f).make(RPG_WEAPONS_RELATIVE_LOCATION, false);
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
