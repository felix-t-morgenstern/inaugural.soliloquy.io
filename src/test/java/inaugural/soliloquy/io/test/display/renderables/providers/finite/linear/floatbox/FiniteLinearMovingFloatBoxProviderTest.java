package inaugural.soliloquy.io.test.display.renderables.providers.finite.linear.floatbox;

import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.io.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.FiniteLinearMovingFloatBoxProvider;
import inaugural.soliloquy.io.test.display.renderables.providers.finite.linear.FiniteLinearMovingProviderTest;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageDefinition;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

class FiniteLinearMovingFloatBoxProviderTest extends FiniteLinearMovingProviderTest {
    protected static void graphicsPreloaderLoadAction() {
        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        var timestamp = GLOBAL_CLOCK.globalTimestamp();

        var startingLocation = floatBoxOf(0f, 0f, 0.25f, 0.375f);
        var endingLocation = floatBoxOf(0.25f, 0.125f, 0.75f, 0.875f);

        var renderingDimensionsAtTimes = mapOf(
                pairOf(timestamp, startingLocation),
                pairOf(timestamp + 3000, endingLocation)
        );

        var finiteLinearMovingFloatBoxProvider =
                new FiniteLinearMovingFloatBoxProvider(java.util.UUID.randomUUID(),
                        renderingDimensionsAtTimes, null, null);
        SpriteRenderable.setRenderingDimensionsProvider(finiteLinearMovingFloatBoxProvider);

        FirstChildStack.add(SpriteRenderable);
        Renderers.registerRenderer(SpriteRenderableImpl.class, SpriteRenderer);

        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
