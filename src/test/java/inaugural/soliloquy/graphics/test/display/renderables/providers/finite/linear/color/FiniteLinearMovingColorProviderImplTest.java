package inaugural.soliloquy.graphics.test.display.renderables.providers.finite.linear.color;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingColorProviderImpl;
import inaugural.soliloquy.graphics.test.display.renderables.providers.finite.linear.FiniteLinearMovingProviderTest;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;

import java.awt.*;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

class FiniteLinearMovingColorProviderImplTest extends FiniteLinearMovingProviderTest {
    protected static final float BORDER_THICKNESS = 0.05f;

    protected static void graphicsPreloaderLoadAction() {
        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        var timestamp = GLOBAL_CLOCK.globalTimestamp();
        var borderColorProviderValues = mapOf(
                Pair.of(timestamp, new Color(68, 20, 135, 0)),
                Pair.of(timestamp + 1000, new Color(0, 238, 255, 127)),
                Pair.of(timestamp + 3000, new Color(83, 94, 112, 63))
        );
        var hueMovementIsClockwise = listOf(false, true);
        var finiteLinearMovingColorProvider =
                new FiniteLinearMovingColorProviderImpl(java.util.UUID.randomUUID(),
                        borderColorProviderValues, hueMovementIsClockwise, null, null);
        SpriteRenderable.setBorderColorProvider(finiteLinearMovingColorProvider);

        FirstChildStack.add(SpriteRenderable);
        Renderers.registerRenderer(soliloquy.specs.graphics.renderables.SpriteRenderable.class.getCanonicalName(), SpriteRenderer);

        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
