package inaugural.soliloquy.io.test.display.renderables.providers.looping.linear.color;

import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.LoopingLinearMovingColorProviderImpl;
import inaugural.soliloquy.io.test.display.rendering.renderers.spriterenderer.SpriteRendererBorderTest;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingColorProvider;

import java.awt.*;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

class LoopingMovingColorProviderImplTest extends SpriteRendererBorderTest {
    protected static LoopingLinearMovingColorProvider BORDER_COLOR_PROVIDER;
    protected static float BORDER_THICKNESS = 0.03125f;

    protected static void graphicsPreloaderLoadAction() {
        var periodDuration = 4000;

        var valuesAtTimes = mapOf(
                pairOf(0, Color.RED),
                pairOf(periodDuration / 4, Color.BLUE),
                pairOf((periodDuration / 4) * 2, Color.GREEN),
                pairOf((periodDuration / 4) * 3, new Color(152, 52, 235))
        );

        var movementIsClockwise = listOf(true, false, true, false);

        BORDER_COLOR_PROVIDER =
                new LoopingLinearMovingColorProviderImpl(java.util.UUID.randomUUID(), valuesAtTimes,
                        movementIsClockwise, periodDuration, 0, null, null);
        SpriteRenderable.setBorderColorProvider(BORDER_COLOR_PROVIDER);

        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
