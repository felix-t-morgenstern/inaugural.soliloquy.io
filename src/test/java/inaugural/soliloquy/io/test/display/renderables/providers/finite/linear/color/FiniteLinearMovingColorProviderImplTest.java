package inaugural.soliloquy.io.test.display.renderables.providers.finite.linear.color;

import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.io.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.FiniteLinearMovingColorProviderImpl;
import inaugural.soliloquy.io.test.display.renderables.providers.finite.linear.FiniteLinearMovingProviderTest;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageDefinition;

import java.awt.*;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

class FiniteLinearMovingColorProviderImplTest extends FiniteLinearMovingProviderTest {
    protected static final float BORDER_THICKNESS = 0.05f;

    protected static void graphicsPreloaderLoadAction() {
        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        var timestamp = GLOBAL_CLOCK.globalTimestamp();
        var borderColorProviderValues = mapOf(
                pairOf(timestamp, new Color(68, 20, 135, 0)),
                pairOf(timestamp + 1000, new Color(0, 238, 255, 127)),
                pairOf(timestamp + 3000, new Color(83, 94, 112, 63))
        );
        var hueMovementIsClockwise = listOf(false, true);
        var finiteLinearMovingColorProvider =
                new FiniteLinearMovingColorProviderImpl(java.util.UUID.randomUUID(),
                        borderColorProviderValues, hueMovementIsClockwise, null, null);
        SpriteRenderable.setBorderColorProvider(finiteLinearMovingColorProvider);

        FirstChildStack.add(SpriteRenderable);
        Renderers.registerRenderer(SpriteRenderableImpl.class, SpriteRenderer);

        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
