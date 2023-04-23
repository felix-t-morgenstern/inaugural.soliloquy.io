package inaugural.soliloquy.graphics.test.display.renderables.providers.looping.linear.color;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.LoopingLinearMovingColorProviderImpl;
import inaugural.soliloquy.graphics.test.display.rendering.renderers.spriterenderer.SpriteRendererBorderTest;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingColorProvider;

import java.awt.*;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

class LoopingMovingColorProviderImplTest extends SpriteRendererBorderTest {
    protected static LoopingLinearMovingColorProvider BORDER_COLOR_PROVIDER;
    protected static float BORDER_THICKNESS = 0.03125f;

    protected static void graphicsPreloaderLoadAction() {
        var periodDuration = 4000;

        var valuesAtTimes = mapOf(
                Pair.of(0, Color.RED),
                Pair.of(periodDuration / 4, Color.BLUE),
                Pair.of((periodDuration / 4) * 2, Color.GREEN),
                Pair.of((periodDuration / 4) * 3, new Color(152, 52, 235))
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
